package com.example.newjobinsenior

import CurrentSourceExtractor
import android.app.Activity
import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newjobinsenior.databinding.ItemRetrofitBinding
import java.io.BufferedReader
import java.io.File
import java.io.OutputStreamWriter

class MyRetrofitViewHolder(val binding: ItemRetrofitBinding): RecyclerView.ViewHolder(binding.root)

class MyRetrofitAdapter(val context: Context, val datas: MutableList<ItemRetrofitModel>?): RecyclerView.Adapter<MyRetrofitViewHolder>(){
//    val selectedItems: MutableSet<ItemRetrofitModel> = mutableSetOf()

    override fun getItemCount(): Int{
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRetrofitViewHolder {
        val binding = ItemRetrofitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyRetrofitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyRetrofitViewHolder, position: Int) {
        val binding = holder.binding

        val model = datas!![position]
        binding.itemStatus.text = model.APPLY_STATE
        binding.itemName.text = model.SUBJECT
        binding.itemTrainingPeriod.text = "교육 기간 | " + model.STARTDATE + " ~ " +  model.ENDDATE
        binding.itemRegisterPeriod.text = "신청 기간 | " + model.APPLICATIONSTARTDATE + " ~ " + model.APPLICATIONENDDATE
        binding.itemCapacity.text = "수강 정원 | " + model.REGISTPEOPLE
        binding.itemCost.text = "교육 비용 | " + model.REGISTCOST
//        binding.itemDetail.text = "상세 보기 | " + model.VIEWDETAIL

        val linkText = "<a href='${model.VIEWDETAIL}'>${model.VIEWDETAIL}</a>"
        binding.itemDetail.text = HtmlCompat.fromHtml("상세 보기 | $linkText", HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.itemDetail.movementMethod = LinkMovementMethod.getInstance()

        val currentSourceExtractor = CurrentSourceExtractor(model.VIEWDETAIL) { currentSource ->
            if (currentSource != null) {
                val imgUrl = if (currentSource.startsWith("http")) {
                    currentSource.replace("http://", "https://")
                } else {
                    "https://www.goldenjob.or.kr$currentSource"
                }
                Log.d("mobileApp", "${imgUrl}")
                (context as Activity).runOnUiThread {
                    Glide.with(context)
                        .load(imgUrl)
                        .into(binding.itemImage)
                }
            } else {
                // 소스 코드 추출에 실패한 경우에 대한 처리
            }
        }
        currentSourceExtractor.extractCurrentSource()

        binding.menuSave.setOnClickListener {
            val file: File = File(context.filesDir, "edu.txt")
            val writeStream: OutputStreamWriter = file.writer()
            writeStream.write("hello android!!\n")
            val data = model.toString() // 해당 모델을 문자열로 변환하여 전달합니다.
            writeStream.write(data + "\n")
            writeStream.flush() // 직접적으로 파일에 작성
            writeStream.close()
            Toast.makeText(context, "데이터가 저장되었습니다.", Toast.LENGTH_SHORT).show()
        }

        binding.menuRead.setOnClickListener {
            val file: File = File(context.filesDir, "edu.txt")
            val readStream: BufferedReader = file.reader().buffered()
            readStream.forEachLine {
                Log.d("mobileApp", "$it")
            }
        }
    }

}