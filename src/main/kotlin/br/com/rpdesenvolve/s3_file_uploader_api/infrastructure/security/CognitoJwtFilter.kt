package br.com.rpdesenvolve.s3_file_uploader_api.infrastructure.security

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.source.RemoteJWKSet
import com.nimbusds.jose.proc.JWSVerificationKeySelector
import com.nimbusds.jose.proc.SecurityContext
import com.nimbusds.jwt.proc.DefaultJWTProcessor
import com.nimbusds.jwt.proc.JWTProcessor
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.net.URL

@Component
class CognitoJwtFilter(
    @Value("\${aws.cognito.jwk-endpoint}") private val jwkEndpoint: String,
    @Value("\${aws.cognito.client-id}") private val clientId: String
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(CognitoJwtFilter::class.java)

    private val jwtProcessor: JWTProcessor<SecurityContext> by lazy {
        val jwkSetURL = URL(jwkEndpoint)
        val jwkSource = RemoteJWKSet<SecurityContext>(jwkSetURL)
        val keySelector = JWSVerificationKeySelector(JWSAlgorithm.RS256, jwkSource)
        DefaultJWTProcessor<SecurityContext>().apply {
            jwsKeySelector = keySelector
        }
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val header = request.getHeader("Authorization")

        if (header?.startsWith("Bearer ") == true) {
            val token = header.removePrefix("Bearer ").trim()

            try {
                val claims = jwtProcessor.process(token, null)

                val audience = claims.subject
                val subject = claims.subject ?: "anonymous"

                if (!audience.contains(clientId)) {
                    log.warn("Invalid audience in JWT: expected={}, actual={}", clientId, audience)
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token audience")
                    return
                }

                val authorities = listOf(SimpleGrantedAuthority("ROLE_SYSTEM"))

                val auth = UsernamePasswordAuthenticationToken(subject, null, authorities)
                SecurityContextHolder.getContext().authentication = auth

                log.info("Final auth object: {}", auth)
                log.info("JWT validated: sub=$subject, roles=$authorities")

            } catch (ex: Exception) {
                log.error("JWT validation error: ${ex.message}")
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token")
                return
            }
        } else {
            log.debug("No Authorization header found or not a Bearer token")
        }

        filterChain.doFilter(request, response)
    }
}