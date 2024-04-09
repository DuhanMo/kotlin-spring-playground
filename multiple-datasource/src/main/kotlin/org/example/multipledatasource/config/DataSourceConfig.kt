package org.example.multipledatasource.config

import org.springframework.context.annotation.Configuration
import com.zaxxer.hikari.HikariDataSource
import org.example.multipledatasource.config.DataSourceType.READ
import org.example.multipledatasource.config.DataSourceType.WRITE
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.transaction.support.TransactionSynchronizationManager.isCurrentTransactionReadOnly
import javax.sql.DataSource

@Configuration
class DataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari.write")
    fun writeDataSource(): DataSource = DataSourceBuilder.create()
        .type(HikariDataSource::class.java)
        .build()

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari.read")
    fun readDataSource(): DataSource = DataSourceBuilder.create()
        .type(HikariDataSource::class.java)
        .build()

    @Bean
    fun routingDataSource(
        @Qualifier("writeDataSource") writeDataSource: DataSource,
        @Qualifier("readDataSource") readDataSource: DataSource,
    ): DataSource {
        val routingDataSource = DynamicRoutingDataSource()

        routingDataSource.setDefaultTargetDataSource(writeDataSource)
        routingDataSource.setTargetDataSources(
            mapOf(
                WRITE to writeDataSource,
                READ to readDataSource
            )
        )
        return routingDataSource
    }

    @Bean
    @Primary
    fun datasource(routingDataSource: DataSource): DataSource {
        return LazyConnectionDataSourceProxy(routingDataSource)
    }

}

class DynamicRoutingDataSource : AbstractRoutingDataSource() {
    override fun determineCurrentLookupKey(): DataSourceType {
        val dataSourceType = if (isCurrentTransactionReadOnly()) READ else WRITE
        logger.info("현재 dataSourceType >>> $dataSourceType")
        return dataSourceType
    }
}


enum class DataSourceType {
    READ, WRITE
}

