package hu.netcode.slog.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
class WebSecurityConfiguration : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        http?.authorizeRequests {
            it.antMatchers("/api/auth/**").authenticated()
            it.antMatchers(HttpMethod.DELETE).authenticated()
            it.antMatchers(HttpMethod.GET).permitAll()
            it.antMatchers(HttpMethod.POST).authenticated()
            it.antMatchers(HttpMethod.PUT).authenticated()
        }
        http?.oauth2Login()
    }
}
