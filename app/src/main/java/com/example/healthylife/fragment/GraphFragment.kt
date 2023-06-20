package com.example.healthylife.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healthylife.data.ExerciseTimeData
import com.example.healthylife.databinding.FragmentGraphBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class GraphFragment : Fragment() {
    lateinit var lineChart: LineChart
    lateinit var auth: FirebaseAuth
    var binding: FragmentGraphBinding? = null

    var databaseList : MutableList<ExerciseTimeData> = mutableListOf()
    //데이터리스트를 좀 바꿔주면 되겠다.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGraphBinding.inflate(layoutInflater, container, false)
        lineChart = binding!!.chart
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        readDataFromFirebase(){
            readDataFromArray2()
        }
        //readDataFromArray2()
    }

    fun readDataFromArray2() {
        val entries: MutableList<Entry> = mutableListOf()

        for (i in databaseList.indices) {
            entries.add(Entry(i.toFloat(), databaseList[i].minute.toFloat())) // entries에 값 집어넣기
        }

        val lineDataSet = LineDataSet(entries, "운동 시간 (단위: 분)")
        lineDataSet.apply {
            lineWidth = 3F  // 그래프 선 굵기
            circleRadius = 8F  // 동그라미 크기
            color = Color.BLACK
            setCircleColor(Color.BLUE)
            setDrawCircles(true)    // 그래프의 해당 위치에 값 표시하는 동그라미
            setDrawValues(true)    // true하면 동그라미 주변에 값 표시, false면 표시 안함
            valueFormatter = DefaultValueFormatter(0)   // 소수점 자릿수 설정
            valueTextSize = 12F
        }

        val descript = Description()
        descript.text = "날짜"

        // 차트 전체 설정
        lineChart.apply {
            axisRight.isEnabled = false // y축 사용여부
            axisLeft.isEnabled = false
            legend.isEnabled = true    // legend 사용여부
            setDrawGridBackground(true)
            description = descript  // 주석
        }

        // x축 설정
        val xAxis = lineChart.xAxis
        xAxis.apply {
            setDrawGridLines(false)
            setDrawAxisLine(true)
            setDrawLabels(true)
            position = XAxis.XAxisPosition.BOTTOM
            valueFormatter = XAxisCustomFormatter(changeDateText(databaseList))
            textColor = Color.BLACK
            textSize = 12F
            enableGridDashedLine(7F, 24F, 0F)
        }

        lineChart.apply {
            data = LineData(lineDataSet)
            notifyDataSetChanged()  // 데이터 갱신ㅅ
            invalidate()    // view 갱신
        }
    }
    override fun onResume() {
        super.onResume()
        // 원하는 작업 수행
        readDataFromFirebase(){
            readDataFromArray2()
        }

    }
    fun changeDateText(dataList: List<ExerciseTimeData>): List<String> {
        val dataTextList = ArrayList<String>()
        for (i in dataList.indices) {
            dataTextList.add(dataList[i].day)
        }
        return dataTextList
    }

    class XAxisCustomFormatter(val xAxisData: List<String>) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return xAxisData[(value).toInt()]
        }
    }

    fun readDataFromFirebase(callback: () -> Unit) {
        // 1. 오늘의 날짜를 포함해서, 이전 7일간의 날짜를 키값으로, 운동시간을 밸류로 가지는 HashMap 생성
        val exerciseTimeMap = hashMapOf<String, Int>()

        val currentDate = LocalDate.now()
        for (i in 0 until 7) {
            val date = currentDate.minusDays(i.toLong())
            val formattedDate = date.format(DateTimeFormatter.BASIC_ISO_DATE)
            exerciseTimeMap[formattedDate] = 0
        }

        // 2. FirebaseFirestore에서 해당 날짜의 운동시간을 HashMap에 업데이트
        val firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection("UserInfo").document(auth.currentUser!!.uid).collection("ExerciseInfo")

        collectionRef.get().addOnSuccessListener { querySnapshot ->
            for (documentSnapshot in querySnapshot.documents) {
                val documentId = documentSnapshot.id
                val exerciseTime = documentSnapshot.getLong("ExerciseTime")

                exerciseTimeMap[documentId] = exerciseTime?.toInt() ?: 0
            }

            // 4. ExerciseTimeData(Date, ExerciseTime)의 형태로 이루어진 Data 클래스를 7개 포함하는 List 초기화
            val dataList: MutableList<ExerciseTimeData> = mutableListOf()

            for (entry in exerciseTimeMap) {
                val date = entry.key
                val exerciseTime = entry.value
                val month = date.substring(4, 6).toInt()
                val day = date.substring(6, 8).toInt()
                val formattedDate = "${month}월 ${day}일"

                dataList.add(ExerciseTimeData(formattedDate, exerciseTime,date.toInt()))
            }
            dataList.sortBy { it.dateData }
            databaseList = dataList
            callback()
        }.addOnFailureListener { exception ->
            // 오류 처리
            Log.e("TAG", "Error reading exercise data from Firestore: $exception")
        }
    }
}