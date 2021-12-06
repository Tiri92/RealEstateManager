package thierry.realestatemanager.utils

import android.text.Editable

class RegexUtils {

    companion object {

        fun validPriceText(textEditText: Editable?): String? {
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

    }

}