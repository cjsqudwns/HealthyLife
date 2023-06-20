package com.example.healthylife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.healthylife.databinding.ActivityModifyDietInfoBinding
import com.example.healthylife.databinding.ActivityModifyExerciseInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ModifyExerciseInfoActivity : AppCompatActivity() {
    lateinit var binding : ActivityModifyExerciseInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityModifyExerciseInfoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Datainit()
        //initSpinner()
    }
    private fun Datainit(){
        val intent = intent
        val data = intent.getSerializableExtra("modify_data") as ExerciseInfoData
        initSpinner(data.startTime, data.exercise_area)
        Log.i("ds",data.startTime)
        binding.memo.setText(data.memo)
    }

    fun initSpinner(startTime : String,exercise_area:String) {
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




    }
}