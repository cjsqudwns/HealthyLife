package com.example.healthylife

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthylife.DietInfoData
import com.example.healthylife.ExerciseInfoData
import com.example.healthylife.databinding.FragmentCalenderBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.runBlocking
import java.util.Calendar
import java.util.Date
import kotlin.math.floor

class CalenderFragment : Fragment() {
    var binding:FragmentCalenderBinding ?= null
    lateinit var adapterExercise: ExerciseInfoRecyclerViewAdapter
    lateinit var adapterDiet: DietInfoRecyclerViewAdapter
    lateinit var auth:FirebaseAuth
    val exerciseInfoDataList: MutableList<ExerciseInfoData> = mutableListOf()
    val dietInfoDataList:MutableList<DietInfoData> = mutableListOf()
    var inputday:String? = null
    var calenderMonth:Int = 0
    var calenderDay:Int = 1
    var calenderYear:Int = 2023




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalenderBinding.inflate(layoutInflater, container, false)
        //initRecyclerView()
        auth = FirebaseAuth.getInstance()
        init()
        /*getData()
        getDietData()
        initRecyclerView()
*/
        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
        getData()
        getDietData()
        initRecyclerView()
    }
    fun init(){
        val selectedDateMillis = binding!!.calenderView.date
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.timeInMillis = selectedDateMillis
        setInputDay(binding!!.calenderView, selectedCalendar.get(Calendar.YEAR), selectedCalendar.get(Calendar.MONTH), selectedCalendar.get(Calendar.DAY_OF_MONTH))
        auth = FirebaseAuth.getInstance()
        //getData()
        //getDietData()
        //initRecyclerView()
        binding!!.calenderView.setOnDateChangeListener { calendarView, year, month, day ->
            setInputDay(calendarView, year, month, day)
            getData()
            getDietData()
            initRecyclerView()
        }
    }
    fun setInputDay(calendarView: CalendarView, year:Int, month:Int, day:Int){
        calenderMonth = month+1
        calenderDay = day
        calenderYear = year
        inputday = year.toString() + String.format("%02d", month+1)+String.format("%02d", day)
        //Log.d("TAG", inputday.toString())
    }

    fun getData() {
        exerciseInfoDataList.clear()
        val collectionRef =
            FirebaseFirestore.getInstance().collection("UserInfo").document(auth.currentUser!!.uid)
                .collection("ExerciseInfo")
        collectionRef.get().addOnSuccessListener {

            for (i in it.documents) {
                if(i.id == inputday){
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
                                                if (exerciseArea != null && memo != null&&check!=null) {
                                                    val exerciseInfoData = ExerciseInfoData(
                                                        day = inputday!!,
                                                        startTime = startTime,
                                                        exercise_area = exerciseArea,
                                                        minute = minute,
                                                        memo = memo,
                                                        check = check,
                                                        did = finalSnapshot.id
                                                    )

                                                    exerciseInfoDataList.add(exerciseInfoData)
                                                    initRecyclerView()
                                                }
                                            }
                                    }

                                }
                        }

                    }
                }
            }
        }
    }

    fun getDietData() {
        dietInfoDataList.clear()
        val collectionRef =
            FirebaseFirestore.getInstance().collection("UserInfo").document(auth.currentUser!!.uid)
                .collection("DietInfo")
        collectionRef.get().addOnSuccessListener {
            Log.d("TAG1", it.documents.toString())

            for (i in it.documents) {
                Log.d("TAG2", i.id)
                if(i.id == inputday){
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
                                                if (dietPart != null && memo != null&&calrorie!=null&&tansu!=null&&protein!=null&&fat!=null&&checked!=null) {
                                                    val dietInputData = DietInfoData(
                                                        day=inputday!!,
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
                                                    initRecyclerView()
                                                }
                                            }
                                    }

                                }
                        }

                    }
                }
            }
        }
    }

    fun initRecyclerView(){
        //운동 정보 recyclerView
        binding!!.recyclerViewExerciseInfo.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        //binding!!.recyclerView_exerciseInfo.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        binding!!.recyclerViewExerciseInfo.addItemDecoration(DividerItemDecoration(this.requireContext(), LinearLayoutManager.VERTICAL))
        adapterExercise = ExerciseInfoRecyclerViewAdapter(exerciseInfoDataList)
        adapterExercise.itemClickListener = object : ExerciseInfoRecyclerViewAdapter.OnItemClickListener {
            // 즐겨찾기 (별)
            override fun OnStarClick(data: ExerciseInfoData, position: Int) {
                // 해당 데이터 check value 바꾸기(false -> true, true -> false) 데이터베이스
                val collectionRef = FirebaseFirestore.getInstance().collection("UserInfo")
                    .document(auth.currentUser!!.uid)
                    .collection("ExerciseInfo")
                    .document(data.day)
                    .collection(data.exercise_area)
                    .document(data.did)

                collectionRef.get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val currentCheckValue = documentSnapshot.getBoolean("check") ?: false
                            val updatedCheckValue = !currentCheckValue

                            val updateData = hashMapOf<String, Any>(
                                "check" to updatedCheckValue
                            )

                            collectionRef.update(updateData)
                                .addOnSuccessListener {
                                    adapterExercise.favoritesSelected(position,updatedCheckValue)
                                }
                                .addOnFailureListener { e ->
                                    adapterExercise.favoritesSelected(position,false)
                                }
                        } else {
                            val updateData = hashMapOf<String, Any>(
                                "check" to true
                            )

                            collectionRef.set(updateData, SetOptions.merge())
                                .addOnSuccessListener {
                                    adapterExercise.favoritesSelected(position,true)
                                }
                                .addOnFailureListener { e ->
                                    adapterExercise.favoritesSelected(position,false)
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        adapterExercise.favoritesSelected(position,false)
                    }

            }
            // 데이터 수정
            override fun modifyData(data: ExerciseInfoData, position: Int) {
                /*// 수정하러 modifyExerciseInfoActivity로 intent
                val intent = Intent(requireContext(),ModifyExerciseInfoActivity::class.java)
                intent.putExtra("modify_data",data) as ExerciseInfoData //이거 왜안됨?
                startActivity(intent)*/
            }
        }
        binding!!.recyclerViewExerciseInfo.adapter = adapterExercise
        val simpleCallbackExercise = object: ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_delete, null)
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                val dialog = mBuilder.show()
                mDialogView.findViewById<Button>(R.id.OKBtn).setOnClickListener {
                    // 해당 데이터 delete 데이터베이스
                    adapterExercise.deleteRecyclerView(viewHolder.adapterPosition)
                    adapterExercise.deleteData(viewHolder.adapterPosition)
                    adapterExercise.notifyDataSetChanged()
                    dialog.dismiss()
                }
                mDialogView.findViewById<Button>(R.id.cancleBtn).setOnClickListener {
                    dialog.dismiss()
                    adapterExercise.notifyDataSetChanged()
                }
            }
        }
        val itemTouchHelperExercise = ItemTouchHelper(simpleCallbackExercise)
        itemTouchHelperExercise.attachToRecyclerView(binding!!.recyclerViewExerciseInfo)
        //식단 정보 recyclerView
        binding!!.recyclerViewDietInfo.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        binding!!.recyclerViewDietInfo.addItemDecoration(DividerItemDecoration(this.requireContext(), LinearLayoutManager.VERTICAL))
        adapterDiet = DietInfoRecyclerViewAdapter(dietInfoDataList)
        adapterDiet.itemClickListener = object : DietInfoRecyclerViewAdapter.OnItemClickListener {
            // 즐겨찾기 (별)
            override fun OnStarClick(data: DietInfoData, position: Int) {
                // 해당 데이터 check value 바꾸기(false -> true, true -> false) 데이터베이스
                val collectionRef = FirebaseFirestore.getInstance().collection("UserInfo")
                    .document(auth.currentUser!!.uid)
                    .collection("ExerciseInfo")
                    .document(data.day)
                    .collection(data.dietPart)
                    .document(data.did)

                collectionRef.get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val currentCheckValue = documentSnapshot.getBoolean("check") ?: false
                            val updatedCheckValue = !currentCheckValue

                            val updateData = hashMapOf<String, Any>(
                                "check" to updatedCheckValue
                            )

                            collectionRef.update(updateData)
                                .addOnSuccessListener {
                                    adapterDiet.favoritesSelected(position,updatedCheckValue)
                                }
                                .addOnFailureListener { e ->
                                    adapterDiet.favoritesSelected(position,false)
                                }
                        } else {
                            val updateData = hashMapOf<String, Any>(
                                "check" to true
                            )

                            collectionRef.set(updateData, SetOptions.merge())
                                .addOnSuccessListener {
                                    adapterDiet.favoritesSelected(position,true)
                                }
                                .addOnFailureListener { e ->
                                    adapterDiet.favoritesSelected(position,false)
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        adapterDiet.favoritesSelected(position,false)
                    }

            }
            // 데이터 수정
            override fun modifyData(data: DietInfoData, position: Int) {
                // 수정하러 modifyExerciseInfoActivity로 intent
            }
        }
        binding!!.recyclerViewDietInfo.adapter = adapterDiet
        // swipe 시 해당 데이터 delete
        val simpleCallbackDiet = object: ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_delete, null)
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                val dialog = mBuilder.show()
                mDialogView.findViewById<Button>(R.id.OKBtn).setOnClickListener {
                    // 해당 데이터 delete 데이터베이스
                    adapterDiet.deleteRecyclerView(viewHolder.adapterPosition)
                    adapterDiet.deleteData(viewHolder.adapterPosition)

                    dialog.dismiss()
                }
                mDialogView.findViewById<Button>(R.id.cancleBtn).setOnClickListener {
                    dialog.dismiss()
                    adapterDiet.notifyDataSetChanged()
                }
            }
        }
        val itemTouchHelperDiet = ItemTouchHelper(simpleCallbackDiet)
        itemTouchHelperDiet.attachToRecyclerView(binding!!.recyclerViewDietInfo)

        adapterExercise.notifyDataSetChanged()
        adapterDiet.notifyDataSetChanged()


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
