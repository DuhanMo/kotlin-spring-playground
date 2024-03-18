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
class ScopeJobConfiguration {
    companion object {
        private val logger = LoggerFactory.getLogger(ScopeJobConfiguration::class.java)
    }

    @Bean
    fun scopeJob(jobRepository: JobRepository, scopeStep: Step): Job {
        return JobBuilder("scopeJob", jobRepository)
            .start(scopeStep)
            .build()
    }

    @Bean
    fun scopeStep(jobRepository: JobRepository, transactionManager: DataSourceTransactionManager): Step {
        return StepBuilder("scopeStep", jobRepository)
            .tasklet({ _, _ ->
                RepeatStatus.FINISHED
            }, transactionManager)
            .build()
    }
}