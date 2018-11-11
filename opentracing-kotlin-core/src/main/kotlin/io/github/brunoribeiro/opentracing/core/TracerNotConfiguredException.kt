package io.github.brunoribeiro.opentracing.core

class TracerNotConfiguredException :
        Throwable("Tracer not configured, please call the configure method before trying any tracing.")
