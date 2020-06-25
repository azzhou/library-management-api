package com.example.library.service

import com.ninjasquad.springmockk.SpykBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@ActiveProfiles("test_no_security")
// Testing with FineService.updateFines() run every 1 second instead of daily at midnight
@TestPropertySource(properties = ["schedule.fine.cron=* * * ? * *"])
class ScheduledFinesTest {

    // Using spy to mock only part of the service - still need the @Scheduled annotation
    @SpykBean
    lateinit var fineService: FineService

    @BeforeAll
    fun setup() {
        every { fineService.updateFines() } just Runs
    }

    @Test
    fun `when wait 3 seconds then updateFines is called 3-4 times`() {
        val seconds = 3
        Thread.sleep(1000L * seconds)
        verify(atLeast = seconds, atMost = seconds + 1) { fineService.updateFines() }
    }

    @Test
    fun `when wait 6 seconds then updateFines is called 6-7 times`() {
        val seconds = 6
        Thread.sleep(1000L * seconds)
        verify(atLeast = seconds, atMost = seconds + 1) { fineService.updateFines() }
    }

}