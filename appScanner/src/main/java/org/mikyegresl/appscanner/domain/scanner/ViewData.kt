package org.mikyegresl.appscanner.domain.scanner

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import org.mikyegresl.appscanner.data.UiElementDto

internal data class ViewData(
    val id: String,
    val x: Int,
    val y: Int,
    val height: Int,
    val width: Int,
    val text: String,
    val textColor: String,
    val viewType: ViewType
) {
    //TODO: add another view types in future using sealed class
}

internal enum class ViewType {
    TEXT_VIEW,
    BUTTON,
    EDIT_TEXT,
    UNKNOWN;
    //TODO: add another viewTypes in future (e.g., Switch, RadioButton, etc)

    override fun toString(): String =
        when (this) {
            TEXT_VIEW -> "text_view"
            BUTTON -> "button"
            EDIT_TEXT -> "edit_text"
            UNKNOWN -> "view"
            //TODO: add another viewTypes in future (e.g., Switch, RadioButton, etc)
        }
}

internal fun View.mapToSupportedType(): ViewType =
    when (this) {
        is Button -> ViewType.BUTTON
        is EditText -> ViewType.EDIT_TEXT
        is TextView -> ViewType.TEXT_VIEW
        else -> ViewType.UNKNOWN
    }

internal fun ViewData.toUiElementDto(): UiElementDto =
    UiElementDto(
        id = id,
        type = viewType.toString(),
        x = x,
        y = y,
        height = height,
        width = width,
    )