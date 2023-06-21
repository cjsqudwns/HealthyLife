package com.example.healthylife

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthylife.databinding.RowExerciseBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.Ref

class ExerciseInfoRecyclerViewAdapter (val items:MutableList<ExerciseInfoData>): RecyclerView.Adapter<ExerciseInfoRecyclerViewAdapter.ViewHolder>(){
    interface OnItemClickListener{
        fun OnStarClick(
            fdata: ExerciseInfoData, position: Int)
        fun modifyData(data: ExerciseInfoData, position: Int)
    }
    var itemClickListener: OnItemClickListener?=null
    inner class ViewHolder(val binding: RowExerciseBinding): RecyclerView.ViewHolder(binding.root){
        init{
            binding.favorites.setOnClickListener{
                itemClickListener!!.OnStarClick(items[adapterPosition], adapterPosition)
            }
        }
    }
    fun favoritesSelected(pos:Int,checked:Boolean){
        items[pos].check = checked
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

    @SuppressLint("SuspiciousIndentation")
    fun deleteRecyclerView(adapterPosition: Int) {
        val auth:FirebaseAuth = FirebaseAuth.getInstance()
        val remData = items[adapterPosition]
        val collectionRef = FirebaseFirestore.getInstance().collection("UserInfo")
            .document(auth.currentUser!!.uid)
            .collection("ExerciseInfo")
            .document(remData.day)
            .collection(remData.exercise_area)
            .document(remData.did)
        collectionRef.delete()
            .addOnSuccessListener {
                // 삭제 성공
                // 추가 작업 수행
                val Ref = FirebaseFirestore.getInstance().collection("UserInfo")
                    .document(auth.currentUser!!.uid)
                    .collection("ExerciseInfo")
                    .document(remData.day)
                    Ref.get().addOnSuccessListener {
                        val exerciseTime = it.getLong("ExerciseTime")
                        if (exerciseTime != null) {
                            val newExerciseTime = exerciseTime - minOf(remData.minute, 60)
                            val updateData = HashMap<String, Any>()
                            updateData["ExerciseTime"] = newExerciseTime
                            Ref.update(updateData)
                        }
                    }
            }
            .addOnFailureListener { e ->
                // 삭제 실패
                // 예외 처리를 수행하거나 오류 메시지를 표시할 수 있습니다.
            }
    }
}
