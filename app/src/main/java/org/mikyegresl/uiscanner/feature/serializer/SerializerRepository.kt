package org.mikyegresl.uiscanner.feature.serializer

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.mikyegresl.uiscanner.feature.screen.ScannerInteractor
import org.mikyegresl.uiscanner.feature.screen.toScannerDataDto

internal class SerializerRepository(

) {

    fun serialize(screenState: ScannerInteractor.ScreenState): String =
        Json.encodeToString(screenState.toScannerDataDto())
}