package com.example.healthylife.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.healthylife.R
import com.example.healthylife.databinding.ActivityAddDietInfoBinding
import java.util.Calendar

class AddDietInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddDietInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddDietInfoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initSpinner()
        manageBtn()
    }

    private fun manageBtn(){
        binding.apply {
            cancleBtn.setOnClickListener {
                finish()
            }
            saveBtn.setOnClickListener {
                //파이어베이스 연동
            }
        }
    }

    private fun initSpinner(){
        val diet_part_string = resources.getStringArray(R.array.diet_part)
        val ampm = resources.getStringArray(R.array.time_spinner_ampm)
        val hour = resources.getStringArray(R.array.time_spinner_hour)
        val minute = resources.getStringArray(R.array.time_spinner_minute)

        val diet_part_adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item, diet_part_string
        )
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
        binding.spinnerStartHour.adapter = adapter_hour
        binding.spinnerStartMinute.adapter = adapter_minute
        binding.spinnerDietPart.adapter = diet_part_adapter

        Current_Spinner()

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
                    //binding.spinnerFinishAmpm.setSelection(position)
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
                    //binding.spinnerFinishHour.setSelection(position)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //TODO("Not yet implemented")
                }
            }
        }
    }
    private fun Current_Spinner(){
        //현재 시간 설정
        val currentTime = getCurrentTime()
        val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
        var currentMinute = currentTime.get(Calendar.MINUTE)

        // 현재 시간에 맞게 스피너 설정
        binding.spinnerStartAmpm.setSelection(if(currentHour < 12) 0 else 1)
        binding.spinnerStartHour.setSelection(if (currentHour%12 ==0) 11  else (currentHour%12)-1)
        binding.spinnerStartMinute.setSelection(if (currentMinute==0) 0 else currentMinute/5)
    }

    private fun getCurrentTime(): Calendar {
        return Calendar.getInstance()
    }
}