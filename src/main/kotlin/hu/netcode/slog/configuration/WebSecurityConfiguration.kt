package hu.netcode.slog.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
class WebSecurityConfiguration : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        http?.authorizeRequests {
            it.antMatchers(HttpMethod.DELETE).authenticated()
            it.antMatchers(HttpMethod.GET).anonymous()
            it.antMatchers(HttpMethod.POST).authenticated()
            it.antMatchers(HttpMethod.PUT).authenticated()
        }
        http?.formLogin()
        http?.httpBasic()
    }
}
