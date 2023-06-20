package com.example.healthylife

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthylife.databinding.RowExerciseBinding


class ExerciseInfoRecyclerViewAdapter (val items:MutableList<ExerciseInfoData>): RecyclerView.Adapter<ExerciseInfoRecyclerViewAdapter.ViewHolder>(){
    interface OnItemClickListener{
        fun OnStarClick(data: ExerciseInfoData, position: Int)
    }
    var itemClickListener: OnItemClickListener?=null
    inner class ViewHolder(val binding: RowExerciseBinding): RecyclerView.ViewHolder(binding.root){
        init{
            binding.favorites.setOnClickListener{
                itemClickListener!!.OnStarClick(items[adapterPosition], adapterPosition)
            }
        }
    }
    fun favoritesSelected(pos:Int){
        if(items[pos].check) items[pos].check = false
        else items[pos].check = true
        notifyItemChanged(pos)
    }

    fun deleteData(pos:Int){
        items.removeAt(pos)
        notifyItemRemoved(pos)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int): ViewHolder {
        val view = RowExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder:ViewHolder, position:Int) {
        holder.binding!!.apply{
            time.text = items[position].day
            exerciseTime.text = items[position].minute.toString() + "분"
            startTime.text = items[position].startTime
            exerciseArea.text = items[position].exercise_area
            memoExercise.text = items[position].memo
            if(items[position].check)
                favorites.setImageResource(R.drawable.baseline_star)
            else
                favorites.setImageResource(R.drawable.baseline_star_border)
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }
}
