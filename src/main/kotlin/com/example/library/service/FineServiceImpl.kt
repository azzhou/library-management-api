package com.example.library.service

import com.example.library.entity.User
import com.example.library.exception.InvalidFinePaymentException
import com.example.library.exception.UserNotFoundException
import com.example.library.repository.LoanRepository
import com.example.library.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


const val FINE_PER_PERIOD_IN_CENTS = 10


@Service
class FineServiceImpl @Autowired constructor(
    private val userRepository: UserRepository,
    private val loanRepository: LoanRepository
) : FineService {

    override fun payFine(userId: Long, paymentAmount: Long): User {
        val user = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException(userId)
        if (paymentAmount > user.fineBalanceCents || paymentAmount <= 0) {
            throw InvalidFinePaymentException()
        }
        user.fineBalanceCents -= paymentAmount
        return userRepository.save(user)
    }

    @Scheduled(cron = "\${schedule.fine.cron}")
    override fun updateFines() {
        val now = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)
        val overdueLoans = loanRepository.findByDateDueBefore(now)
        for (loan in overdueLoans) {
            loan.user.fineBalanceCents += FINE_PER_PERIOD_IN_CENTS
        }
    }
}