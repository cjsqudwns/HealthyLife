package com.example.healthylife

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthylife.databinding.RecyclerRowBinding
import kotlinx.coroutines.NonDisposableHandle.parent

class ExerciseInfoRecyclerViewAdapter (val items:ArrayList<ExerciseInfoData>): RecyclerView.Adapter<ExerciseInfoRecyclerViewAdapter.MyViewHolder>(){
    inner class MyViewHolder(val binding: RecyclerRowBinding): RecyclerView.ViewHolder(binding.root){
        init{
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int):MyViewHolder{
        val view = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder:MyViewHolder, position:Int) {
        if(itemCount>=1){
            holder.binding.noData.visibility = View.GONE
            holder.binding.time.text = items[position].day
            holder.binding.exerciseTime.text = items[position].minute.toString()
            holder.binding.startTime.text = items[position].startTime
            holder.binding.exerciseArea.text = items[position].exercise_area
            holder.binding.memo.text = items[position].memo
        }else{
            holder.binding.noData.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
