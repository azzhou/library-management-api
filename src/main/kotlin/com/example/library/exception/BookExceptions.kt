package com.example.library.exception

import org.springframework.http.HttpStatus

class BookNotFoundException(isbn: String) : BasicException("Could not find book with ISBN-13 $isbn", HttpStatus.NOT_FOUND)

class BookAlreadyExistsException(isbn: String) : BasicException("The library already has a book with ISBN $isbn", HttpStatus.CONFLICT)

class AuthorNotFoundException(authorId: Long) : BasicException("Could not find author with id $authorId", HttpStatus.NOT_FOUND)

class BookCopyNotFoundException(barcode: Long) : BasicException("Could not find book copy with barcode $barcode", HttpStatus.NOT_FOUND)