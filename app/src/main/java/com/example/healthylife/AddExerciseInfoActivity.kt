package com.example.healthylife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import com.example.healthylife.databinding.ActivityAddExerciseInfoBinding

class AddExerciseInfoActivity : AppCompatActivity() {
    lateinit var binding:ActivityAddExerciseInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExerciseInfoBinding.inflate(layoutInflater)
        initSpinner()
        manageBtn()
        setContentView(binding.root)
    }

    fun manageBtn(){
        binding.apply {
            cancleBtn.setOnClickListener {
                finish()
            }
            saveBtn.setOnClickListener {

            }
        }
    }

    fun initSpinner(){
        val ampm = resources.getStringArray(R.array.time_spinner_ampm)
        val hour = resources.getStringArray(R.array.time_spinner_hour)
        val minute = resources.getStringArray(R.array.time_spinner_minute)
        val exerciseArea = resources.getStringArray(R.array.exercise_area)

        val adapter_ampm = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item, ampm)
        val adapter_hour = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item, hour)
        val adapter_minute = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item, minute)
        binding.spinnerStartAmpm.adapter = adapter_ampm
        binding.spinnerFinishAmpm.adapter = adapter_ampm
        binding.spinnerStartHour.adapter = adapter_hour
        binding.spinnerFinishHour.adapter = adapter_hour
        binding.spinnerStartMinute.adapter = adapter_minute
        binding.spinnerFinishMinute.adapter = adapter_minute
        binding.spinnerExerciseArea.adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item, exerciseArea)

    }
}