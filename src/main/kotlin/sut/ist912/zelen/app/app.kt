package sut.ist912.zelen.app

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import sut.ist912m.zelen.app.dto.Receipt
import sut.ist912m.zelen.app.entity.Operation
import sut.ist912m.zelen.app.entity.UserProfile


fun main() {
    val objectMapper = ObjectMapper().registerModule(KotlinModule())
    val source = """
       [
            {
              "id":148021,
              "senderId":14882281337,
              "receiverId":14882281337,
              "opState":"COMPLETED",
              "opType":"DEPOSIT",
              "income":99.9,
              "outcome":0.0,
              "fee":0.0,
              "created":"2020-10-06T18:01:58.166Z",
              "updated":"2020-10-06T18:01:58.166Z"
            }
        ]
        """.trimIndent()

    print(objectMapper.readValue<List<Receipt>>(source))


}