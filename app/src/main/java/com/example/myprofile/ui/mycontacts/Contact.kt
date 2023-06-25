package com.example.myprofile.ui.mycontacts

import java.io.Serializable

data class Contact(     //todo package data/Model
    val avatar: String,
    val name: String,
    val career: String
): Serializable
