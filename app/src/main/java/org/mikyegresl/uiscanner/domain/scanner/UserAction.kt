package org.mikyegresl.uiscanner.domain.scanner

import android.content.res.Resources
import android.view.MotionEvent

data class UserAction(
    val elementId: String,
    val actionType: UserActionType,
    val timestamp: Long,
    val touchCoordinates: TouchCoordinates,
)

data class TouchCoordinates(
    val x: Int,
    val y: Int,
)

enum class UserActionType {
    CLICK,
    //TODO: add other user actions
}

//TODO: map event.actionMasked to business logic event
internal fun toUserAction(resources: Resources, viewId: Int, event: MotionEvent, x: Int, y: Int): UserAction =
    UserAction(
        elementId = resources.getResourceName(viewId),
        actionType = UserActionType.CLICK,
        timestamp = event.downTime,
        touchCoordinates = TouchCoordinates(x, y),
    )