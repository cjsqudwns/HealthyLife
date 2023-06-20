package com.example.healthylife

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthylife.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    var category = arrayListOf<String>("운동 부위 별", "식사 시간 별")
    var binding: FragmentHomeBinding ?=null
    lateinit var adapterExercise: ExerciseInfoRecyclerViewAdapter
    lateinit var adapterDiet: DietInfoRecyclerViewAdapter
    var exerciseInfoDataList: MutableList<ExerciseInfoData> = mutableListOf()
    var dietInfoDataList: MutableList<DietInfoData> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
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
                        var exerciseArea = binding!!.spinnerType2.selectedItem.toString()
                        // + exerciseArea(전신 ..)에 해당하는 값들을 데이터 베이스에서 읽어 리스트에 담는 get함수
                        initExerciseRecyclerView(exerciseArea)
                    }
                    1 -> {
                        binding!!.spinnerType2.adapter = ArrayAdapter(
                            this@HomeFragment.requireContext(),
                            android.R.layout.simple_spinner_dropdown_item, mealTime)
                        var dietPart = binding!!.spinnerType2.selectedItem.toString()
                        // + dietPart(아점, 점심 ..)에 해당하는 값들을 데이터 베이스에서 읽어 리스트에 담는 get함수
                        initDietRecyclerView(dietPart)
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
                startActivity(intent)
            }
            addDietBtn.setOnClickListener {
                val intent = Intent(requireContext(), AddDietInfoActivity::class.java)
                startActivity(intent)
            }
        }
    }
    //여기도 데베 연결 작업 해주긴 해야하나..?
    fun initExerciseRecyclerView(exerciseArea:String){
        //운동 정보 recyclerView
        binding!!.recyclerView.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        binding!!.recyclerView.addItemDecoration(DividerItemDecoration(this.requireContext(), LinearLayoutManager.VERTICAL))
        adapterExercise = ExerciseInfoRecyclerViewAdapter(exerciseInfoDataList)
        binding!!.recyclerView.adapter = adapterExercise

    }
    fun initDietRecyclerView(diet:String){
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}