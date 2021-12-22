package thierry.realestatemanager.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.FileInputStream
import java.io.IOException

class MediaUtils {

    companion object {

        fun savePhotoToInternalMemory(
            fileName: String,
            fileDate: String,
            bitmap: Bitmap,
            context: Context,
        ): Boolean {
            return try {
                context.openFileOutput("$fileName$fileDate.jpg", Activity.MODE_PRIVATE)
                    .use { stream ->
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

        fun saveVideoToInternalMemory(
            fileName: String,
            fileDate: String,
            uri: Uri,
            context: Context,
        ): Boolean {
            return try {
                context.openFileOutput("$fileName$fileDate.mp4", Activity.MODE_PRIVATE)
                    .use { stream ->
                        val sourceFile =
                            FileInputStream(context.contentResolver.openFileDescriptor(uri,
                                "r")?.fileDescriptor)
                        val buf = ByteArray(1024)
                        var len: Int
                        while (sourceFile.read(buf).also { len = it } > 0) {
                            stream?.write(buf, 0, len)
                        }
                        stream?.flush()
                        stream?.close()
                    }
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }

    }

}