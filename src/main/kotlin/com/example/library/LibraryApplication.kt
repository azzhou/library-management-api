package com.example.library

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.*
import javax.annotation.PostConstruct

@SpringBootApplication
@EnableScheduling
class LibraryApplication {
	@PostConstruct
	fun setTZ() {
		TimeZone.setDefault(TimeZone.getTimeZone("America/Los_Angeles"))
	}
}

fun main(args: Array<String>) {
	runApplication<LibraryApplication>(*args)
}
