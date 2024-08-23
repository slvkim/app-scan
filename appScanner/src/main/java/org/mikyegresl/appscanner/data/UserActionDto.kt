package org.mikyegresl.appscanner.data

import kotlinx.serialization.Serializable

@Serializable
class UserActionDto(
    val elementId: String,
    val actionType: String,
    val timestamp: Long,
    val touchCoordinates: org.mikyegresl.appscanner.data.TouchCoordinatesDto,
)

@Serializable
class TouchCoordinatesDto(
    val x: Int,
    val y: Int,
)