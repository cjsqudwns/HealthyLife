package com.example.healthylife

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.healthylife.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    var category = arrayListOf<String>("운동 부위 별", "식사 시간 별")

    var binding: FragmentHomeBinding ?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        initSpinner()
        initRecyclerView()
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
                    0 -> binding!!.spinnerType2.adapter = ArrayAdapter(this@HomeFragment.requireContext(),
                        android.R.layout.simple_spinner_dropdown_item, exerciseArea)
                    1 -> binding!!.spinnerType2.adapter = ArrayAdapter(this@HomeFragment.requireContext(),
                        android.R.layout.simple_spinner_dropdown_item, mealTime)
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
        }
    }
    fun initRecyclerView(){
        if (view is RecyclerView){
            with(view){
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}