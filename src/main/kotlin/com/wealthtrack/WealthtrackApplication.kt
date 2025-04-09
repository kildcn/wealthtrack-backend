package com.wealthtrack

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WealthtrackApplication

fun main(args: Array<String>) {
    runApplication<WealthtrackApplication>(*args)
}
