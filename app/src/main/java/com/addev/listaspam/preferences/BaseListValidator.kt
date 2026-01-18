package com.addev.listaspam.preferences

import android.text.TextUtils

abstract class BaseListValidator {
    fun cleanInput(input: String): List<String> {
        if (TextUtils.isEmpty(input) || input.isBlank()) return emptyList()
        return input
            .lineSequence()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toList()
    }

    abstract fun validate(input: String): Boolean
}
