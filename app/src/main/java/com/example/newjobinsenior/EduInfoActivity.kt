package com.example.newjobinsenior

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newjobinsenior.databinding.ActivityEduInfoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EduInfoActivity : BaseActivity() {
    lateinit var binding: ActivityEduInfoBinding
    var datas: MutableList<String>? = null
    lateinit var adapter: MyRetrofitAdapter

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
                        adapter = MyRetrofitAdapter(this@EduInfoActivity, responseBody.row)
                        binding.retrofitRecyclerView.adapter = adapter
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
        adapter = MyRetrofitAdapter(this, mutableList)
        binding.retrofitRecyclerView.adapter = adapter

    }
}