package com.addev.listaspam.preferences

import android.content.Context
import android.util.AttributeSet
import androidx.preference.DialogPreference
import org.json.JSONArray

abstract class BaseListManagerPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.preference.R.attr.dialogPreferenceStyle,
    defStyleRes: Int = 0
) : DialogPreference(context, attrs, defStyleAttr, defStyleRes) {

    abstract val validator: BaseListValidator
    abstract val errorMessageResId: Int
    abstract val hintResId: Int

    open fun getEntries(): List<String> {
        val raw = getPersistedString("") ?: ""
        if (raw.isEmpty()) return emptyList()
        return try {
            val arr = JSONArray(raw)
            (0 until arr.length()).map { arr.getString(it) }.filter { it.isNotEmpty() }
        } catch (_: Exception) {
            // migrate legacy newline-separated format
            raw.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
        }
    }

    open fun saveEntries(entries: List<String>) {
        val arr = JSONArray().apply { entries.forEach { put(it) } }
        persistString(arr.toString())
        summary = buildSummary(entries)
    }

    protected fun buildSummary(entries: List<String>): String {
        return if (entries.isEmpty()) ""
        else entries.take(3).joinToString(", ") + if (entries.size > 3) " …" else ""
    }

    override fun onSetInitialValue(defaultValue: Any?) {
        summary = buildSummary(getEntries())
    }
}
