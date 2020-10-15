package sut.ist912.zelen.rest

import com.mashape.unirest.http.Unirest
import org.springframework.stereotype.Component
import sut.ist912.zelen.rest.dto.ZelenException
import sut.ist912.zelen.utils.jwt
import sut.ist912m.zelen.app.dto.Receipt
import sut.ist912m.zelen.app.dto.ZelenMessage
import sut.ist912m.zelen.app.entity.Operation
import java.lang.RuntimeException

@Component
class OperationClient : BaseClient() {


    fun deposit(amount: Double): Operation {
       return ioRequest(amount, "deposit")
    }

    fun withdrawal(amount: Double): Operation {
      return ioRequest(amount, "withdrawal")
    }


    fun createTransfer(receiverId: Long, amount: Double): Receipt{
        val response = Unirest.post("$API_URL/api/v1/ops/transfer")
                .header("Authorization", jwt())
                .header("Content-Type", "application/json")
                .body("""
                        {
                            "amount" : $amount,
                            "receiverId" : $receiverId
                        }
                    """.trimIndent()
                ).asString()
        when (response.status) {
            200 -> return objectMapper.readValue(response.body, Receipt::class.java)
            400 -> {
                val msg = objectMapper.readValue(response.body, ZelenMessage::class.java)
                throw ZelenException(msg.message)
            }
            else -> throw RuntimeException(response.body)
        }
    }

    fun confirmTransfer(transferId: Long, confirm: Boolean = true): Receipt{
        val response = Unirest.put("$API_URL/api/v1/ops/transfer")
                .header("Authorization", jwt())
                .header("Content-Type", "application/json")
                .body("""
                    {
                        "operationId" : $transferId,
                        "confirm" : $confirm
                    }
                    """.trimIndent()
                ).asString()
        when (response.status) {
            200 -> return objectMapper.readValue(response.body, Receipt::class.java)
            400 -> {
                val msg = objectMapper.readValue(response.body, ZelenMessage::class.java)
                throw ZelenException(msg.message)
            }
            else -> throw RuntimeException(response.body)
        }
    }

    private fun ioRequest(amount: Double, opType: String): Operation {
        val response = Unirest.post("$API_URL/api/v1/ops/$opType")
                .header("Authorization", jwt())
                .header("Content-Type", "application/json")
                .body(
                        """
                        {
                            "amount" : $amount
                        }
                    """.trimIndent()
                ).asString()
        when (response.status) {
            200 -> return objectMapper.readValue(response.body, Operation::class.java)
            400 -> {
                val msg = objectMapper.readValue(response.body, ZelenMessage::class.java)
                throw ZelenException(msg.message)
            }
            else -> throw RuntimeException(response.body)
        }

    }

}