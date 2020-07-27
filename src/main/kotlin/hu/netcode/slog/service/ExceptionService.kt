package hu.netcode.slog.service

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class ExceptionService {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun createResponseMap(ex: Exception, status: HttpStatus): Map<String, Any> {
        return mapOf("code" to status.value(), "msg" to ex.message!!)
    }
}
