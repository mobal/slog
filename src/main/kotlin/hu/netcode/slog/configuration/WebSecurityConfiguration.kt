package hu.netcode.slog.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import hu.netcode.slog.service.ExceptionService
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.DefaultRedirectStrategy
import javax.servlet.http.HttpServletRequest

@Configuration
class WebSecurityConfiguration(
    private val exceptionService: ExceptionService,
    private val objectMapper: ObjectMapper,
) : WebSecurityConfigurerAdapter() {
    private companion object {
        const val BASE_URL: String = "/"
    }

    override fun configure(http: HttpSecurity?) {
        http?.authorizeRequests {
            it.antMatchers("/api/auth/**").authenticated()
            it.antMatchers(HttpMethod.DELETE).authenticated()
            it.antMatchers(HttpMethod.GET).permitAll()
            it.antMatchers(HttpMethod.POST).authenticated()
            it.antMatchers(HttpMethod.PUT).authenticated()
        }
        http?.csrf()?.disable()
        http?.logout {
            it.permitAll()
            it.logoutSuccessHandler { request, response, _ ->
                if (isJsonRequest(request)) {
                    response.status = HttpStatus.OK.value()
                } else {
                    DefaultRedirectStrategy()
                        .sendRedirect(request, response, BASE_URL)
                }
            }
        }
    }

    private fun isJsonRequest(request: HttpServletRequest): Boolean {
        return checkHeader(request)
    }

    private fun checkHeader(
        request: HttpServletRequest,
        header: String = "Accept",
        type: String = MediaType.APPLICATION_JSON_VALUE
    ): Boolean {
        return request.getHeader(header).isNullOrEmpty() && request.getHeader(header) == type
    }
}
