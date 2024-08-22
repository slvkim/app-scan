package org.mikyegresl.uiscanner.data

import kotlinx.serialization.Serializable

@Serializable
class UiElementDto(
    val id: String,
    val type: String,
    val x: Int,
    val y: Int,
    val height: Int,
    val width: Int
)
