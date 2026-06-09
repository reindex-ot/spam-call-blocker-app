package com.addev.listaspam.preferences

import android.content.Context
import android.util.AttributeSet
import com.addev.listaspam.R
import com.addev.listaspam.util.getBlockedNumbers
import com.addev.listaspam.util.setBlockedNumbers

class BlockedNumberListPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.preference.R.attr.dialogPreferenceStyle,
    defStyleRes: Int = 0
) : BaseListManagerPreference(context, attrs, defStyleAttr, defStyleRes) {

    init {
        isPersistent = false
    }

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

    override val errorMessageResId = R.string.pref_blocked_numbers_list_error
    override val hintResId = R.string.pref_blocked_numbers_list_hint

    override fun getEntries(): List<String> = getBlockedNumbers(context).toList().sorted()

    override fun saveEntries(entries: List<String>) {
        setBlockedNumbers(context, entries.toSet())
        summary = buildSummary(entries)
    }
}
