package com.example.healthylife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.healthylife.databinding.ActivityModifyDietInfoBinding
import com.example.healthylife.databinding.ActivityModifyExerciseInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ModifyExerciseInfoActivity : AppCompatActivity() {
    lateinit var binding : ActivityModifyExerciseInfoBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityModifyExerciseInfoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Datainit()
    }
    private fun Datainit(){
        if (intent.hasExtra("date")) {
            val date_key = intent.getStringExtra("date")
        } else {
            Toast.makeText(this, "전달된 key가 없습니다", Toast.LENGTH_SHORT).show()
        }


        auth = FirebaseAuth.getInstance()
        val collectionRef = FirebaseFirestore.getInstance().collection("UserInfo")
        val documentRef = collectionRef.document(auth.currentUser!!.uid)
    }
}