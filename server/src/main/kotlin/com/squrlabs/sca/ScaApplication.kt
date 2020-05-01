package com.squrlabs.sca

import com.squrlabs.sca.config.AppProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(AppProperties::class)
class ScaApplication

fun main(args: Array<String>) {
	runApplication<ScaApplication>(*args)
}
