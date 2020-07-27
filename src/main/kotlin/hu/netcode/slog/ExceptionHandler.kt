package hu.netcode.slog

import hu.netcode.slog.service.ExceptionService
import javax.persistence.EntityNotFoundException
import javax.servlet.http.HttpServletRequest
import javax.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
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

    @ExceptionHandler(value = [ConstraintViolationException::class])
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleConstraintViolationException(req: HttpServletRequest, ex: Exception): Map<String, Any> {
        logger.error("ConstraintViolationException {} {}", req, ex)
        return exceptionService.createResponseMap(ex, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [EntityNotFoundException::class])
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    fun handleEntityNotFoundException(req: HttpServletRequest, ex: java.lang.Exception): Map<String, Any> {
        logger.error("EntityNotFoundException {} {}", req, ex)
        return exceptionService.createResponseMap(ex, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [Exception::class])
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(req: HttpServletRequest, ex: Exception): Map<String, Any> {
        logger.error("Exception {} {}", req, ex)
        return exceptionService.createResponseMap(ex, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
