package com.example.newjobinsenior

import java.util.Date

// Json : javascript 에서 데이터를 표현하는 방법, 웹에서 데이터를 주고받을 때 주로 사용
// Gson : json을 JAVA의 객체로 역직렬화, 직렬화 해주는 자바 라이브러리
// body.items[item.rnum]

data class ItemRetrofitModel (
    var IDX: Long = 0,
    var SUBJECT: String? = null,
    var STARTDATE: String? = null,
    var ENDDATE: String? = null,
    var APPLICATIONSTARTDATE: String? = null,
    var APPLICATIONENDDATE: String? = null,
    var REGISTPEOPLE: String? = null,
    var REGISTCOST: String? = null,
    var APPLY_STATE: String? = null,
    var VIEWDETAIL: String? = null,
)
data class MyItems(val row: MutableList<ItemRetrofitModel>)
data class MyModel(val tbViewProgram: MyItems)