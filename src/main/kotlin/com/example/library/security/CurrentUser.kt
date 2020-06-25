package com.example.library.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class CurrentUser(
    val id: Long,
    username: String,
    password: String,
    authorities: Collection<GrantedAuthority>
) : User(username, password, authorities) {}
