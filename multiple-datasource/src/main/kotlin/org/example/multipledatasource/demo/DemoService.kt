package org.example.multipledatasource.demo

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DemoService(
    private val demoRepository: DemoRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun findAll(): List<Demo> {
        logger.info("findAll 메서드 호출")
        return demoRepository.findAll()
    }

    @Transactional
    fun save(name: String) {
        logger.info("save 메서드 호출")
        demoRepository.save(Demo(name = name))
    }
}