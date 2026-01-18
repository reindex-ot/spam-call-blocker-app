package com.addev.listaspam.preferences

import android.content.Context
import android.util.AttributeSet
import android.widget.Toast
import androidx.preference.EditTextPreference
import com.addev.listaspam.R

abstract class BaseEditTextPreference(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.preference.R.attr.editTextPreferenceStyle,
    defStyleRes: Int = 0
) : EditTextPreference(context, attrs, defStyleAttr, defStyleRes) {

    protected abstract val validator: BaseListValidator
    protected abstract val errorMessageResId: Int

    init {
        setOnPreferenceChangeListener { preference, newValue ->
            val input = newValue as String

            if (!validator.validate(input)) {
                Toast.makeText(
                    context,
                    errorMessageResId,
                    Toast.LENGTH_LONG
                ).show()
                return@setOnPreferenceChangeListener false
            }

            // Cleanup and normalization
            val cleaned = validator.cleanInput(input)
            val joined = cleaned.joinToString("\n")

            // Manually persist the cleaned value
            preference as EditTextPreference
            preference.text = joined

            // Return false so Android doesn't automatically persist the value
            false
        }
    }
}