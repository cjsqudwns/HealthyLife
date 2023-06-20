package com.example.healthylife.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.healthylife.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    lateinit var binding:ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    private fun createUser(){
    //아이디, 비밀번호, 몸무게, 키, 닉네임
        val id=binding.registerID.text.toString()
        val passwd = binding.registerPassword.text.toString()
        val passwdCheck = binding.registerPasswordCheck.text.toString()
        val nickname = binding.registerNickname.text.toString()
        val height = binding.registerHeight.text.toString()
        val weight = binding.registerWeight.text.toString()
        if(passwd == passwdCheck){
           auth.createUserWithEmailAndPassword(id,passwd)
               .addOnCompleteListener {
                   if(it.isSuccessful){
                       if(auth.currentUser != null){
                           val db = FirebaseFirestore.getInstance()
                           val collectionRef = db.collection("UserInfo")
                           val documentRef = collectionRef.document(auth.currentUser!!.uid)
                           val data = hashMapOf(
                               "Nickname" to nickname,
                               "height" to height.toInt(),
                               "weight" to weight.toInt()
                           )
                           documentRef.set(data)
                               .addOnSuccessListener {
                                   // 필드 추가 성공시 이벤트.
                               }
                               .addOnFailureListener { e ->
                                   // 필드 추가 실패시 이벤트. 나중에 필요하면 쓰기.
                               }
                           val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                           startActivity(intent)
                       }
                       else{
                           Toast.makeText(this@RegisterActivity, "뭔가..잘못됨...", Toast.LENGTH_SHORT).show()
                       }
                   }
               }
        }
        else{
            Toast.makeText(this@RegisterActivity, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
        }
    }
    fun init(){
        binding.apply{
            registerBtn.setOnClickListener {
                createUser()
            }
            backBtn.setOnClickListener {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}