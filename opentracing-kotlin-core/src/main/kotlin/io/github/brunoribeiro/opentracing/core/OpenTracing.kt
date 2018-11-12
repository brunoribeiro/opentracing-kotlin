package io.github.brunoribeiro.opentracing.core

import com.lightstep.tracer.jre.JRETracer
import com.lightstep.tracer.shared.Options
import io.opentracing.Span
import io.opentracing.SpanContext
import io.opentracing.Tracer
import io.opentracing.noop.NoopTracerFactory
import io.opentracing.propagation.Format
import io.opentracing.propagation.TextMapExtractAdapter
import io.opentracing.util.GlobalTracer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object OpenTracing {

    private val logger: Logger = LoggerFactory.getLogger(OpenTracing::class.java)

    private var tracer: Tracer? = null

    private lateinit var headersResolver: () -> Map<String, String>

    private fun spanContext(): SpanContext? = tracer()?.extract(Format.Builtin.HTTP_HEADERS, TextMapExtractAdapter(headersResolver.invoke()))

    fun tracer() = tracer

    fun configure(configuration: OpenTracingConfiguration,
                  headersResolver: () -> Map<String, String> = { emptyMap() }) {

        try {
            val options = Options.OptionsBuilder()
                    .withAccessToken(configuration.accessToken)
                    .withCollectorHost(configuration.collectorHost)
                    .withCollectorPort(configuration.collectorPort)
                    .withComponentName(configuration.kioServiceName)
                    .withClockSkewCorrection(false)
                    .build()

            val tracer = JRETracer(options)

            when (GlobalTracer.isRegistered()) {
                false -> GlobalTracer.register(tracer)
            }

            OpenTracing.tracer = tracer
            OpenTracing.headersResolver = headersResolver

        } catch (e: RuntimeException) {
            logger.error("Could not configure tracing, using noop.", e)
            tracer = NoopTracerFactory.create()
        }
    }


    fun span(): Span? = tracer()?.activeSpan() ?: throw TracerNotConfiguredException()


    fun <T> trace(operationName: String, block: () -> T?): T {

        assert(tracer() != null) { throw TracerNotConfiguredException() }

        val spanContext = span()?.context()?: spanContext()

        val scope = spanContext?.let { tracer()?.buildSpan(operationName)?.asChildOf(it) }
                ?: tracer()?.buildSpan(operationName)

        return scope?.startActive(true).use {
            block.invoke()!!
        }

    }
}

