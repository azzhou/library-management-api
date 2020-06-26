package com.example.library.service

import com.example.library.entity.Author
import com.example.library.entity.Book
import com.example.library.entity.BookCopy
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface BookCatalogService {

    fun getAllBooks(pageable: Pageable): Page<Book>

    fun getBookWithIsbn(isbn: String): Book

    fun getBooksWithAuthorId(authorId: Long, pageable: Pageable): Page<Book>

    fun getAllBookCopies(pageable: Pageable): Page<BookCopy>

    fun getBookCopyWithBarcode(barcode: Long): BookCopy

    fun getBookCopiesWithIsbn(isbn: String, pageable: Pageable): Page<BookCopy>

    fun getAllAuthors(pageable: Pageable): Page<Author>

    fun getAuthorWithId(authorId: Long): Author

    fun getAuthorsWithName(firstName: String? = null, lastName: String? = null, pageable: Pageable): Page<Author>

    fun getBooksWithTitleContaining(title: String, pageable: Pageable): Page<Book>
}