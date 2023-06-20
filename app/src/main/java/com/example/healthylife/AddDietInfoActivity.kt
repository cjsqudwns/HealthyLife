package com.example.healthylife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.Toast
import com.example.healthylife.R
import com.example.healthylife.databinding.ActivityAddDietInfoBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Calendar

class AddDietInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddDietInfoBinding
    lateinit var auth: FirebaseAuth
    var inputday:String? = null
    var calenderMonth:Int = 0
    var calenderDay:Int = 1
    var calenderYear:Int = 2023
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddDietInfoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
        initSpinner()
        manageBtn()
    }
    fun setInputDay(calendarView: CalendarView, year:Int, month:Int, day:Int){
        calenderMonth = month+1
        calenderDay = day
        calenderYear = year
        inputday = year.toString() + String.format("%02d", month+1)+String.format("%02d", day)
        Log.d("TAG", inputday.toString())
    }
    fun init(){
        val selectedDateMillis = binding.calenderView.date
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.timeInMillis = selectedDateMillis
        setInputDay(binding.calenderView, selectedCalendar.get(Calendar.YEAR), selectedCalendar.get(Calendar.MONTH), selectedCalendar.get(Calendar.DAY_OF_MONTH))
        auth = FirebaseAuth.getInstance()
        binding.calenderView.setOnDateChangeListener { calendarView, year, month, day ->
            setInputDay(calendarView, year, month, day)
        }
    }
    fun getStartTime(): Timestamp {
        var hours = Integer.parseInt(binding.spinnerStartHour.selectedItem.toString())
        val minutes = Integer.parseInt(binding.spinnerStartMinute.selectedItem.toString())
        if(binding.spinnerStartAmpm.selectedItem as String == "오후"){
            hours +=12
        }
        val temp = LocalDateTime.of(calenderYear, calenderMonth, calenderDay, hours, minutes)
        val timestamp = Timestamp(temp.toEpochSecond(ZoneOffset.ofHours(9)), 0)
        return timestamp
    }
    private fun manageBtn() {
        binding.apply {
            cancleBtn.setOnClickListener {
                finish()
            }
            saveBtn.setOnClickListener {
                val data = hashMapOf(
                    "startTime" to getStartTime(),
                    "dietPart" to binding.spinnerDietPart.selectedItem.toString(),
                    "calrorie" to binding.EditeTextCalorie.text.toString().toInt(),
                    "tansu" to binding.tansu.text.toString().toInt(),
                    "protein" to binding.protein.text.toString().toInt(),
                    "fat" to binding.fat.text.toString().toInt(),
                    "memoFood" to binding.memoFood.text.toString(),
                    "check" to false
                )
                val data2 = hashMapOf(
                    "calrorie" to binding.EditeTextCalorie.text.toString().toInt()
                )
                val collectionRef = FirebaseFirestore.getInstance().collection("UserInfo")
                val documentRef = collectionRef.document(auth.currentUser!!.uid)
                if(inputday != null){
                    val todayCollectionRef = documentRef.collection("DietInfo")
                    todayCollectionRef
                        .document(inputday!!).collection(binding.spinnerDietPart.selectedItem.toString()).get()
                        .addOnSuccessListener { querySnapshot ->
                            val documentCount = querySnapshot.size()
                            data["did"] = (documentCount+1).toString()

                            todayCollectionRef
                                .document(inputday!!).collection(binding.spinnerDietPart.selectedItem.toString()).document((documentCount+1).toString())
                                .set(data)
                                .addOnFailureListener {
                                    Log.d("TAG", it.message.toString())
                                }
                        }
                    todayCollectionRef.document(inputday!!).get().addOnSuccessListener {
                        if(it.exists()){
                            val fieldValue = it.getLong("calrorie")
                            if(fieldValue != null){
                                todayCollectionRef.document(inputday!!).update("calrorie",binding.EditeTextCalorie.text.toString()+fieldValue)
                            }
                            else{
                                todayCollectionRef.document(inputday!!).set(data2)
                            }
                        }
                        else{
                            todayCollectionRef.document(inputday!!).set(data2)
                        }
                    }
                }
                else{
                    Log.d("TAG","inputday 비동기화 오류")
                }
                Toast.makeText(this@AddDietInfoActivity,"저장완료", Toast.LENGTH_SHORT).show()
                finish()
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