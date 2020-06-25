package com.example.library.service

import com.example.library.dto.AuthorRegistration
import com.example.library.dto.BookCopyRegistration
import com.example.library.dto.BookRegistration
import com.example.library.entity.Author
import com.example.library.entity.Book
import com.example.library.entity.BookCopy
import org.springframework.security.access.annotation.Secured

interface BookManagementService {

    @Secured("ROLE_ADMIN", "ROLE_LIBRARIAN")
    fun addBook(bookRegistration: BookRegistration): Book

    @Secured("ROLE_ADMIN", "ROLE_LIBRARIAN")
    fun addBookCopy(bookCopyRegistration: BookCopyRegistration): BookCopy

    @Secured("ROLE_ADMIN", "ROLE_LIBRARIAN")
    fun addAuthor(authorRegistration: AuthorRegistration): Author
}