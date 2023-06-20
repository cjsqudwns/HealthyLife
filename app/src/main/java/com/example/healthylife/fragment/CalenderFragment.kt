package com.example.healthylife

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthylife.data.*
import com.example.healthylife.data.ExerciseInfoData
import com.example.healthylife.databinding.FragmentCalenderBinding
import java.util.Date

class CalenderFragment : Fragment() {
    var binding:FragmentCalenderBinding ?= null
    lateinit var adapterExercise: ExerciseInfoRecyclerViewAdapter
    var arr = arrayListOf<ExerciseInfoData>(ExerciseInfoData("2023-06-20", "13:00", "전신", 45, "ㅁㄴㅇㄹㅁㅇㄴㄹ"))
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalenderBinding.inflate(layoutInflater, container, false)
        initRecyclerView()
        return binding!!.root
    }

    fun initRecyclerView(){
        binding!!.recyclerViewExerciseInfo.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        //binding!!.recyclerView_exerciseInfo.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        binding!!.recyclerViewExerciseInfo.addItemDecoration(DividerItemDecoration(this.requireContext(), LinearLayoutManager.VERTICAL))
        adapterExercise = ExerciseInfoRecyclerViewAdapter(arrayListOf())
        binding!!.recyclerViewExerciseInfo.adapter = adapterExercise

    }
}