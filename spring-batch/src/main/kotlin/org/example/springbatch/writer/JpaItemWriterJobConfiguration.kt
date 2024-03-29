package org.example.springbatch.writer

import jakarta.persistence.EntityManagerFactory
import org.example.springbatch.domain.Pay
import org.example.springbatch.domain.Pay2
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.database.JpaItemWriter
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class JpaItemWriterJobConfiguration(
    private val jobRepository: JobRepository,
    private val entityManagerFactory: EntityManagerFactory,
    @Qualifier("jpaTransactionManager") private val jpaTransactionManager: PlatformTransactionManager
) {
    companion object {
        private const val CHUNK_SIZE = 10
    }

    @Bean
    fun jpaItemWriterJob(): Job {
        return JobBuilder("jpaItemWriterJob", jobRepository)
            .start(jpaItemWriterStep())
            .build()
    }

    @Bean
    fun jpaItemWriterStep(): Step {
        return StepBuilder("jpaItemWriterStep", jobRepository)
            .chunk<Pay, Pay2>(CHUNK_SIZE, jpaTransactionManager)
            .reader(jpaPagingItemReader())
            .processor(jpaItemProcessor())
            .writer(jpaItemWriter())
            .build()
    }

    @Bean
    fun jpaPagingItemReader(): JpaPagingItemReader<Pay> {
        return JpaPagingItemReaderBuilder<Pay>()
            .name("jpaPagingItemReader")
            .entityManagerFactory(entityManagerFactory)
            .pageSize(CHUNK_SIZE)
            .queryString("SELECT p FROM Pay p")
            .build()
    }

    @Bean
    fun jpaItemProcessor(): ItemProcessor<Pay, Pay2> {
        return ItemProcessor { pay -> Pay2(pay.amount, pay.txName, pay.txDateTime) }
    }

    @Bean
    fun jpaItemWriter(): JpaItemWriter<Pay2> {
        val jpaItemWriter = JpaItemWriter<Pay2>()
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory)
        return jpaItemWriter
    }

}