package org.example.springbatch.reader

import org.example.springbatch.domain.Pay
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JdbcPagingItemReader
import org.springframework.batch.item.database.Order
import org.springframework.batch.item.database.PagingQueryProvider
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.SimplePropertyRowMapper
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import javax.sql.DataSource

@Configuration
class JdbcPagingItemReaderJobConfiguration(
    private val dataSource: DataSource
) {

    companion object {
        private const val CHUNK_SIZE = 10
    }

    @Bean
    fun jdbcPagingItemReaderJob(jobRepository: JobRepository, jdbcPagingItemReaderStep: Step): Job {
        return JobBuilder("jdbcPagingItemReaderJob", jobRepository)
            .start(jdbcPagingItemReaderStep)
            .build()
    }

    @Bean
    fun jdbcPagingItemReaderStep(
        jobRepository: JobRepository,
        dataSourceTransactionManager: DataSourceTransactionManager,
        jdbcPagingItemReader: JdbcPagingItemReader<Pay>,
    ): Step {
        return StepBuilder("jdbcPagingItemReaderStep", jobRepository)
            .chunk<Pay, Pay>(CHUNK_SIZE, dataSourceTransactionManager)
            .reader(jdbcPagingItemReader)
            .writer(jdbcPagingItemWriter())
            .build()
    }

    @Bean
    fun jdbcPagingItemReader(): JdbcPagingItemReader<Pay> {
        val parameterValues = mutableMapOf<String, Any>()
        parameterValues["amount"] = 2000
        return JdbcPagingItemReaderBuilder<Pay>()
            .pageSize(CHUNK_SIZE)
            .fetchSize(CHUNK_SIZE)
            .dataSource(dataSource)
            .rowMapper(SimplePropertyRowMapper(Pay::class.java))
            .queryProvider(createQueryProvider())
            .parameterValues(parameterValues)
            .name("jdbcPagingItemReader")
            .build()
    }

    private fun jdbcPagingItemWriter(): ItemWriter<Pay> {
        return ItemWriter<Pay> { list ->
            list.map { println("Current Pay = ${it.id} ${it.amount} ${it.txName} ${it.txDateTime}") }
        }
    }

    @Bean
    fun createQueryProvider(): PagingQueryProvider {
        val queryProvider = SqlPagingQueryProviderFactoryBean()
        queryProvider.setDataSource(dataSource)
        queryProvider.setSelectClause("id, amount, tx_name, tx_date_time")
        queryProvider.setFromClause("from pay")
        queryProvider.setWhereClause("where amount >= :amount")

        val sortKeys = mutableMapOf<String, Order>()
        sortKeys["id"] = Order.ASCENDING
        queryProvider.setSortKeys(sortKeys)
        return queryProvider.`object`
    }
}