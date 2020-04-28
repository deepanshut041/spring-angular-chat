package com.squrlabs.sca

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ScaApplication

fun main(args: Array<String>) {
	runApplication<ScaApplication>(*args)
}
