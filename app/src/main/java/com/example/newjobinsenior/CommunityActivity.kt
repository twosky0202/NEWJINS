package com.example.newjobinsenior

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newjobinsenior.databinding.ActivityCommunityBinding
import com.google.firebase.firestore.Query

class CommunityActivity : BaseActivity() {
    lateinit var binding: ActivityCommunityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myCheckPermission()

        binding.mainFab.setOnClickListener {
            if (MyApplication.checkAuth()) {
                startActivity(Intent(this, AddActivity::class.java))
            } else {
                Toast.makeText(this, "인증을 진행해 주세요", Toast.LENGTH_SHORT).show()
            }
        }
        binding.boardRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.boardRecyclerView.adapter = MyBoardAdapter(this, mutableListOf())
    }

    override fun onStart() {
        super.onStart()

        if (MyApplication.checkAuth()) {
            Log.d("mobileApp", "onStart")
            MyApplication.db.collection("news")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { result ->
                    val itemList = mutableListOf<ItemBoardModel>()
                    for (document in result) {
                        val item = document.toObject(ItemBoardModel::class.java)
                        item.docId = document.id
                        itemList.add(item)
                    }
                    Log.d("mobileApp", "$itemList")
                    binding.boardRecyclerView.layoutManager = LinearLayoutManager(this)
                    val adapter = MyBoardAdapter(this, itemList) // 어댑터 인스턴스 생성
                    binding.boardRecyclerView.adapter = adapter // 어댑터 연결
                    adapter.notifyDataSetChanged() // 변경 사항 알림
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "서버 데이터 획득 실패", Toast.LENGTH_SHORT).show()
                }
        }
    }
    private fun myCheckPermission() {
        Log.d("mobileApp", "myCheckPermission")

        val requestPermissionLauncher = this.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Log.d("mobileApp", "권한 승인")
            } else {
                Log.d("mobileApp", "권한 거부")
            }
        }

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(intent)
            }
        }
    }

}