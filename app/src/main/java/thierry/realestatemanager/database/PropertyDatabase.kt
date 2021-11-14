package thierry.realestatemanager.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import thierry.realestatemanager.database.dao.PropertyDao
import thierry.realestatemanager.di.ApplicationScope
import thierry.realestatemanager.model.Property
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Property::class], version = 1, exportSchema = false)
abstract class PropertyDatabase : RoomDatabase() {

    abstract fun propertyDao(): PropertyDao

    class Callback @Inject constructor(
        private val database: Provider<PropertyDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().propertyDao()

            applicationScope.launch {
                dao.insert(Property(price = "$17,870,000"))
                dao.insert(Property(price = "$8,430,000"))
                dao.insert(Property(price = "$41,650,000"))
            }
        }
    }

}