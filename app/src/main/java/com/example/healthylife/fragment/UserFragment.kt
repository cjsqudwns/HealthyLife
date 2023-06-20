package com.example.healthylife

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
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
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.math.BigInteger.TWO
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class UserFragment : Fragment() {
    lateinit var lineChart: LineChart
    var binding:FragmentUserBinding ?= null
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var auth: FirebaseAuth
    var databaseList : MutableList<ExerciseTimeData> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = FirebaseAuth.getInstance()
        binding = FragmentUserBinding.inflate(layoutInflater, container, false)
        lineChart = binding!!.chart
        init()
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        readDataFromFirebase(){
            readDataFromArray2()
        }
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

    fun readDataFromArray2() {
        val entries: MutableList<Entry> = mutableListOf()

        for (i in databaseList.indices) {
            entries.add(Entry(i.toFloat(), databaseList[i].minute.toFloat())) // entries에 값 집어넣기
        }

        val lineDataSet = LineDataSet(entries, "운동 시간 (단위: 분)")
        lineDataSet.apply {
            lineWidth = 3F  // 그래프 선 굵기
            circleRadius = 8F  // 동그라미 크기
            color = Color.BLACK
            setCircleColor(Color.BLUE)
            setDrawCircles(true)    // 그래프의 해당 위치에 값 표시하는 동그라미
            setDrawValues(true)    // true하면 동그라미 주변에 값 표시, false면 표시 안함
            valueFormatter = DefaultValueFormatter(0)   // 소수점 자릿수 설정
            valueTextSize = 12F
            valueTextColor = Color.MAGENTA
        }

        val descript = Description()
        descript.text = "날짜"

        // 차트 전체 설정
        lineChart.apply {
            axisRight.isEnabled = false // y축 사용여부
            axisLeft.isEnabled = false
            legend.isEnabled = true    // legend 사용여부
            setDrawGridBackground(true)
            description = descript  // 주석
        }

        // x축 설정
        val xAxis = lineChart.xAxis
        xAxis.apply {
            setDrawGridLines(false)
            setDrawAxisLine(true)
            setDrawLabels(true)
            position = XAxis.XAxisPosition.BOTTOM
            valueFormatter = XAxisCustomFormatter(changeDateText(databaseList))
            textColor = Color.WHITE
            textSize = 12F
            enableGridDashedLine(7F, 24F, 0F)
        }

        lineChart.apply {
            data = LineData(lineDataSet)
            notifyDataSetChanged()  // 데이터 갱신ㅅ
            invalidate()    // view 갱신
        }
    }
    override fun onResume() {
        super.onResume()
        // 원하는 작업 수행
        readDataFromFirebase(){
            readDataFromArray2()
        }

    }
    fun changeDateText(dataList: List<ExerciseTimeData>): List<String> {
        val dataTextList = ArrayList<String>()
        for (i in dataList.indices) {
            dataTextList.add(dataList[i].day)
        }
        return dataTextList
    }

    class XAxisCustomFormatter(val xAxisData: List<String>) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return xAxisData[(value).toInt()]
        }
    }

    fun readDataFromFirebase(callback: () -> Unit) {
        // 1. 오늘의 날짜를 포함해서, 이전 7일간의 날짜를 키값으로, 운동시간을 밸류로 가지는 HashMap 생성
        val exerciseTimeMap = hashMapOf<String, Int>()

        val currentDate = LocalDate.now()
        for (i in 0 until 7) {
            val date = currentDate.minusDays(i.toLong())
            val formattedDate = date.format(DateTimeFormatter.BASIC_ISO_DATE)
            exerciseTimeMap[formattedDate] = 0
        }

        // 2. FirebaseFirestore에서 해당 날짜의 운동시간을 HashMap에 업데이트
        val firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection("UserInfo").document(auth.currentUser!!.uid).collection("ExerciseInfo")

        collectionRef.get().addOnSuccessListener { querySnapshot ->
            for (documentSnapshot in querySnapshot.documents) {
                val documentId = documentSnapshot.id
                val exerciseTime = documentSnapshot.getLong("ExerciseTime")

                exerciseTimeMap[documentId] = exerciseTime?.toInt() ?: 0
            }

            // 4. ExerciseTimeData(Date, ExerciseTime)의 형태로 이루어진 Data 클래스를 7개 포함하는 List 초기화
            val dataList: MutableList<ExerciseTimeData> = mutableListOf()

            for (entry in exerciseTimeMap) {
                val date = entry.key
                val exerciseTime = entry.value
                val month = date.substring(4, 6).toInt()
                val day = date.substring(6, 8).toInt()
                val formattedDate = "${month}월${day}일"

                dataList.add(ExerciseTimeData(formattedDate, exerciseTime,date.toInt()))
            }
            dataList.sortBy { it.dateData }
            databaseList = dataList
            callback()
        }.addOnFailureListener { exception ->
            // 오류 처리
            Log.e("TAG", "Error reading exercise data from Firestore: $exception")
        }
    }
}