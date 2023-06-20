package com.example.healthylife

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.healthylife.databinding.ActivityAddDietInfoBinding
import com.example.healthylife.databinding.ActivityModifyDietInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ModifyDietInfoActivity : AppCompatActivity() {
    lateinit var binding : ActivityModifyDietInfoBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityModifyDietInfoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Datainit()
    }
    private fun Datainit(){
        val intent = intent
        val data = intent.getSerializableExtra("modify_data") as ExerciseInfoData
        //initSpinner(data.startTime, data.exercise_area)
        Log.i("ds",data.startTime)
        //binding.memo.setText(data.memo)


        auth = FirebaseAuth.getInstance()
        val collectionRef = FirebaseFirestore.getInstance().collection("UserInfo")
        val documentRef = collectionRef.document(auth.currentUser!!.uid)
    }

}