package sut.ist912.zelen.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule

open class BaseClient {

    protected val API_URL = "http://localhost:8085"

    protected val objectMapper = ObjectMapper().registerModule(KotlinModule())

}