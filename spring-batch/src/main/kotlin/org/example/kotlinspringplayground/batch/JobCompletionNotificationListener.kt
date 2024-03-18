package org.example.kotlinspringplayground.batch

import org.slf4j.LoggerFactory
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class JobCompletionNotificationListener(
    private val jdbcTemplate: JdbcTemplate
) : JobExecutionListener {
    companion object {
        private val logger = LoggerFactory.getLogger(JobCompletionNotificationListener::class.java)
    }

    override fun afterJob(jobExecution: JobExecution) {
        if (jobExecution.status == BatchStatus.COMPLETED) {
            logger.info("!! JOB FINISHED! Time to verify the results!!")
            jdbcTemplate
                .query("SELECT first_name, last_name FROM people", DataClassRowMapper(Person::class.java))
                .forEach { logger.info("Found $it in the database") }
        }
    }
}