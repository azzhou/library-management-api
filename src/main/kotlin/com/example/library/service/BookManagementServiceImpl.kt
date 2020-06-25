package com.example.library.service

import com.example.library.dto.AuthorRegistration
import com.example.library.dto.BookCopyRegistration
import com.example.library.dto.BookRegistration
import com.example.library.dto.asEntity
import com.example.library.entity.*
import com.example.library.exception.AuthorNotFoundException
import com.example.library.exception.BookCopyNotFoundException
import com.example.library.exception.BookAlreadyExistsException
import com.example.library.exception.BookNotFoundException
import com.example.library.repository.AuthorRepository
import com.example.library.repository.BookCopyRepository
import com.example.library.repository.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class BookManagementServiceImpl @Autowired constructor(
    private val bookRepository: BookRepository,
    private val bookCopyRepository: BookCopyRepository,
    private val authorRepository: AuthorRepository
) : BookManagementService {

    override fun addBook(bookRegistration: BookRegistration): Book {
        val isbn = bookRegistration.isbn
        if (bookRepository.findByIdOrNull(isbn) != null) {
            throw BookAlreadyExistsException(isbn)
        }

        // Extension function includes basic fields
        val bookEntity = bookRegistration.asEntity()

        // Add authors separately since they require db lookup
        for (authorId in bookRegistration.authorIds) {
            val authorEntity = authorRepository.findByIdOrNull(authorId) ?: throw AuthorNotFoundException(authorId)
            bookEntity.authors.add(authorEntity)
            authorEntity.books.add(bookEntity)
        }

        bookRepository.save(bookEntity)
        return bookEntity
    }

    override fun addBookCopy(bookCopyRegistration: BookCopyRegistration): BookCopy {
        val book = bookRepository.findByIdOrNull(bookCopyRegistration.isbn)
            ?: throw BookNotFoundException(bookCopyRegistration.isbn)
        val bookCopy = BookCopy(
            format = BookFormat.valueOf(bookCopyRegistration.format),
            book = book,
            status = BookStatus.valueOf(bookCopyRegistration.status)
        )
        book.bookCopies.add(bookCopy)
        bookRepository.save(book)
        return bookCopy
    }

    override fun addAuthor(authorRegistration: AuthorRegistration): Author {
        val author = authorRegistration.asEntity()
        authorRepository.save(author)
        return author
    }
}