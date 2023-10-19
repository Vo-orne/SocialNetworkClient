package com.example.myprofile.presentation.utils

import java.util.Locale

object Parser {
    fun parsEmail(email: String): String {
        val substring = email.substring(0, email.indexOf('@')) // qwe.rty@d.asd -> qwe.rty
        val splittedEmail = substring.split('.') // qwe.rty -> [qwe, rty]

        if (splittedEmail.size == 1) {
            return splittedEmail[0]
        }
        val sb = StringBuilder()
        splittedEmail.forEach { it ->
            val word =
                it.replaceFirstChar {
                    if (it.isLowerCase())
                        it.titlecase(Locale.ROOT)
                    else
                        it.toString()
                }
            sb.append("$word ")
        }
        return sb.substring(0, sb.length - 1).toString()
    }
}