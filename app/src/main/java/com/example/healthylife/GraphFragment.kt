package com.example.healthylife

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healthylife.databinding.FragmentGraphBinding
import com.example.healthylife.databinding.FragmentHomeBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter


class GraphFragment : Fragment() {
    lateinit var lineChart: LineChart
    var binding: FragmentGraphBinding? = null
    // 임시로 넣는 데이터
    val dataList: List<ExerciseTimeData> = listOf(
        ExerciseTimeData("월요일", 3),
        ExerciseTimeData("화요일", 2),
        ExerciseTimeData("수요일", 4),
        ExerciseTimeData("목요일", 1),
        ExerciseTimeData("금요일", 3),
        ExerciseTimeData("토요일", 5),
        ExerciseTimeData("일요일", 3),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGraphBinding.inflate(layoutInflater, container, false)
        lineChart = binding!!.chart
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        readDataFromArray2()
        // readDataFromFirebase()
    }

    private fun readDataFromArray2() {
        val entries: MutableList<Entry> = mutableListOf()

        for (i in dataList.indices) {
            entries.add(Entry(i.toFloat(), dataList[i].hour.toFloat())) // entries에 값 집어넣기
        }

        val lineDataSet = LineDataSet(entries, "운동 시간 (단위: 시간)")
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
        descript.text = "요일"

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
            valueFormatter = XAxisCustomFormatter(changeDateText(dataList))
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

    private fun readDataFromFirebase() {
        /*
        FirebaseDatabase.getInstance().reference.child("graph_data")
            .addListenerForSingleValueEvent(object : HomeItemRecyclerViewAdapter.ValueEventListener {
                fun onDataChange(dataSnapshot: DataSnapshot) {
                    val entries = mutableListOf<Entry>()

                    for (snapshot in dataSnapshot.children) {
                        val x = snapshot.child("x").getValue(Float::class.java) ?: 0f   // 요일
                        val y = snapshot.child("y").getValue(Float::class.java) ?: 0f   // 시간
                        entries.add(Entry(x, y))
                    }

                    val lineDataSet = LineDataSet(entries, "운동 시간 (단위: 시간)")
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
                    descript.text = "요일"

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
                        // valueFormatter = XAxisCustomFormatter(changeDateText(dataList)) // 해당 부분은 Firebase에 맞게 수정
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

                fun onCancelled(databaseError: DatabaseError) {
                    Log.e("GraphFragment", "Failed to read graph data.", databaseError.toException())
                }
            })


         */
    }
}