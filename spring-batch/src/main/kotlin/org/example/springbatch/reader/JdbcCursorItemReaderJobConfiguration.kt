package org.example.springbatch.reader

import org.example.springbatch.domain.Pay
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JdbcCursorItemReader
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.SimplePropertyRowMapper
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import javax.sql.DataSource

@Configuration
class JdbcCursorItemReaderJobConfiguration {

    companion object {
        private const val CHUNK_SIZE = 10
    }

    @Bean
    fun jdbcCursorItemReaderJob(jobRepository: JobRepository, jdbcCursorItemReaderStep: Step): Job {
        return JobBuilder("jdbcCursorItemReaderJob", jobRepository)
            .start(jdbcCursorItemReaderStep)
            .build()
    }

    @Bean
    fun jdbcCursorItemReaderStep(
        jobRepository: JobRepository,
        dataSourceTransactionManager: DataSourceTransactionManager,
        jdbcCursorItemReader: JdbcCursorItemReader<Pay>,
    ): Step {
        return StepBuilder("jdbcCursorItemReaderStep", jobRepository)
            .chunk<Pay, Pay>(CHUNK_SIZE, dataSourceTransactionManager)
            .reader(jdbcCursorItemReader)
            .writer(jdbcCursorItemWriter())
            .build()
    }

    @Bean
    fun jdbcCursorItemReader(dataSource: DataSource): JdbcCursorItemReader<Pay> {
        return JdbcCursorItemReaderBuilder<Pay>()
            .fetchSize(CHUNK_SIZE)
            .dataSource(dataSource)
            .rowMapper(SimplePropertyRowMapper(Pay::class.java))
            .sql("SELECT id, amount, tx_name, tx_date_time FROM pay")
            .name("jdbcCursorItemReader")
            .build()
    }

    private fun jdbcCursorItemWriter(): ItemWriter<Pay> {
        return ItemWriter<Pay> { list ->
            list.map { println("Current Pay = ${it.id} ${it.amount} ${it.txName} ${it.txDateTime}") }
        }
    }

}