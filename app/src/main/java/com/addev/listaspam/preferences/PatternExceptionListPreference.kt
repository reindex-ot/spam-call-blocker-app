package com.addev.listaspam.preferences

import android.content.Context
import android.util.AttributeSet
import com.addev.listaspam.R

class PatternExceptionListPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.preference.R.attr.dialogPreferenceStyle,
    defStyleRes: Int = 0
) : BaseListManagerPreference(context, attrs, defStyleAttr, defStyleRes) {

    override val validator = object : BaseListValidator() {
        override fun validate(input: String): Boolean {
            val minLen = 2
            val maxLen = 20
            val patterns = cleanInput(input)
            if (patterns.isEmpty()) return false
            for (pattern in patterns) {
                if (pattern.length < minLen || pattern.length > maxLen) return false
                if (!pattern.matches(Regex("^\\+?[0-9*]+$"))) return false
                if (pattern.contains("**")) return false
            }
            return true
        }
    }

    override val errorMessageResId = R.string.pref_pattern_exception_list_error
    override val hintResId = R.string.pref_pattern_exception_list_hint
}
