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
    val story_3: Boolean = false,
    val story_4: Boolean = false,
    val fav_story_1: Boolean = false,
    val fav_story_2: Boolean = false,
    )