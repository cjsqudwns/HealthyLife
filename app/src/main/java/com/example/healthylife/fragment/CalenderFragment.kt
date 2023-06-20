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
import com.example.healthylife.data.DietInfoData
import com.example.healthylife.databinding.FragmentCalenderBinding
import java.util.Calendar

class CalenderFragment : Fragment() {
    var binding:FragmentCalenderBinding ?= null
    lateinit var adapterExercise: ExerciseInfoRecyclerViewAdapter
    lateinit var adapterDiet: DietInfoRecyclerViewAdapter
    var arr = arrayListOf<ExerciseInfoData>(ExerciseInfoData("2023-06-20", "13:00", "전신", 45, "ㅁㄴㅇㄹㅁㅇㄴㄹ"))
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalenderBinding.inflate(layoutInflater, container, false)
        initRecyclerView()
        selectDay()
        return binding!!.root
    }
    fun selectDay(){
        val calender = binding!!.calenderView

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
}