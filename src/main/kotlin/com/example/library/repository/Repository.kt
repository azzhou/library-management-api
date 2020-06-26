package com.example.library.repository

import com.example.library.entity.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import java.time.LocalDateTime


interface UserRepository : PagingAndSortingRepository<User, Long> {
    fun findByEmailIgnoreCase(email: String): User?
}


interface BookRepository : PagingAndSortingRepository<Book, String> {
    fun findByTitleIgnoreCaseContaining(title: String, pageable: Pageable): Page<Book>

    fun findByAuthors_id(authorId: Long, pageable: Pageable): Page<Book>
}


interface BookCopyRepository : PagingAndSortingRepository<BookCopy, Long> {
    fun findByBook_isbn(isbn: String, pageable: Pageable): Page<BookCopy>
}


interface AuthorRepository : PagingAndSortingRepository<Author, Long> {
    fun findByLastNameIgnoreCase(lastName: String, pageable: Pageable): Page<Author>

    fun findByFirstNameAndLastNameAllIgnoreCase(
        firstName: String,
        lastName: String,
        pageable: Pageable
    ): Page<Author>

    fun findByFirstNameIgnoreCase(firstName: String, pageable: Pageable): Page<Author>
}


interface LoanRepository: PagingAndSortingRepository<Loan, Long> {
    fun findByUser_idAndBookCopy_barcode(userId: Long, barcode: Long): Loan?

    fun findByUser_id(userId: Long, pageable: Pageable): Page<Loan>

    fun findByDateDueBefore(date: LocalDateTime): MutableIterable<Loan>
}

