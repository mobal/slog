package hu.netcode.slog.exception

import com.amazonaws.AmazonClientException
import hu.netcode.slog.service.ExceptionService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException
import java.lang.NullPointerException
import javax.servlet.http.HttpServletRequest
import javax.validation.ConstraintViolationException

@RestControllerAdvice
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class ExceptionHandler(
    private val exceptionService: ExceptionService
) {
    private companion object {
        const val STATUS_CODE = "Status Code"
    }
    private val logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(value = [AmazonClientException::class])
    fun handleAmazonClientException(req: HttpServletRequest, ex: AmazonClientException):
        ResponseEntity<Map<String, Any>> {
        logger.error("{} {} {}", ex::class, req, ex)
        val httpStatus = if (ex.message!!.contains(STATUS_CODE)) {
            HttpStatus.valueOf("(Status Code: \\d{3})".toRegex().find(ex.message!!)?.value?.takeLast(3)!!.toInt())
        } else {
            HttpStatus.INTERNAL_SERVER_ERROR
        }
        return ResponseEntity(exceptionService.createResponseMap(ex, httpStatus), httpStatus)
    }

    @ExceptionHandler(
        value = [
            ConstraintViolationException::class,
            MethodArgumentNotValidException::class
        ]
    )
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleConstraintViolationException(req: HttpServletRequest, ex: ConstraintViolationException):
        Map<String, Any> {
        logger.error("ConstraintViolationException {} {}", req, ex)
        return exceptionService.createResponseMap(ex, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [DocumentNotFoundException::class])
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    fun handleDocumentNotFoundException(req: HttpServletRequest, ex: DocumentNotFoundException): Map<String, Any> {
        logger.error("DocumentNotFoundException {} {}", req, ex)
        return exceptionService.createResponseMap(ex, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(
        value = [
            Exception::class,
            NullPointerException::class
        ]
    )
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(req: HttpServletRequest, ex: Exception): Map<String, Any> {
        logger.error("{} {} {}", ex::class, req, ex)
        return exceptionService.createResponseMap(ex, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(
        value = [
            NoHandlerFoundException::class
        ]
    )
    fun handleNoHandlerFoundException(req: HttpServletRequest, ex: Exception): Map<String, Any> {
        logger.error("NoHandlerFoundException {} {}", ex::class, req, ex)
        return exceptionService.createResponseMap("Not found", HttpStatus.NOT_FOUND)
    }
}
