package io.github.brunoribeiro.opentracing.spring.config

import io.github.brunoribeiro.opentracing.core.OpenTracing
import io.github.brunoribeiro.opentracing.core.OpenTracingConfiguration
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import javax.annotation.PostConstruct

@Configuration
class OpenTracingSpringConfiguration {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(OpenTracingSpringConfiguration::class.java)

    }

    @PostConstruct
    fun setupTracer(config: OpenTracingPropertiesConfiguration) =
            OpenTracing.configure(config.asOpenTracingConfiguration()) { httpHeaders() }


    private fun httpHeaders(): Map<String, String> {
        val requestAttributes = RequestContextHolder.getRequestAttributes()
        val servletRequest = requestAttributes?.let { (it as ServletRequestAttributes).request }
        return servletRequest?.let { request -> request.headerNames.asSequence().map { it to request.getHeader(it) }.toMap() }
                ?: let {
                    logger.warn("Could not retrieve request headers.")
                    emptyMap<String, String>()
                }
    }
}

@ConfigurationProperties(prefix = "opentracing")
class OpenTracingPropertiesConfiguration {
    var accessToken: String? = null
    var collectorHost: String? = null
    var collectorPort: Int? = null
    var serviceName: String? = null

    fun asOpenTracingConfiguration(): OpenTracingConfiguration {

        return with(this) {
            OpenTracingConfiguration(
                    accessToken!!,
                    collectorHost!!,
                    collectorPort!!,
                    serviceName!!
            )
        }
    }
}
