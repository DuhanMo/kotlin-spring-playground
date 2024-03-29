package org.example.springbatch.config

import jakarta.persistence.EntityManagerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
class DataSourceConfig(
    private val entityManagerFactory: EntityManagerFactory
) {

    @Bean
    @Primary
    fun dataSourceTransactionManager(dataSource: DataSource): DataSourceTransactionManager {
        return DataSourceTransactionManager(dataSource)
    }

    @Bean
    @Qualifier("jpaTransactionManager")
    fun jpaTransactionManager(): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }
}