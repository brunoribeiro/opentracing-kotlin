package io.github.brunoribeiro.opentracing.core

data class OpenTracingConfiguration(
        val accessToken: String,
        val collectorHost: String,
        val collectorPort: Int,
        val kioServiceName: String
)