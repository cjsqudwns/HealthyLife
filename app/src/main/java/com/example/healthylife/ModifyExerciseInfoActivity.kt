package com.example.healthylife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.healthylife.databinding.ActivityModifyDietInfoBinding
import com.example.healthylife.databinding.ActivityModifyExerciseInfoBinding

class ModifyExerciseInfoActivity : AppCompatActivity() {
    lateinit var binding : ActivityModifyExerciseInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityModifyExerciseInfoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Datainit()
    }
    private fun Datainit(){

    }
}