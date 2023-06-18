package com.example.newjobinsenior

import android.content.Intent
import android.os.Bundle
import com.example.newjobinsenior.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.eduInfo.setOnClickListener {
            val intent = Intent(this, EduInfoActivity::class.java)
            startActivity(intent)
        }

        binding.community.setOnClickListener {
            val intent = Intent(this, CommunityActivity::class.java)
            startActivity(intent)
        }
    }

}