package com.example.healthylife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.healthylife.databinding.ActivityAddDietInfoBinding
import com.example.healthylife.databinding.ActivityModifyDietInfoBinding

class ModifyDietInfoActivity : AppCompatActivity() {
    lateinit var binding : ActivityModifyDietInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityModifyDietInfoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Datainit()
    }
    private fun Datainit(){

    }

}