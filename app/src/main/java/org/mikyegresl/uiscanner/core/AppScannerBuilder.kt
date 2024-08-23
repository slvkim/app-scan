package org.mikyegresl.uiscanner.core

import android.app.Activity
import android.app.Application
import android.os.Bundle

object AppScannerBuilder : Application.ActivityLifecycleCallbacks {

    private val appScanner = AppScanner()

    fun inject(application: Application) {
        application.registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        appScanner.init(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        appScanner.doOnResume(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        appScanner.doOnPause()
    }

    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
}