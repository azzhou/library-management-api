package com.example.library.service

import com.example.library.entity.Author
import com.example.library.entity.Book
import com.example.library.entity.BookCopy
import com.example.library.exception.AuthorNotFoundException
import com.example.library.exception.BookCopyNotFoundException
import com.example.library.exception.BookNotFoundException
import com.example.library.repository.AuthorRepository
import com.example.library.repository.BookCopyRepository
import com.example.library.repository.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service


@Service
class BookCatalogServiceImpl @Autowired constructor(
    private val bookRepository: BookRepository,
    private val bookCopyRepository: BookCopyRepository,
    private val authorRepository: AuthorRepository
) : BookCatalogService {

    override fun getAllBooks(pageable: Pageable): Page<Book> {
        return bookRepository.findAll(pageable)
    }

    override fun getBookWithIsbn(isbn: String): Book {
        return bookRepository.findByIdOrNull(isbn) ?: throw BookNotFoundException(isbn)
    }

    override fun getBooksWithAuthorId(authorId: Long, pageable: Pageable): Page<Book> {
        authorRepository.findByIdOrNull(authorId) ?: throw AuthorNotFoundException(authorId)
        // Perform query on bookRepository instead of using nested list of books within the author entity
        // to allow for pagination and sorting for consistency with other methods
        return bookRepository.findByAuthors_id(authorId, pageable)
    }

    override fun getAllBookCopies(pageable: Pageable): Page<BookCopy> {
        return bookCopyRepository.findAll(pageable)
    }

    override fun getBookCopyWithBarcode(barcode: Long): BookCopy {
        return bookCopyRepository.findByIdOrNull(barcode) ?: throw BookCopyNotFoundException(barcode)
    }

    override fun getBookCopiesWithIsbn(isbn: String, pageable: Pageable): Page<BookCopy> {
        bookRepository.findByIdOrNull(isbn) ?: throw BookNotFoundException(isbn)
        return bookCopyRepository.findByBook_isbn(isbn, pageable)
    }

    override fun getAllAuthors(pageable: Pageable): Page<Author> {
        return authorRepository.findAll(pageable)
    }

    override fun getAuthorWithId(authorId: Long): Author {
        return authorRepository.findByIdOrNull(authorId) ?: throw AuthorNotFoundException(authorId)
    }

    override fun getAuthorsWithName(firstName: String?, lastName: String?, pageable: Pageable): Page<Author> {
        return if (firstName != null) {
            if (lastName != null) {
                authorRepository.findByFirstNameAndLastNameAllIgnoreCase(firstName, lastName, pageable)
            } else {
                authorRepository.findByFirstNameIgnoreCase(firstName, pageable)
            }
        } else if (lastName != null) {
            authorRepository.findByLastNameIgnoreCase(lastName, pageable)
        } else {
            getAllAuthors(pageable)
        }
    }

    override fun getBooksWithTitleContaining(title: String, pageable: Pageable): Page<Book> {
        return bookRepository.findByTitleIgnoreCaseContaining(title, pageable)
    }
}