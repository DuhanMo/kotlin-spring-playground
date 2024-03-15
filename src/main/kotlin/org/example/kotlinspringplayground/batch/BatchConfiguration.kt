package org.example.kotlinspringplayground.batch

import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DataSourceTransactionManager

@Configuration
class BatchConfiguration {
    companion object {
        private val logger = LoggerFactory.getLogger(BatchConfiguration::class.java)
    }
    @Bean
    fun simpleJob(jobRepository: JobRepository, simpleStep: Step): Job {
        return JobBuilder("simpleJob", jobRepository)
            .start(simpleStep)
            .build()
    }

    @Bean
    @JobScope
    fun simpleStep(
        @Value("#{jobParameters[requestDate]}") requestDate: String,
        jobRepository: JobRepository,
        transactionManager: DataSourceTransactionManager,
    ): Step {
        return StepBuilder("simpleStep", jobRepository)
            .tasklet({ _, _ ->
                logger.info(">>> This is simpleStep")
                logger.info(">>> requestDate = $requestDate")
                RepeatStatus.FINISHED
            }, transactionManager)
            .build()
    }
}