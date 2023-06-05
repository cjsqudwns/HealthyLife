package com.example.healthylife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import com.example.healthylife.databinding.ActivityAddExerciseInfoBinding

class AddExerciseInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddExerciseInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExerciseInfoBinding.inflate(layoutInflater)
        initSpinner()
        manageBtn()
        setContentView(binding.root)
    }

    fun manageBtn() {
        binding.apply {
            cancleBtn.setOnClickListener {
                finish()
            }
            saveBtn.setOnClickListener {

            }
        }
    }

    fun initSpinner() {
        val ampm = resources.getStringArray(R.array.time_spinner_ampm)
        val hour = resources.getStringArray(R.array.time_spinner_hour)
        val minute = resources.getStringArray(R.array.time_spinner_minute)
        val exerciseArea = resources.getStringArray(R.array.exercise_area)

        val adapter_ampm = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item, ampm
        )
        val adapter_hour = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item, hour
        )
        val adapter_minute = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item, minute
        )
        binding.spinnerStartAmpm.adapter = adapter_ampm
        binding.spinnerFinishAmpm.adapter = adapter_ampm
        binding.spinnerStartHour.adapter = adapter_hour
        binding.spinnerFinishHour.adapter = adapter_hour
        binding.spinnerStartMinute.adapter = adapter_minute
        binding.spinnerFinishMinute.adapter = adapter_minute
        binding.spinnerExerciseArea.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item, exerciseArea
        )

        //시작시간 선택시 종료시간 자동 체크
        binding.apply {
            spinnerStartAmpm.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(    //시작시간 선택 시 종료시간 자동 선택
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // 시작 시간이 선택되었을 때 실행되는 코드
                    binding.spinnerFinishAmpm.setSelection(position)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //TODO("Not yet implemented")
                }
            }
            spinnerStartHour.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(    //시작시간 선택 시 종료시간 자동 선택
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.spinnerFinishHour.setSelection(position+1)  //시작시간 선택시 1시간 후의 시간 자동 선택
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //TODO("Not yet implemented")
                }
            }
        }
    }
}