package com.example.healthylife

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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthylife.databinding.FragmentCalenderBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar
import java.util.Date

class CalenderFragment : Fragment() {
    var binding:FragmentCalenderBinding ?= null
    lateinit var adapterExercise: ExerciseInfoRecyclerViewAdapter
    lateinit var adapterDiet: DietInfoRecyclerViewAdapter
    var exerciseInfoDataList: MutableList<ExerciseInfoData> = mutableListOf()
    var dietInfoDataList: MutableList<DietInfoData> = mutableListOf()
    lateinit var auth: FirebaseAuth
    var inputday:String? = null
    var calenderMonth:Int = 0
    var calenderDay:Int = 1
    var calenderYear:Int = 2023
    var arrDiet = arrayListOf<DietInfoData>(
        DietInfoData("2023-06-20", "아점", "13:00", 1305,
            "ㅁㄴㅇㄹㅁㅇㄴㄹ", 10, 30, 50, true),
        DietInfoData("2023-06-21", "저녁", "17:00", 1200,
            "ㅁㄴㅇㄹㅇㄴㄹㅈㄷㄹㅈ", 20, 70, 10, true))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalenderBinding.inflate(layoutInflater, container, false)
        exerciseInfoDataList.add(ExerciseInfoData("2023-06-20", "13:00", "전신", 45, "ㅁㄴㅇㄹㅁㅇㄴㄹ", true))
        exerciseInfoDataList.add(ExerciseInfoData("2023-06-21", "15:00", "어깨", 60, "ㄻㄴㅇㄹㄴㅇㄹㄴㅇㄹㄴㅇㄹㄴㅇㄹ", true))
        init()
        initRecyclerView()
        return binding!!.root
    }
    fun init(){
        val selectedDateMillis = binding!!.calenderView.date
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.timeInMillis = selectedDateMillis
        setInputDay(binding!!.calenderView, selectedCalendar.get(Calendar.YEAR), selectedCalendar.get(
            Calendar.MONTH), selectedCalendar.get(Calendar.DAY_OF_MONTH))
        auth = FirebaseAuth.getInstance()
        binding!!.calenderView.setOnDateChangeListener { calendarView, year, month, day ->
            setInputDay(calendarView, year, month, day)
        }
    }
    fun setInputDay(calendarView: CalendarView, year:Int, month:Int, day:Int){
        calenderMonth = month+1
        calenderDay = day
        calenderYear = year
        inputday = year.toString() + String.format("%02d", month+1)+String.format("%02d", day)
        Log.d("TAG", inputday.toString())
    }
    fun initRecyclerView(){
        // 운동 정보 recyclerView
        binding!!.recyclerViewExerciseInfo.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        binding!!.recyclerViewExerciseInfo.addItemDecoration(DividerItemDecoration(this.requireContext(), LinearLayoutManager.VERTICAL))
        adapterExercise = ExerciseInfoRecyclerViewAdapter(exerciseInfoDataList)
        //운동 정보 click시 작동
        adapterExercise.itemClickListener = object : ExerciseInfoRecyclerViewAdapter.OnItemClickListener {
            // 즐겨찾기 (별)
            override fun OnStarClick(data: ExerciseInfoData, position: Int) {
                // 해당 데이터 check value 바꾸기(false -> true, true -> false) 데이터베이스
                adapterExercise.favoritesSelected(position)
            }
            // 데이터 수정
            override fun modifyData(data: ExerciseInfoData, position: Int) {
                // 수정하러 modifyExerciseInfoActivity로 intent
            }
        }
        binding!!.recyclerViewExerciseInfo.adapter = adapterExercise
        // swipe 시 해당 데이터 delete
        val simpleCallback = object: ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_delete, null)
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                val dialog = mBuilder.show()
                mDialogView.findViewById<Button>(R.id.OKBtn).setOnClickListener {
                    // 해당 데이터 delete 데이터베이스
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
        // 식단 정보 recyclerView
        binding!!.recyclerViewDietInfo.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        binding!!.recyclerViewDietInfo.addItemDecoration(DividerItemDecoration(this.requireContext(), LinearLayoutManager.VERTICAL))
        adapterDiet = DietInfoRecyclerViewAdapter(arrDiet)
//        adapterDiet.itemClickListener = object : DietInfoRecyclerViewAdapter.OnItemClickListener {
//            override fun OnItemClick(data: DietInfoData, position: Int) {
//                adapterDiet.detailOnClick(position)
//            }
//        }
        binding!!.recyclerViewDietInfo.adapter = adapterDiet
    }
}