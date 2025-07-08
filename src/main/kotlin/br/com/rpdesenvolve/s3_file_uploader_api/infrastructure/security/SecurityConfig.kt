package br.com.rpdesenvolve.s3_file_uploader_api.infrastructure.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val cognitoJwtFilter: CognitoJwtFilter
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers(HttpMethod.POST, "/v1/files").hasAuthority("ROLE_SYSTEM")
                    .requestMatchers(HttpMethod.GET, "/v1/files/**").hasAnyRole("SYSTEM", "USER")
                    .anyRequest().authenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(cognitoJwtFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}
