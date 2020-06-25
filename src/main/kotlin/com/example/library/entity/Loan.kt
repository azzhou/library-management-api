package com.example.library.entity

import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name="loans")
class Loan(
    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @OneToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    @JoinColumn(name = "barcode")
    val bookCopy: BookCopy,

    @Column(name = "date_borrowed")
    val dateBorrowed: LocalDateTime,

    @Column(name = "date_due")
    var dateDue: LocalDateTime,

    @Column(name = "num_renewals")
    var numRenewals: Int = 0,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
)
