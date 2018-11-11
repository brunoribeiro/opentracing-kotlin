package io.github.brunoribeiro.opentracing.spring.config

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Traceable(val operation: String)