package com.bddi.doomo.model

data class User(
    val email: String = "",
    val name: String = "",
    val address: String = "",
    val sound: Boolean = true,
    val notification: Boolean = true,
    val image: String = "",
    val story_1: Boolean = false,
    val story_2: Boolean = false,
    )