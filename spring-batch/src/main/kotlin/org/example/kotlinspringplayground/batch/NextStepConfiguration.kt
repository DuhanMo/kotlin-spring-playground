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
    fun stepNextJob(jobRepository: JobRepository, nextStep1: Step, nextStep2: Step, nextStep3: Step): Job {
        return JobBuilder("stepNextJob", jobRepository)
            .start(nextStep1)
            .next(nextStep2)
            .next(nextStep3)
            .build()
    }

    @Bean
    fun nextStep1(
        jobRepository: JobRepository,
        transactionManager: DataSourceTransactionManager,
    ): Step {
        return StepBuilder("nextStep1", jobRepository)
            .tasklet({ _, _ ->
                logger.info(">>> This is step1")
                RepeatStatus.FINISHED
            }, transactionManager)
            .build()
    }

    @Bean
    fun nextStep2(
        jobRepository: JobRepository,
        transactionManager: DataSourceTransactionManager,
    ): Step {
        return StepBuilder("nextStep2", jobRepository)
            .tasklet({ _, _ ->
                logger.info(">>> This is step2")
                RepeatStatus.FINISHED
            }, transactionManager)
            .build()
    }

    @Bean
    fun nextStep3(
        jobRepository: JobRepository,
        transactionManager: DataSourceTransactionManager,
    ): Step {
        return StepBuilder("nextStep3", jobRepository)
            .tasklet({ _, _ ->
                logger.info(">>> This is step3")
                RepeatStatus.FINISHED
            }, transactionManager)
            .build()
    }

}