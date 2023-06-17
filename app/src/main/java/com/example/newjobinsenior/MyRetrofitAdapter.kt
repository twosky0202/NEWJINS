package com.example.newjobinsenior

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newjobinsenior.databinding.ItemRetrofitBinding

class MyRetrofitViewHolder(val binding: ItemRetrofitBinding): RecyclerView.ViewHolder(binding.root)

class MyRetrofitAdapter(val context: Context, val datas: MutableList<ItemRetrofitModel>?): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemCount(): Int{
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = MyRetrofitViewHolder(ItemRetrofitBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as MyRetrofitViewHolder).binding

        //add......................................
        val model = datas!![position]
        binding.itemStatus.text = model.APPLY_STATE
        binding.itemName.text = model.SUBJECT
        binding.itemTrainingPeriod.text = model.STARTDATE + " ~ " +  model.ENDDATE
        binding.itemRegisterPeriod.text = model.APPLICATIONSTARTDATE + " ~ " + model.APPLICATIONENDDATE
        binding.itemCapacity.text = model.REGISTPEOPLE
        binding.itemCost.text = model.REGISTCOST
        binding.itemDetail.text = model.VIEWDETAIL

//        Glide.with(context)
//            .load(model.item.imgurl1)
//            .into(binding.itemImage)
    }
}