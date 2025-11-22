package com.example.myrecipepal

import android.app.Application
import com.example.myrecipepal.data.AppContainer
import com.example.myrecipepal.data.DefaultAppContainer

class MyRecipePalApplication : Application() {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        // Create the dependency container when the app starts
        container = DefaultAppContainer(this)
    }
}
