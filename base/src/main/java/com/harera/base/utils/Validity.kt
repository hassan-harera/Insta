package com.harera.base.utils

class Validity {

    companion object {

        fun checkPhoneNumber(string: String) =
            false
                .or(string.matches(Regex("^010[0-9]{8}\$")))
                .or(string.matches(Regex("^011[0-9]{8}\$")))
                .or(string.matches(Regex("^012[0-9]{8}\$")))
                .or(string.matches(Regex("^015[0-9]{8}\$")))

        fun checkCode(string: String) =
            string.matches(Regex("^[0-9]{6}\$"))

        fun checkName(string: String) =
            string.matches(Regex("^[a-z]{5,15}\$"))

        fun checkDesc(string: String) =
            string.matches(Regex("^[a-z]{10,200}\$"))

        fun checkPassword(string: String) =
            string.matches(Regex("^[a-z]{8,25}\$"))

        fun checkCategoryName(value: String): Boolean =
            value.matches(Regex("^[a-z]{4,50}\$"))

        fun checkCodeValidity(code: String): Boolean =
            code.matches(Regex("^[0-9]{6}\$"))

        fun checkEmail(email: String): Boolean =
            email.matches(Regex("^[\\w]+@([\\w]+\\.)+[\\w]{2,4}\$"))
    }
}