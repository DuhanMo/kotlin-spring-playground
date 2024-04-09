package org.example.multipledatasource.demo

import org.springframework.data.jpa.repository.JpaRepository

interface DemoRepository : JpaRepository<Demo, Long>