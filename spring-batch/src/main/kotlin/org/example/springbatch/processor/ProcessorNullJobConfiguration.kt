package org.example.springbatch.processor

import jakarta.persistence.EntityManagerFactory
import org.example.springbatch.domain.Teacher
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class ProcessorNullJobConfiguration(
    private val jobRepository: JobRepository,
    private val entityManagerFactory: EntityManagerFactory,
    @Qualifier("jpaTransactionManager") private val jpaTransactionManager: PlatformTransactionManager,
    @Value("\${chunkSize:1000}") private val chunkSize: Int
) {
    companion object {
        private const val JOB_NAME = "ProcessorNullBatch"
        private const val BEAN_PREFIX = JOB_NAME + "_"
    }

    @Bean(JOB_NAME)
    fun job(): Job {
        return JobBuilder(JOB_NAME, jobRepository)
            .start(step())
            .build()
    }

    @Bean("${BEAN_PREFIX}step")
    @JobScope
    fun step(): Step {
        return StepBuilder("${BEAN_PREFIX}step", jobRepository)
            .chunk<Teacher, Teacher>(chunkSize, jpaTransactionManager)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .build()
    }

    @Bean("${BEAN_PREFIX}reader")
    fun reader(): JpaPagingItemReader<Teacher> {
        return JpaPagingItemReaderBuilder<Teacher>()
            .name("${BEAN_PREFIX}reader")
            .entityManagerFactory(entityManagerFactory)
            .pageSize(chunkSize)
            .queryString("SELECT t FROM Teacher t")
            .build()
    }

    @Bean("${BEAN_PREFIX}processor")
    fun processor(): ItemProcessor<Teacher, Teacher> {
        return ItemProcessor {
            if (it.id!! % 2 == 0L) null
            else it
        }
    }

    private fun writer(): ItemWriter<Teacher> {
        return ItemWriter { items ->
            println("=====chunk=====")
            items.forEach { println("${it.id} ${it.name}") }
        }
    }
}