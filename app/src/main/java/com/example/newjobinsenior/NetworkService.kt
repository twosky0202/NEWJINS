package com.example.newjobinsenior

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkService {
    @GET("/{KEY}/{TYPE}/{SERVICE}/{START_INDEX}/{END_INDEX}")
    fun getList(
        // query안의 내용은 사용하려는 데이터 항목명과 똑같이
        @Path("KEY") apiKey:String,
        @Path("TYPE") type:String,
        @Path("SERVICE") service:String,
        @Path("START_INDEX") start:Int,
        @Path("END_INDEX") end:Int,
        ) : Call<MyModel>
}