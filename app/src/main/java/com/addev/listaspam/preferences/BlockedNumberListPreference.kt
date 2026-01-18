package com.addev.listaspam.preferences

import android.content.Context
import android.util.AttributeSet
import androidx.preference.EditTextPreference
import com.addev.listaspam.R

class BlockedNumberListPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.preference.R.attr.editTextPreferenceStyle,
    defStyleRes: Int = 0
) : BaseEditTextPreference(context, attrs, defStyleAttr, defStyleRes) {

    override val validator = object : BaseListValidator() {
        override fun validate(input: String): Boolean {
            val minLen = 2
            val maxLen = 20
            val numbers = cleanInput(input)
            if (numbers.isEmpty()) return false
            for (number in numbers) {
                if (number.length < minLen || number.length > maxLen) return false
                if (!number.matches(Regex("^\\+?[0-9]+$"))) return false
            }
            return true
        }
    }

    override val errorMessageResId = R.string.pref_pattern_exception_list_error
}
