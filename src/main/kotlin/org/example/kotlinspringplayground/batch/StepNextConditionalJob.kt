package org.example.kotlinspringplayground.batch

import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DataSourceTransactionManager

@Configuration
class StepNextConditionalJob {
    companion object {
        private val logger = LoggerFactory.getLogger(StepNextConditionalJob::class.java)
    }

    @Bean
    fun conditionalJob(
        jobRepository: JobRepository,
        conditionalStep1: Step,
        conditionalStep2: Step,
        conditionalStep3: Step
    ): Job {
        return JobBuilder("conditionalJob", jobRepository)
            .start(conditionalStep1)
            .on("FAILED") // FAILED 일 경우
            .to(conditionalStep3) // step3로 이동한다.
            .on("*") // step3의 결과 관계 없이
            .end() // Flow를 종료한다.
            .from(conditionalStep1) // step1로부터
            .on("*") // FAILED 외 모든 경우
            .to(conditionalStep2) // step2로 이동한다.
            .next(conditionalStep3) // step2가 정상 종료되면 step3으로 이동한다.
            .on("*") // step3의 결과 관계 없이
            .end() // Flow를 종료한다.
            .end() // Job 종료
            .build()
    }

    @Bean
    fun conditionalStep1(
        jobRepository: JobRepository,
        transactionManager: DataSourceTransactionManager,
    ): Step {
        return StepBuilder("conditionalStep1", jobRepository)
            .tasklet({ contribution, _ ->
                logger.info(">>> This is conditionalStep1")
//                contribution.exitStatus = ExitStatus.FAILED
                RepeatStatus.FINISHED
            }, transactionManager)
            .build()
    }

    @Bean
    fun conditionalStep2(
        jobRepository: JobRepository,
        transactionManager: DataSourceTransactionManager,
    ): Step {
        return StepBuilder("conditionalStep2", jobRepository)
            .tasklet({ _, _ ->
                logger.info(">>> This is conditionalStep2")
                RepeatStatus.FINISHED
            }, transactionManager)
            .build()
    }

    @Bean
    fun conditionalStep3(
        jobRepository: JobRepository,
        transactionManager: DataSourceTransactionManager,
    ): Step {
        return StepBuilder("step3", jobRepository)
            .tasklet({ _, _ ->
                logger.info(">>> This is conditionalStep3")
                RepeatStatus.FINISHED
            }, transactionManager)
            .build()
    }
}