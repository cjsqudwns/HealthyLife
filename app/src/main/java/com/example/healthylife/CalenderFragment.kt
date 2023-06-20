package com.example.healthylife

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthylife.DietInfoData
import com.example.healthylife.ExerciseInfoData
import com.example.healthylife.databinding.FragmentCalenderBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
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
        initRecyclerView()
        auth = FirebaseAuth.getInstance()
        runBlocking {
            init()
            initRecyclerView()

        }
        return binding!!.root
    }
    fun init(){
        val selectedDateMillis = binding!!.calenderView.date
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.timeInMillis = selectedDateMillis
        setInputDay(binding!!.calenderView, selectedCalendar.get(Calendar.YEAR), selectedCalendar.get(Calendar.MONTH), selectedCalendar.get(Calendar.DAY_OF_MONTH))
        auth = FirebaseAuth.getInstance()
        getData()
        getDietData()
        //initRecyclerView()
        binding!!.calenderView.setOnDateChangeListener { calendarView, year, month, day ->
            setInputDay(calendarView, year, month, day)
            getData()
            getDietData()
            //initRecyclerView()
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
                                                if (exerciseArea != null && memo != null) {
                                                    val exerciseInfoData = ExerciseInfoData(
                                                        day = inputday!!,
                                                        startTime = startTime,
                                                        exercise_area = exerciseArea,
                                                        minute = minute,
                                                        memo = memo,
                                                        check = false
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
                                                if (dietPart != null && memo != null&&calrorie!=null&&tansu!=null&&protein!=null&&fat!=null) {
                                                    val dietInputData = DietInfoData(
                                                        day=inputday!!,
                                                        dietPart=dietPart,
                                                        startTime=startTime,
                                                        calorie=calrorie,
                                                        memoDiet=memo,
                                                        carbs = tansu,
                                                        protein = protein,
                                                        fat = fat,
                                                        check = false
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
        adapterExercise = ExerciseInfoRecyclerViewAdapter(arrayListOf())
        adapterExercise = ExerciseInfoRecyclerViewAdapter(exerciseInfoDataList)
        for(i in exerciseInfoDataList){
            Log.d("Tag","${i.exercise_area} 출력됨!")
            Log.d("Tag",i.startTime)
        }
        binding!!.recyclerViewExerciseInfo.adapter = adapterExercise
        //식단 정보 recyclerView
        binding!!.recyclerViewDietInfo.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        binding!!.recyclerViewDietInfo.addItemDecoration(DividerItemDecoration(this.requireContext(), LinearLayoutManager.VERTICAL))
        adapterDiet = DietInfoRecyclerViewAdapter(dietInfoDataList)
        binding!!.recyclerViewDietInfo.adapter = adapterDiet

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
