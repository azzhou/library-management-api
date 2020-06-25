package com.example.library.entity

import javax.persistence.*

@Entity
@Table(name="authors")
class Author(
    @Column(name="first_name")
    var firstName: String,

    @Column(name="last_name")
    var lastName: String,

    @ManyToMany(
        cascade = [CascadeType.PERSIST, CascadeType.MERGE],
        fetch = FetchType.LAZY,
        mappedBy = "authors"
    )
    var books: MutableList<Book> = mutableListOf(),

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
)