package com.example.healthylife

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthylife.databinding.FragmentHomeBinding
import com.example.healthylife.databinding.RowBinding

class HomeItemRecyclerViewAdapter(val items:List<String>):RecyclerView.Adapter<HomeItemRecyclerViewAdapter.MyViewHolder>() {

    interface ValueEventListener {
        // fun onDataChange(dataSnapshot: DataSnapshot)
        // fun onCancelled(databaseError: DatabaseError)
    }

    inner class MyViewHolder(val binding: RowBinding) : RecyclerView.ViewHolder(binding.root){
        init{

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int):MyViewHolder{
        val view = RowBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position:Int){
        val item = items[position]
    }

    override fun getItemCount(): Int {
        return items.size
    }

}