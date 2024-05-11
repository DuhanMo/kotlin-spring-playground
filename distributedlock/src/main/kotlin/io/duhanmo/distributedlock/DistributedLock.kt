package io.duhanmo.distributedlock

import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.SECONDS
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FUNCTION

@Target(FUNCTION)
@Retention(RUNTIME)
annotation class DistributedLock(
    /**
     * 락 이름
     */
    val key: String,

    /**
     * 락 시간 단위
     */
    val timeUnit: TimeUnit = SECONDS,

    /**
     * 락을 기다리는 최대 시간
     * 해당 시간 지난 후 락 획득 포기
     */
    val waitTime: Long = 3,

    /**
     * 락을 갖고 있는 최대 시간
     * 해당 시간 지난 후 락 해제
     */
    val leaseTime: Long = 5,
)
