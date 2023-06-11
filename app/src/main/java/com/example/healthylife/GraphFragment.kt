package com.example.healthylife

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healthylife.databinding.FragmentGraphBinding
import com.example.healthylife.databinding.FragmentHomeBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class GraphFragment : Fragment() {
    lateinit var lineChart: LineChart
    var binding: FragmentGraphBinding? = null

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
        readDataFromFirebase()
    }

    private fun readDataFromFirebase() {
        /*
        FirebaseDatabase.getInstance().reference.child("graph_data")
            .addListenerForSingleValueEvent(object : HomeItemRecyclerViewAdapter.ValueEventListener {
                fun onDataChange(dataSnapshot: DataSnapshot) {
                    val entries = mutableListOf<Entry>()

                    for (snapshot in dataSnapshot.children) {
                        val x = snapshot.child("x").getValue(Float::class.java) ?: 0f   // 날짜 or 부위 파트
                        val y = snapshot.child("y").getValue(Float::class.java) ?: 0f   // 시간 파트
                        entries.add(Entry(x, y))
                    }

                    val dataSet = LineDataSet(entries, "Graph Data")
                    val lineData = LineData(dataSet)
                    lineChart.data = lineData
                    lineChart.invalidate()
                }

                fun onCancelled(databaseError: DatabaseError) {
                    Log.e("GraphFragment", "Failed to read graph data.", databaseError.toException())
                }
            })

         */
    }
}