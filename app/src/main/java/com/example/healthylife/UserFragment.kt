package com.example.healthylife

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.healthylife.databinding.FragmentUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.math.BigInteger.TWO

class UserFragment : Fragment() {

    var binding:FragmentUserBinding ?= null
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = FirebaseAuth.getInstance()
        binding = FragmentUserBinding.inflate(layoutInflater, container, false)
        init()
        return binding!!.root
    }

    fun init(){
        val collectionRef =FirebaseFirestore.getInstance().collection("UserInfo")
        val documentRef = collectionRef.document(auth.currentUser!!.uid)
        var nicknameGet:String?
        documentRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    nicknameGet = documentSnapshot.getString("Nickname")
                    Log.d("TAG", "Variable value: $nicknameGet")
                    binding?.userID?.text = nicknameGet
                    //이미지도 이곳에서 바꿔줘야함. 데이터베이스의 비동기성 떄문.
                } else {
                    Log.d("TAG", "존재하지않는필드")
                }   
            }
            .addOnFailureListener { e ->
                // 문서 가져오기 실패
                Log.d("TAG", "존재하지않는문서")
            }
        binding?.Logout?.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
        binding?.setNickname?.setOnClickListener{
            //닉네임을 입력할 수 있는 창같은 걸 띄워주고, 확인 누르면 변경되게..
            val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_nickname, null)
            val mBuilder = AlertDialog.Builder(requireContext())
                .setView(mDialogView)
            mBuilder.show()
            mDialogView.findViewById<Button>(R.id.OKBtn).setOnClickListener {
                val nickname = mDialogView.findViewById<EditText>(R.id.NicknameEditText).text.toString()
                FirebaseFirestore.getInstance().collection("UserInfo").document(auth.currentUser!!.uid)
                    .update("Nickname", nickname)
                    .addOnSuccessListener {
                        binding?.userID?.text = nickname
                    }
            }
        }
        binding?.setImage?.setOnClickListener {
            val intent = Intent().also { intent ->
                intent.type = "image/"
                intent.action = Intent.ACTION_GET_CONTENT
            }
            launcher.launch(intent)
        }
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val intent = checkNotNull(result.data)
                val imageUri = intent.data
                binding?.imageView?.setImageURI(imageUri)
                uploadProfilePhoto(imageUri!!)
            }
        }
        val storageRef = FirebaseStorage.getInstance().getReference("${auth.currentUser!!.uid}.jpg")
        storageRef.downloadUrl.addOnSuccessListener { uri ->
            // 이미지 다운로드 URL을 성공적으로 가져왔을 때
            val imageUrl = uri.toString()
            // imageUrl을 사용하여 ImageView에 이미지 설정
            Glide.with(this)
                .load(imageUrl)
                .into(binding!!.imageView)
        }
    }

    fun uploadProfilePhoto(imageFile: Uri) {
        // Firebase Storage 경로 설정
        val storagePath = "${auth.currentUser!!.uid}.jpg"

        // Firebase Storage에 이미지 업로드
        val storageRef = FirebaseStorage.getInstance().getReference(storagePath)
        storageRef.putFile(imageFile)
            .addOnSuccessListener { taskSnapshot ->
                // 업로드 성공 처리
                taskSnapshot.storage.downloadUrl.addOnSuccessListener{
                }

            }
    }
}