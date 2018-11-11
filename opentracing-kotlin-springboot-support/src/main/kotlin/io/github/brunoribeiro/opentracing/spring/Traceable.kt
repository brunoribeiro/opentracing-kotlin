package io.github.brunoribeiro.opentracing.spring

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Traceable(val operation: String)