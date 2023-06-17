package com.example.newjobinsenior

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newjobinsenior.databinding.ActivityEduInfoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EduInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityEduInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEduInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var mutableList: MutableList<ItemRetrofitModel>

        val call: Call<MyModel> = MyApplication.networkService.getList(
            "514365775168616e3339576863484f",
            "json",
            "tbViewProgram",
            1,
            1000
        )

        Log.d("mobileApp", "${call.request()}")

        call?.enqueue(object: Callback<MyModel> {
            override fun onResponse(call: Call<MyModel>, response: Response<MyModel>) {
                if(response.isSuccessful){
                    Log.d("mobileApp", "success - ${response.body()}")
                    val responseBody = response.body()?.tbViewProgram
                    if (responseBody != null) {
                        responseBody.row?.sortByDescending { it.IDX }
                        binding.retrofitRecyclerView.layoutManager = LinearLayoutManager(this@EduInfoActivity)
                        binding.retrofitRecyclerView.adapter = MyRetrofitAdapter(this@EduInfoActivity, responseBody.row)
                    } else {
                        Log.d("mobileApp", "response body is null")
                    }
                } else {
                    Log.d("mobileApp", "response unsuccessful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MyModel>, t: Throwable) {
                Log.d("mobileApp", "failure - ${t.toString()}")
            }
        })

        mutableList = mutableListOf<ItemRetrofitModel>()
        binding.retrofitRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.retrofitRecyclerView.adapter = MyRetrofitAdapter(this, mutableList)

    }


}