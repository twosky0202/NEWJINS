package com.example.newjobinsenior

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.newjobinsenior.databinding.ActivityAddBinding
import com.google.firebase.storage.StorageReference
import java.io.File

import java.text.SimpleDateFormat
import java.util.*

class AddActivity : BaseActivity() {
    lateinit var binding : ActivityAddBinding
    lateinit var filePath : String

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 이미지 선택 후 반환 -> 전달된 Result가 OK이면 ImageView에 보이기, 업로드를 위해 파일이름 저장
        val requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode === android.app.Activity.RESULT_OK) {
                // 이미지를 ImageView에 보이기
                Glide
                    .with(applicationContext)
                    .load(it.data?.data) // 이미지 불러오기
                    .apply(RequestOptions().override(250, 200)) // 이미지 사이즈 규격화
                    .centerCrop() // 이미지가 사이즈보다 큰 경우 센터를 기준으로 자름
                    .into(binding.addImageView) // 이미지뷰에 적용
                // 이미지의 주소 저장
                val cursor = contentResolver.query(it.data?.data as Uri,
                    arrayOf<String>(MediaStore.Images.Media.DATA), null, null, null)
                cursor?.moveToFirst().let {
                    filePath = cursor?.getString(0) as String
                    Log.d("mobileApp", filePath)
                }
            }
        }
        binding.btnGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK) // Gallaey에서 이미지 선택
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*") // 갤러리에 있는 이미지를 불러오는 방법
            requestLauncher.launch(intent)
        }

        binding.btnSave.setOnClickListener {
            if(binding.addImageView.drawable !== null && binding.addEditView.text.isNotEmpty()){
                // firestore 저장 후 document id 값으로 업로드 파일 이름 지정
                saveStore()
            } else{
                Toast.makeText(this, "내용을 입력해주세요..", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }
    private fun dateToString(date:Date):String{
        val format = SimpleDateFormat("yyyy-MM-dd hh:mm")
        return format.format(date)

    }
    private fun saveStore(){
        val data = mapOf(
            "email" to MyApplication.email,
            "content" to binding.addEditView.text.toString(),
            "date" to dateToString(Date()),
        )
        MyApplication.db.collection("news")
            .add(data) // id 발급을 위해 먼저 저장
            .addOnSuccessListener {
                Log.d("mobileApp", "data firestore save ok")
                uploadImage(it.id)
            }
            .addOnFailureListener {
                Log.d("mobileApp", "data firestore save error - ${it.toString()}")
            }
    }
    private fun uploadImage(docId: String){
        //add............................
        val storage = MyApplication.storage
        val storageRef : StorageReference = storage.reference
        val imgRef : StorageReference= storageRef.child("images/${docId}.jpg")

        val file = Uri.fromFile(File(filePath))
        Log.d("mobileApp", file.toString())
        imgRef.putFile(file)
            .addOnSuccessListener {
                Toast.makeText(this, "save ok..", Toast.LENGTH_SHORT).show()
                Log.d("mobileApp", "save ok..")
                finish()
            }
            .addOnFailureListener{
                Log.d("mobileApp", "file save error", it)
            }
    }
}