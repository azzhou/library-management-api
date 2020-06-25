package com.example.library.entity

import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name = "books")
class Book(
    @Id
    val isbn: String,

    var title: String,
    var publisher: String,
    var language: String,

    // save() method of SimpleJpaRepository (default implementation of CrudRepository) invokes persist and merge
    // in the EntityManager, so need to cascade both of them for save to cascade
    @OneToMany(cascade = [CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY, mappedBy = "book")
    var bookCopies: MutableList<BookCopy> = mutableListOf(),

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    @JoinTable(
        name = "book_author",
        joinColumns = [JoinColumn(name = "book_isbn", referencedColumnName = "isbn")],
        inverseJoinColumns = [JoinColumn(name = "author_id", referencedColumnName = "id")]
    )
    var authors: MutableList<Author> = mutableListOf(),

    @Column(name = "date_added")
    val dateAdded: LocalDateTime = LocalDateTime.now()
)