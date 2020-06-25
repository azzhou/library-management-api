package com.example.library.entity

import com.example.library.security.AuthenticationDetails
import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name="users")
class User(
    @Column(unique=true)
    var email: String,

    @Column(name="first_name")
    var firstName: String,

    @Column(name="last_name")
    var lastName: String,

    @Column(name="password")
    var password: String,

    @Enumerated(EnumType.STRING)
    var permission: UserPermission = UserPermission.MEMBER,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    var loans: MutableSet<Loan> = mutableSetOf(),

    @Column(name="fine_balance_cents")
    var fineBalanceCents: Long = 0,

    @Column(name="date_created")
    val dateCreated: LocalDateTime = LocalDateTime.now(),

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
)

data class UserAuthentication(
    val user: User,
    val authentication: AuthenticationDetails
)


// Enums used in the User entity

// Simple representation of permissions stored directly in the User entity
// In a more complex system, replace with Roles and Privileges that have their own tables
enum class UserPermission {
    MEMBER,
    LIBRARIAN,
    ADMIN
}