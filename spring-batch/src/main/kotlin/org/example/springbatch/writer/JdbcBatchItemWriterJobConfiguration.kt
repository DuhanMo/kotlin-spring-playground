package org.example.springbatch.writer

import org.example.springbatch.domain.Pay
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.database.JdbcCursorItemReader
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.SimplePropertyRowMapper
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import javax.sql.DataSource

@Configuration
class JdbcBatchItemWriterJobConfiguration(
    private val dataSource: DataSource
) {

    companion object {
        private const val CHUNK_SIZE = 10
    }

    @Bean
    fun jdbcBatchItemWriterJob(jobRepository: JobRepository, jdbcBatchItemWriterStep: Step): Job {
        return JobBuilder("jdbcBatchItemWriterJob", jobRepository)
            .start(jdbcBatchItemWriterStep)
            .build()
    }

    @Bean
    fun jdbcBatchItemWriterStep(
        jobRepository: JobRepository,
        dataSourceTransactionManager: DataSourceTransactionManager,
        jdbcBatchItemWriterReader: JdbcCursorItemReader<Pay>,
        jdbcBatchItemWriter: JdbcBatchItemWriter<Pay>
    ): Step {
        return StepBuilder("jdbcBatchItemWriterStep", jobRepository)
            .chunk<Pay, Pay>(CHUNK_SIZE, dataSourceTransactionManager)
            .reader(jdbcBatchItemWriterReader)
            .writer(jdbcBatchItemWriter)
            .build()
    }

    @Bean
    fun jdbcBatchItemWriterReader(): JdbcCursorItemReader<Pay> {
        return JdbcCursorItemReaderBuilder<Pay>()
            .fetchSize(CHUNK_SIZE)
            .dataSource(dataSource)
            .rowMapper(SimplePropertyRowMapper(Pay::class.java))
            .sql("SELECT id, amount, tx_name, tx_date_time FROM pay")
            .name("jdbcCursorItemReader")
            .build()
    }

    @Bean
    fun jdbcBatchItemWriter(): JdbcBatchItemWriter<Pay> {
        return JdbcBatchItemWriterBuilder<Pay>()
            .dataSource(dataSource)
            .sql("insert into pay2(amount, tx_name, tx_date_time) values (:amount, :txName, :txDateTime)")
            .beanMapped()
            .build()
    }
}