package com.example.healthylife.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.core.view.get
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthylife.DietInfoRecyclerViewAdapter
import com.example.healthylife.data.ExerciseInfoData
import com.example.healthylife.ExerciseInfoRecyclerViewAdapter
import com.example.healthylife.R
import com.example.healthylife.data.DietInfoData
import com.example.healthylife.databinding.FragmentCalenderBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar
import java.util.Date
import kotlin.math.floor

class CalenderFragment : Fragment() {
    var binding:FragmentCalenderBinding ?= null
    lateinit var adapterExercise: ExerciseInfoRecyclerViewAdapter
    lateinit var adapterDiet: DietInfoRecyclerViewAdapter
    lateinit var auth:FirebaseAuth
    val exerciseInfoDataList: MutableList<ExerciseInfoData> = mutableListOf()
    var arr = arrayListOf<ExerciseInfoData>(ExerciseInfoData("2023-06-20", "13:00", "전신", 45, "ㅁㄴㅇㄹㅁㅇㄴㄹ",false))
    private val calendar = Calendar.getInstance()
    private var currentMonth = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalenderBinding.inflate(layoutInflater, container, false)
        auth = FirebaseAuth.getInstance()
        initRecyclerView()
        selectDay()
        return binding!!.root
    }
    fun selectDay(){
        val calenderV = binding!!.calenderView
        calenderV.setOnDateChangeListener { calendarView, year, month, day
            ->  getData(year,month,day)}

    }
    fun getData(year:Int,month:Int,day:Int){
        exerciseInfoDataList.clear()
        val userdata = FirebaseFirestore.getInstance().collection("UserInfo").document(auth.currentUser!!.uid)
        val inputday = year.toString() + String.format("%02d", month+1)+String.format("%02d", day)
        userdata.collection("ExerciseInfo").document(inputday).get().addOnSuccessListener { DocumentsSnapshot ->
            //class ExerciseInfoData (val day: String, val startTime: String, val exercise_area: String, val minute: Int, val memo: String)
            val exerciseAreas = resources.getStringArray(R.array.exercise_area)
            for (area in exerciseAreas) {
                if (DocumentsSnapshot.exists()) {
                    DocumentsSnapshot.reference.collection(area).get().addOnSuccessListener {
                        if (it.isEmpty) {
                            // 컬렉션이 비어있음
                        } else {
                            // 컬렉션에 문서가 존재함
                            for (document in it.documents) {
                                val startTime = document.getString("startTime")
                                val exerciseArea = document.getString("exercise_area")
                                val minute = calculateDurationInMinutes(document.getDate("startTime")!!,document.getDate("finishTime")!!)
                                val memo = document.getString("memo")

                                if (startTime != null && exerciseArea != null && memo != null) {
                                    val exerciseInfoData = ExerciseInfoData(
                                        day = inputday,
                                        startTime = startTime,
                                        exercise_area = exerciseArea,
                                        minute = minute,
                                        memo = memo,
                                        check = false
                                    )
                                    exerciseInfoDataList.add(exerciseInfoData)
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
        binding!!.recyclerViewExerciseInfo.addItemDecoration(DividerItemDecoration(this.requireContext(), LinearLayoutManager.VERTICAL))
        adapterExercise = ExerciseInfoRecyclerViewAdapter(arrayListOf())
        binding!!.recyclerViewExerciseInfo.adapter = adapterExercise
        //식단 정보 recyclerView
        binding!!.recyclerViewDietInfo.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        binding!!.recyclerViewDietInfo.addItemDecoration(DividerItemDecoration(this.requireContext(), LinearLayoutManager.VERTICAL))
        adapterDiet = DietInfoRecyclerViewAdapter(arrayListOf())
//        adapterDiet.itemClickListener = object : DietInfoRecyclerViewAdapter.OnItemClickListener {
//            override fun OnItemClick(data: DietInfoData, position: Int) {
//                adapterDiet.detailOnClick(position)
//            }
//        }
        binding!!.recyclerViewDietInfo.adapter = adapterDiet



    }

    fun calculateDurationInMinutes(startTimeStamp: Date, endTimeStamp: Date): Int {
        val durationInMillis = endTimeStamp.time - startTimeStamp.time
        val minutes = floor(durationInMillis.toDouble() / (1000 * 60)).toInt()
        return minutes
    }
}