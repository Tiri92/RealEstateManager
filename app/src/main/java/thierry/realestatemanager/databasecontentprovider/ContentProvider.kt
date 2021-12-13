package thierry.realestatemanager.databasecontentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import thierry.realestatemanager.database.dao.PropertyDao
import thierry.realestatemanager.model.Property

class ContentProvider : ContentProvider() {

    companion object {
        private const val AUTHORITY = "thierry.realestatemanager.databasecontentprovider"
        private val TABLE_NAME = Property::class.java.simpleName
        val URI_ITEM = Uri.parse("content://$AUTHORITY/$TABLE_NAME")!!
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface EntryPointForContentProvider {
        fun getPropertyDao(): PropertyDao
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?,
    ): Cursor {
        val app = context?.applicationContext ?: throw IllegalStateException()
        val hiltEntryPointForDao =
            EntryPointAccessors.fromApplication(app, EntryPointForContentProvider::class.java)
        val dao = hiltEntryPointForDao.getPropertyDao().getPropertyCursor()

        val cursor: Cursor = dao
        cursor.setNotificationUri(app.contentResolver, p0)

        return cursor
    }

    override fun getType(p0: Uri): String? {
        return null
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        return null
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        return 0
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        return 0
    }

}