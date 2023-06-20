package com.example.healthylife

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthylife.AddDietInfoActivity
import com.example.healthylife.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.Calendar
import java.util.Date
import kotlin.math.floor

class HomeFragment : Fragment() {

    var category = arrayListOf<String>("운동 부위 별", "식사 시간 별")
    var binding: FragmentHomeBinding ?=null
    lateinit var auth:FirebaseAuth
    lateinit var adapterExercise: ExerciseInfoRecyclerViewAdapter
    lateinit var adapterDiet: DietInfoRecyclerViewAdapter
    var exerciseInfoDataList: ArrayList<ExerciseInfoData> = arrayListOf()
    var dietInfoDataList: ArrayList<DietInfoData> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        auth = FirebaseAuth.getInstance()
        initSpinner()
        moveAddActivity()
        return binding!!.root
    }

    fun initSpinner(){
        val exerciseArea = resources.getStringArray(R.array.exercise_area)
        var mealTime = resources.getStringArray(R.array.mealTime)
        val adapter1 = ArrayAdapter(this.requireContext(),
            android.R.layout.simple_spinner_dropdown_item, category)
        binding!!.spinnerType1.adapter = adapter1
        binding!!.spinnerType2.adapter = ArrayAdapter(this.requireContext(),
            android.R.layout.simple_spinner_dropdown_item, exerciseArea)
        binding!!.spinnerType1.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position){
                    0 -> {
                        binding!!.spinnerType2.adapter = ArrayAdapter(
                            this@HomeFragment.requireContext(),
                            android.R.layout.simple_spinner_dropdown_item, exerciseArea)
                        binding!!.spinnerType2.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ){
                                //구문 작성
                                val inputExerciseArea = binding!!.spinnerType2.selectedItem.toString()
                                // + exerciseArea(전신 ..)에 해당하는 값들을 데이터베이스에서 읽어 리스트에 담는 get함수
                                getData(inputExerciseArea)
                                initExerciseRecyclerView()
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }
                        }


                    }
                    1 -> {
                        binding!!.spinnerType2.adapter = ArrayAdapter(
                            this@HomeFragment.requireContext(),
                            android.R.layout.simple_spinner_dropdown_item, mealTime)
                        binding!!.spinnerType2.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ){
                                //구문 작성
                                val dietPart = binding!!.spinnerType2.selectedItem.toString()
                                // + dietPart(아점, 점심 ..)에 해당하는 값들을 데이터베이스에서 읽어 리스트에 담는 get함수
                                getDietData(dietPart)
                                initDietRecyclerView()
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }
                        }

                    }

                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

    }
    fun moveAddActivity(){
        binding!!.apply {
            addExerciseBtn.setOnClickListener {
                val intent = Intent(requireContext(), AddExerciseInfoActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            addDietBtn.setOnClickListener {
                val intent = Intent(requireContext(), AddDietInfoActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }
    //여기도 데베 연결 작업 해주긴 해야하나..?
    fun initExerciseRecyclerView(){
        //운동 정보 recyclerView
        binding!!.recyclerView.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        binding!!.recyclerView.addItemDecoration(DividerItemDecoration(this.requireContext(), LinearLayoutManager.VERTICAL))
        adapterExercise = ExerciseInfoRecyclerViewAdapter(exerciseInfoDataList)
        binding!!.recyclerView.adapter = adapterExercise

    }
    fun initDietRecyclerView(){
        //식단 정보 recyclerView
        binding!!.recyclerView.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        binding!!.recyclerView.addItemDecoration(DividerItemDecoration(this.requireContext(), LinearLayoutManager.VERTICAL))
        adapterDiet = DietInfoRecyclerViewAdapter(dietInfoDataList)
//        adapterDiet.itemClickListener = object : DietInfoRecyclerViewAdapter.OnItemClickListener {
//            override fun OnItemClick(data: DietInfoData, position: Int) {
//                adapterDiet.detailOnClick(position)
//            }
//        }
        binding!!.recyclerView.adapter = adapterDiet



    }
    fun getData(inputExerciseArea:String) {
        exerciseInfoDataList.clear()
        val collectionRef =
            FirebaseFirestore.getInstance().collection("UserInfo").document(auth.currentUser!!.uid)
                .collection("ExerciseInfo")
        collectionRef.get().addOnSuccessListener {

            for (i in it.documents) {
                    i.reference.get().addOnSuccessListener { dateSnapshot: DocumentSnapshot ->
                        val temp =resources.getStringArray(R.array.exercise_area)
                        for(area in temp){
                            dateSnapshot.reference.collection(area).get()
                                .addOnSuccessListener { areaSnapshot: QuerySnapshot ->
                                    for (j in areaSnapshot.documents) {
                                        j.reference.get()
                                            .addOnSuccessListener { finalSnapshot: DocumentSnapshot ->
                                                val startTime =
                                                    formatTimestamp(finalSnapshot.getDate("startTime")!!)
                                                val exerciseArea = finalSnapshot.getString("exerciseArea")
                                                val minute = calculateDurationInMinutes(
                                                    finalSnapshot.getDate("startTime")!!,
                                                    finalSnapshot.getDate("finishTime")!!
                                                )
                                                val memo = finalSnapshot.getString("memo")
                                                val check = finalSnapshot.getBoolean("check")
                                                if (exerciseArea == inputExerciseArea && memo != null&&check==true) {
                                                    val exerciseInfoData = ExerciseInfoData(
                                                        day = dateSnapshot.id,
                                                        startTime = startTime,
                                                        exercise_area = exerciseArea,
                                                        minute = minute,
                                                        memo = memo,
                                                        check = check,
                                                        did = finalSnapshot.id
                                                    )

                                                    exerciseInfoDataList.add(exerciseInfoData)
                                                    initExerciseRecyclerView()
                                                    //initRecyclerView()
                                                }
                                            }
                                    }

                                }
                        }

                    }

            }
        }
    }

    fun getDietData(inputDiet:String) {
        dietInfoDataList.clear()
        val collectionRef =
            FirebaseFirestore.getInstance().collection("UserInfo").document(auth.currentUser!!.uid)
                .collection("DietInfo")
        collectionRef.get().addOnSuccessListener {
            Log.d("TAG1", it.documents.toString())

            for (i in it.documents) {
                Log.d("TAG2", i.id)
                    i.reference.get().addOnSuccessListener { dateSnapshot: DocumentSnapshot ->
                        Log.d("TAG3", dateSnapshot.id)
                        val temp =resources.getStringArray(R.array.mealTime)
                        for(area in temp){
                            dateSnapshot.reference.collection(area).get()
                                .addOnSuccessListener { areaSnapshot: QuerySnapshot ->
                                    Log.d("TAG4", areaSnapshot.documents.toString())
                                    for (j in areaSnapshot.documents) {
                                        j.reference.get()
                                            .addOnSuccessListener { finalSnapshot: DocumentSnapshot ->
                                                Log.d("TAG5", finalSnapshot.id)
                                                val startTime =
                                                    formatTimestamp(finalSnapshot.getDate("startTime")!!)
                                                val calrorie = finalSnapshot.getLong("calrorie")?.toInt()
                                                val dietPart = finalSnapshot.getString("dietPart")
                                                val memo = finalSnapshot.getString("memoFood")
                                                val tansu= finalSnapshot.getLong("tansu")?.toInt()
                                                val protein= finalSnapshot.getLong("protein")?.toInt()
                                                val fat= finalSnapshot.getLong("fat")?.toInt()
                                                val checked = finalSnapshot.getBoolean("check")
                                                if (dietPart == inputDiet && memo != null&&calrorie!=null&&tansu!=null&&protein!=null&&fat!=null&&checked==true) {
                                                    val dietInputData = DietInfoData(
                                                        day=dateSnapshot.id,
                                                        dietPart=dietPart,
                                                        startTime=startTime,
                                                        calorie=calrorie,
                                                        memoDiet=memo,
                                                        carbs = tansu,
                                                        protein = protein,
                                                        fat = fat,
                                                        check = checked,
                                                        did = finalSnapshot.id
                                                    )

                                                    dietInfoDataList.add(dietInputData)
                                                    //initRecyclerView()
                                                    initDietRecyclerView()
                                                }
                                            }
                                    }

                                }
                        }

                    }

            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    fun calculateDurationInMinutes(startTimeStamp: Date, endTimeStamp: Date): Int {
        val durationInMillis = endTimeStamp.time - startTimeStamp.time
        val minutes = floor(durationInMillis.toDouble() / (1000 * 60)).toInt()
        return minutes
    }
    fun formatTimestamp(date: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = date

        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val amPm = if (hourOfDay < 12) "오전" else "오후"
        val formattedHour = if (hourOfDay % 12 == 0) 12 else hourOfDay % 12

        return "$amPm ${formattedHour}시 ${minute}분"
    }
}