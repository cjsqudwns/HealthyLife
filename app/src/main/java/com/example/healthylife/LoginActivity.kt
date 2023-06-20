package com.example.healthylife

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.healthylife.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    lateinit var binding:ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        init()
    }

    fun init(){
        binding.apply {
            createIDBtn.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
            loginBtn.setOnClickListener {
                val email = binding.IDEditText.text?.toString()
                val password = binding.PasswordEditText.text?.toString()
                if (email != null) {
                    if (password != null) {
                        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task->
                            if(task.isSuccessful) {
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                // 로그인한 후 메인화면에서 뒤로 가기 눌렀을 때 다시 로그인 화면으로 가지 않도록 하는 코드
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            } else{
                                // Show the error message
                                Toast.makeText(this@LoginActivity, "로그인에 실패했습니다.", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    else{
                        Toast.makeText(this@LoginActivity, "로그인에 실패했습니다.", Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    Toast.makeText(this@LoginActivity, "로그인에 실패했습니다.", Toast.LENGTH_LONG).show()
                }

            }
        }
    }
}