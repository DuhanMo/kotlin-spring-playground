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
class NextStepConfiguration {

    companion object {
        private val logger = LoggerFactory.getLogger(NextStepConfiguration::class.java)
    }

    @Bean
    fun stepNextJob(jobRepository: JobRepository, step1: Step, step2: Step, step3: Step): Job {
        return JobBuilder("stepNextJob", jobRepository)
            .start(step1)
            .next(step2)
            .next(step3)
            .build()
    }

    @Bean
    fun step1(
        jobRepository: JobRepository,
        transactionManager: DataSourceTransactionManager,
    ): Step {
        return StepBuilder("step1", jobRepository)
            .tasklet({ _, _ ->
                logger.info(">>> This is step1")
                RepeatStatus.FINISHED
            }, transactionManager)
            .build()
    }

    @Bean
    fun step2(
        jobRepository: JobRepository,
        transactionManager: DataSourceTransactionManager,
    ): Step {
        return StepBuilder("step2", jobRepository)
            .tasklet({ _, _ ->
                logger.info(">>> This is step2")
                RepeatStatus.FINISHED
            }, transactionManager)
            .build()
    }

    @Bean
    fun step3(
        jobRepository: JobRepository,
        transactionManager: DataSourceTransactionManager,
    ): Step {
        return StepBuilder("step3", jobRepository)
            .tasklet({ _, _ ->
                logger.info(">>> This is step3")
                RepeatStatus.FINISHED
            }, transactionManager)
            .build()
    }

}