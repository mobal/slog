package hu.netcode.slog.exception

import com.amazonaws.AmazonClientException
import hu.netcode.slog.service.ExceptionService
import java.lang.NullPointerException
import javax.persistence.EntityNotFoundException
import javax.servlet.http.HttpServletRequest
import javax.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class ExceptionHandler(
    private val exceptionService: ExceptionService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(value = [AmazonClientException::class])
    fun handleAmazonClientException(req: HttpServletRequest, ex: AmazonClientException):
            ResponseEntity<Map<String, Any>> {
        logger.error("{} {} {}", ex::class, req, ex)
        val httpStatus = HttpStatus.valueOf("(Status Code: \\d{3})".toRegex().find(ex.message!!)?.value
            ?.takeLast(3)!!.toInt())
        return ResponseEntity(exceptionService.createResponseMap(ex, httpStatus), httpStatus)
    }

    @ExceptionHandler(value = [ConstraintViolationException::class])
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleConstraintViolationException(req: HttpServletRequest, ex: ConstraintViolationException):
            Map<String, Any> {
        logger.error("ConstraintViolationException {} {}", req, ex)
        return exceptionService.createResponseMap(ex, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [EntityNotFoundException::class])
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    fun handleEntityNotFoundException(req: HttpServletRequest, ex: EntityNotFoundException): Map<String, Any> {
        logger.error("EntityNotFoundException {} {}", req, ex)
        return exceptionService.createResponseMap(ex, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [
        Exception::class,
        NullPointerException::class
    ])
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(req: HttpServletRequest, ex: Exception): Map<String, Any> {
        logger.error("{} {} {}", ex::class, req, ex)
        return exceptionService.createResponseMap(ex, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
