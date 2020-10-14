package sut.ist912m.zelen.app.entity

import java.util.*

data class Operation (
        val id : Long,
        val senderId : Long,
        val receiverId : Long,
        val opState: String,
        val opType: String,
        val income : Double,
        val outcome : Double,
        val fee : Double,
        val created : Date,
        val updated : Date
)