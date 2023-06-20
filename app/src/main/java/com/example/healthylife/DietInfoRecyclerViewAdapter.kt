package com.example.healthylife

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthylife.databinding.RowDietBinding

class DietInfoRecyclerViewAdapter (val items:MutableList<DietInfoData>): RecyclerView.Adapter<DietInfoRecyclerViewAdapter.MyViewHolder>(){
    interface OnItemClickListener{
        fun OnStarClick(data: DietInfoData, position: Int)
        fun modifyData(data: DietInfoData, position: Int)
    }

    var itemClickListener:OnItemClickListener?=null
    inner class MyViewHolder(val binding: RowDietBinding): RecyclerView.ViewHolder(binding.root){
        init{
            binding.entireFrame.setOnClickListener{
                itemClickListener!!.modifyData(items[adapterPosition], adapterPosition)
            }
            binding.favorites.setOnClickListener{
                itemClickListener!!.OnStarClick(items[adapterPosition], adapterPosition)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int):MyViewHolder{
        val view = RowDietBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
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

    override fun onBindViewHolder(holder:MyViewHolder, position:Int) {
        holder.binding.time.text = items[position].day
        holder.binding.dietPart.text = items[position].dietPart
        holder.binding.startTime.text = items[position].startTime
        holder.binding.calorie.text = items[position].calorie.toString() + "cal"
        holder.binding.memoDiet.text = items[position].memoDiet
        holder.binding.carbs.text = items[position].carbs.toString() + "g"
        holder.binding.protein.text = items[position].protein.toString() + "g"
        holder.binding.fat.text = items[position].fat.toString() + "g"
    }

    override fun getItemCount(): Int {
        return items.size
    }
}