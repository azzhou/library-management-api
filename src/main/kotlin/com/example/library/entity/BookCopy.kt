package com.example.library.entity

import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name = "book_copies")
class BookCopy(
    @Enumerated(EnumType.STRING)
    var format: BookFormat,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_isbn")
    var book: Book,

    // Not redundant with loan object since status includes PROCESSING and potentially other states in the future
    @Enumerated(EnumType.STRING)
    var status: BookStatus = BookStatus.AVAILABLE,

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "bookCopy", orphanRemoval = true)
    var loan: Loan? = null,

    @Column(name = "date_added")
    val dateAdded: LocalDateTime = LocalDateTime.now(),

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val barcode: Long = 0
)


// Enums used in the BookCopy entity

enum class BookFormat {
    HARDCOVER,
    PAPERBACK,
    AUDIO,
    EBOOK
}

enum class BookStatus {
    AVAILABLE,
    BORROWED,
    PROCESSING
}