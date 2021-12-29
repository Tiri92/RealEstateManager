package thierry.realestatemanager.utils

import android.text.Editable
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import thierry.realestatemanager.R
import java.util.*

class RegexUtils {

    companion object {

        fun emptyTextVerification(textEditText: Editable?): String? {
            if (Locale.getDefault().displayLanguage.equals("français")) {
                return when (textEditText.toString()) {
                    "" -> "Le champ ne peut pas être vide"
                    else -> return null
                }
            } else {
                return when (textEditText.toString()) {
                    "" -> "Field can't be empty"
                    else -> return null
                }
            }
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