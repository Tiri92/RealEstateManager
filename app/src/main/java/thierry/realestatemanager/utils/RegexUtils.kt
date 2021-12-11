package thierry.realestatemanager.utils

import android.text.Editable
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import thierry.realestatemanager.R

class RegexUtils {

    companion object {

        fun emptyTextVerification(textEditText: Editable?): String? {
            return when (textEditText.toString()) {
                "" -> "Field can't be empty"
                else -> return null
            }
        }

        fun validCityText(textEditText: Editable?): String? {

            if (textEditText!!.isEmpty()) {
                return "Field can't be empty"
            }
            if (textEditText.matches(regex = "[A-Z]".toRegex())) {
                return "Must end with an lowercase letter "
            }
            if (textEditText.matches(regex = ".*\\d.*".toRegex())) {
                return "Numbers are not allowed"
            }
            if (!textEditText.matches(regex = "[A-Z][a-z]+((\\s?[A-Z][a-z]+){1,2})?".toRegex())) {
                return "Must start with an uppercase letter "
            }
            return null
        }

        fun updateButtonState(
            saveButton: AppCompatButton,
            string1: Editable?,
            string2: Editable?,
            string3: Editable?,
            string4: Editable?,
            string5: Editable?,
            string6: Editable?,
            string7: Editable?,
            string8: Editable?,
            string9: Editable?,
        ) {
            if (string1.isNullOrEmpty() || string2.isNullOrEmpty() || string3.isNullOrEmpty()
                || string4.isNullOrEmpty() || string5.isNullOrEmpty() || string6.isNullOrEmpty()
                || string7.isNullOrEmpty() || string8.isNullOrEmpty() || string9.isNullOrEmpty()
            ) {
                saveButton.isEnabled = false
                saveButton.isClickable = false
                saveButton.setTextColor(ContextCompat.getColor(saveButton.context,
                    R.color.grayTransparent))
            } else {
                saveButton.isEnabled = true
                saveButton.isClickable = true
                saveButton.setTextColor(ContextCompat.getColor(saveButton.context, R.color.white))
            }
        }

    }

}