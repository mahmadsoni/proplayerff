package com.ffpro.settings.i18n

enum class Language(val code: String, val nativeName: String) {
    ENGLISH("en", "English"),
    TAJIK("tg", "Тоҷикӣ"),
    UZBEK("uz", "O'zbekcha");

    companion object {
        fun fromCode(code: String): Language = entries.find { it.code == code } ?: ENGLISH
    }
}
