package thierry.realestatemanager

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ToDoApplication : Application() {

    companion object{
        lateinit var  instance:Application
            private set
    }
    init{
        instance = this
    }

}