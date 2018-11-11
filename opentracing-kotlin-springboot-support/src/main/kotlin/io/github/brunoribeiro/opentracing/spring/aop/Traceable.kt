package io.github.brunoribeiro.opentracing.spring.aop

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Traceable(val operation: String)