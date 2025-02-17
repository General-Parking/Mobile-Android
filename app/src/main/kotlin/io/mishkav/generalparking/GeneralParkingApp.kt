package io.mishkav.generalparking

import android.app.Application
import android.util.Log
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import io.mishkav.generalparking.dagger.AppComponent
import io.mishkav.generalparking.dagger.DaggerAppComponent
import timber.log.Timber

class GeneralParkingApp : Application() {

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .context(this)
            .build()
        //logging
        val formatStrategy = PrettyFormatStrategy
            .newBuilder()
            .showThreadInfo(true)
            .methodCount(1)
            .methodOffset(5)
            .tag("")
            .build()

        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))

        if (BuildConfig.DEBUG) {
            Timber.plant(initTimberDebugTree())
            Log.DEBUG
        } else {
            Log.WARN
        }
    }

    private fun initTimberDebugTree() = object : Timber.DebugTree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            Logger.log(priority, "-$tag", message, t)
        }
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}