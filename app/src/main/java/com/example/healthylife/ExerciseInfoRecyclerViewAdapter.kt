package com.example.healthylife

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthylife.data.ExerciseInfoData
import com.example.healthylife.databinding.RowDietBinding
import com.example.healthylife.databinding.RowExerciseBinding

class ExerciseInfoRecyclerViewAdapter (val items:ArrayList<ExerciseInfoData>): RecyclerView.Adapter<ExerciseInfoRecyclerViewAdapter.MyViewHolder>(){
    inner class MyViewHolder(val binding: RowExerciseBinding): RecyclerView.ViewHolder(binding.root){
        init{
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int):MyViewHolder{
        val view = RowExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder:MyViewHolder, position:Int) {
        holder.binding.time.text = items[position].day
        holder.binding.exerciseTime.text = items[position].minute.toString()
        holder.binding.startTime.text = items[position].startTime
        holder.binding.exerciseArea.text = items[position].exercise_area
        holder.binding.memoExercise.text = items[position].memo
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
