package org.mikyegresl.appscanner.feature.display

import android.util.DisplayMetrics
import org.mikyegresl.appscanner.domain.display.DisplayRepository

internal class DisplayRepositoryImpl(private val displayMetrics: DisplayMetrics): DisplayRepository {
    override val screenHeight get() = displayMetrics.heightPixels
    override val screenWidth get() = displayMetrics.widthPixels
}