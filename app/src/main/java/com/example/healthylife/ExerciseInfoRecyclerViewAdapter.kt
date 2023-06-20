package com.example.healthylife

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthylife.databinding.RowExerciseBinding

class ExerciseInfoRecyclerViewAdapter (val items:MutableList<ExerciseInfoData>): RecyclerView.Adapter<ExerciseInfoRecyclerViewAdapter.MyViewHolder>(){
    interface OnItemClickListener{
        fun OnItemClick(data: ExerciseInfoData, position: Int)
    }
    var itemClickListener: ExerciseInfoRecyclerViewAdapter.OnItemClickListener?=null
    inner class MyViewHolder(val binding: RowExerciseBinding): RecyclerView.ViewHolder(binding.root){
        init{
            binding.favorites.setOnClickListener{
                itemClickListener?.OnItemClick(items[adapterPosition], adapterPosition)
            }
        }
    }
    fun favoritesSelected(pos:Int){
        if(items[pos].check) items[pos].check = false
        else items[pos].check = true
        notifyItemChanged(pos)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int):MyViewHolder{
        val view = RowExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder:MyViewHolder, position:Int) {
        holder.binding.time.text = items[position].day
        holder.binding.exerciseTime.text = items[position].minute.toString() + "분"
        holder.binding.startTime.text = items[position].startTime
        holder.binding.exerciseArea.text = items[position].exercise_area
        holder.binding.memoExercise.text = items[position].memo
        if(items[position].check)
            holder.binding.favorites.setImageResource(R.drawable.baseline_star)
        else
            holder.binding.favorites.setImageResource(R.drawable.baseline_star_border)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
