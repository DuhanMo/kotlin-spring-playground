package io.duhanmo.distributedlock

import java.lang.reflect.Method
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Aspect
@Component
class DistributedLockAop(
    private val redissonClient: RedissonClient,
    private val aopForTransaction: AopForTransaction,
) {
    @Around("@annotation(io.duhanmo.distributedlock.DistributedLock)")
    fun lock(joinPoint: ProceedingJoinPoint): Any? {
        val methodSignature: MethodSignature = joinPoint.signature as MethodSignature
        val method: Method = methodSignature.method
        val distributedLock: DistributedLock = method.getAnnotation(DistributedLock::class.java)

        val key = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(
            methodSignature.parameterNames,
            joinPoint.args,
            distributedLock.key
        )
        val rLock = redissonClient.getLock(key)
        try {
            val available = rLock.tryLock(distributedLock.waitTime, distributedLock.leaseTime, distributedLock.timeUnit)
            if (!available) {
                return false
            }
            return aopForTransaction.proceed(joinPoint)
        } catch (e: InterruptedException) {
            throw e
        } finally {
            try {
                rLock.unlock()
            } catch (e: IllegalMonitorStateException) {
                log.info("Redisson Lock Already Unlock service: ${method.name}, key: $key")
            }
        }
    }

    companion object {
        private const val REDISSON_LOCK_PREFIX = "LOCK:"
        private val log = LoggerFactory.getLogger(DistributedLockAop::class.java)
    }
}

