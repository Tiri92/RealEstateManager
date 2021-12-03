package thierry.realestatemanager.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import java.io.IOException

class MediaUtils {

    companion object {
         fun savePhotoToInternalMemory(
            fileDate: String,
            fileName: String,
            bitmap: Bitmap,
            context: Context,
        ): Boolean {
            return try {
                context.openFileOutput("$fileName$fileDate.jpg", Activity.MODE_PRIVATE)
                    .use { stream ->

                        //compress photo
                        if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                            throw IOException("error compression")
                        }
                    }
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }
    }

}