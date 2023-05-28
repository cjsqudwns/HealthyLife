package com.example.healthylife

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.healthylife.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    var category = arrayListOf<String>("운동 부위 별", "식단 성분 별")
    var exerciseArea = arrayListOf<String>("전신", "가슴", "등", "어깨",
        "하체", "팔", "유산소", "복근", "기타")
    var nutrient = arrayListOf<String>("탄수화물", "단백질", "지방")
    var binding: FragmentHomeBinding ?=null

    var itemArr = arrayListOf<String>("아메리카노", "카페라떼", "카푸치노")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        initSpinner()
        initRecyclerView()
        return binding!!.root
    }

    fun initSpinner(){
        val adapter1 = ArrayAdapter(this.requireContext(),
            android.R.layout.simple_spinner_dropdown_item, category)
        binding!!.spinner1.adapter = adapter1
        binding!!.spinner2.adapter = ArrayAdapter(this.requireContext(),
            android.R.layout.simple_spinner_dropdown_item, exerciseArea)
        binding!!.spinner1.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position){
                    0 -> binding!!.spinner2.adapter = ArrayAdapter(this@HomeFragment.requireContext(),
                        android.R.layout.simple_spinner_dropdown_item, exerciseArea)
                    1 -> binding!!.spinner2.adapter = ArrayAdapter(this@HomeFragment.requireContext(),
                        android.R.layout.simple_spinner_dropdown_item, nutrient)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
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