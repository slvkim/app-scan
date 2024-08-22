package org.mikyegresl.uiscanner.domain.scanner

import android.view.View
import android.widget.Button
import android.widget.TextView

sealed class ViewData(
    open val id: String,
    open val x: Int,
    open val y: Int,
    open val height: Int,
    open val width: Int,
) {
    data class TextViewData(
        override val id: String,
        override val x: Int,
        override val y: Int,
        override val height: Int,
        override val width: Int,
//        val text: String,
//        val textColor: String,
    ) : ViewData(id, x, y, height, width)

    data class ButtonViewData(
        override val id: String,
        override val x: Int,
        override val y: Int,
        override val height: Int,
        override val width: Int,
    ) : ViewData(id, x, y, height, width)

    //TODO: add another view types in future
}

internal enum class ViewType {
    TEXT,
    BUTTON,
    UNKNOWN
    //TODO: add another viewTypes in future (e.g., Switch, RadioButton, etc)
}

internal fun View.mapToSupportedType(): ViewType =
    when (this) {
        is Button -> ViewType.BUTTON
        is TextView -> ViewType.TEXT
        else -> ViewType.UNKNOWN
    }