package io.github.brunoribeiro.opentracing.spring.aop

import io.github.brunoribeiro.opentracing.core.OpenTracing.trace
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component


@Aspect
@Component
class TraceAspect {

    @Around("@annotation(Trace)")
    @Throws(Throwable::class)
    fun traceMethod(joinPoint: ProceedingJoinPoint): Any {

        val signature = joinPoint.signature as MethodSignature
        val method = signature.method
        val operation = method.getAnnotation(Traceable::class.java).operation

        return trace(operation) {
            joinPoint.proceed()
        }
    }

}

