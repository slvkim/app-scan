package org.mikyegresl.uiscanner

import android.app.Application
import org.mikyegresl.uiscanner.core.AppScanner

internal class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppScanner().inject(this)
    }
}