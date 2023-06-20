package com.example.healthylife.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import androidx.core.view.get
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthylife.DietInfoRecyclerViewAdapter
import com.example.healthylife.data.ExerciseInfoData
import com.example.healthylife.ExerciseInfoRecyclerViewAdapter
import com.example.healthylife.R
import com.example.healthylife.data.DietInfoData
import com.example.healthylife.databinding.FragmentCalenderBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class CalenderFragment : Fragment() {
    var binding:FragmentCalenderBinding ?= null
    lateinit var adapterExercise: ExerciseInfoRecyclerViewAdapter
    lateinit var adapterDiet: DietInfoRecyclerViewAdapter
    var arr = arrayListOf<ExerciseInfoData>(ExerciseInfoData("2023-06-20", "13:00", "전신", 45, "ㅁㄴㅇㄹㅁㅇㄴㄹ", false))
    var inputday:String? = null
    var calenderMonth:Int = 0
    var calenderDay:Int = 1
    var calenderYear:Int = 2023
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalenderBinding.inflate(layoutInflater, container, false)
        initRecyclerView()
        val selectedDateMillis = binding!!.calenderView.date
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.timeInMillis = selectedDateMillis
        setInputDay(binding!!.calenderView, selectedCalendar.get(Calendar.YEAR), selectedCalendar.get(Calendar.MONTH), selectedCalendar.get(Calendar.DAY_OF_MONTH))
        binding!!.calenderView.setOnDateChangeListener { calendarView, year, month, day ->
            setInputDay(calendarView, year, month, day)
        }
        return binding!!.root
    }
    fun setInputDay(calendarView:CalendarView, year:Int, month:Int, day:Int){
        calenderMonth = month+1
        calenderDay = day
        calenderYear = year
        inputday = year.toString() + String.format("%02d", month+1)+String.format("%02d", day)
        Log.d("TAG", inputday.toString())
    }
    fun initRecyclerView(){
        //운동 정보 recyclerView
        binding!!.recyclerViewExerciseInfo.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        binding!!.recyclerViewExerciseInfo.addItemDecoration(DividerItemDecoration(this.requireContext(), LinearLayoutManager.VERTICAL))
        adapterExercise = ExerciseInfoRecyclerViewAdapter(arr)
        adapterExercise.itemClickListener = object : ExerciseInfoRecyclerViewAdapter.OnItemClickListener {
            override fun OnItemClick(data: ExerciseInfoData, position: Int) {
                adapterExercise.favoritesSelected(position)
            }
        }
        binding!!.recyclerViewExerciseInfo.adapter = adapterExercise
        //식단 정보 recyclerView
        binding!!.recyclerViewDietInfo.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        binding!!.recyclerViewDietInfo.addItemDecoration(DividerItemDecoration(this.requireContext(), LinearLayoutManager.VERTICAL))
        adapterDiet = DietInfoRecyclerViewAdapter(arrayListOf())
//        adapterDiet.itemClickListener = object : DietInfoRecyclerViewAdapter.OnItemClickListener {
//            override fun OnItemClick(data: DietInfoData, position: Int) {
//                adapterDiet.detailOnClick(position)
//            }
//        }
        binding!!.recyclerViewDietInfo.adapter = adapterDiet
        //swipe시 해당 데이터 지우기
        val simpleCallback = object: ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                //adapterExercise.moveItem(viewHolder.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //adapterExercise.removeItem(viewHolder.adapterPosition)
                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_delete, null)
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                val dialog = mBuilder.show()
                mDialogView.findViewById<Button>(R.id.OKBtn).setOnClickListener {
                    // 해당 데이터 삭제 진행
                    dialog.dismiss()
                }
                mDialogView.findViewById<Button>(R.id.cancleBtn).setOnClickListener {
                    dialog.dismiss()
                    adapterExercise.notifyDataSetChanged()
                }
            }

        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding!!.recyclerViewExerciseInfo)


    }
}