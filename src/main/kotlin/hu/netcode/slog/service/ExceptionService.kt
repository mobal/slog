package hu.netcode.slog.service

import org.apache.commons.lang3.exception.ExceptionUtils
import org.apache.logging.log4j.LogManager
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class ExceptionService {
    private val logger = LogManager.getLogger(javaClass)

    fun createResponseMap(ex: Exception, status: HttpStatus): Map<String, Any> {
        return mapOf("code" to status.value(), "msg" to ExceptionUtils.getMessage(ex))
    }

    fun createResponseMap(msg: String, status: HttpStatus): Map<String, Any> {
        return mapOf("code" to status.value(), "msg" to msg)
    }
}
