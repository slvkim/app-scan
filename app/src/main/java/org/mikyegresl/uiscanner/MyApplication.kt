package org.mikyegresl.uiscanner

import android.app.Application
import org.mikyegresl.uiscanner.core.AppScannerBuilder

internal class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppScannerBuilder.inject(this)
    }
}