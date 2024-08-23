package org.mikyegresl.appscanner.feature.serializer

import android.util.Log
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.mikyegresl.appscanner.data.ScannerDataDto
import org.mikyegresl.appscanner.domain.SerializerRepository
import org.mikyegresl.appscanner.feature.ScannerData
import org.mikyegresl.appscanner.feature.toScannerDataDto

internal class SerializerRepositoryImpl: SerializerRepository {

    override fun serialize(scannerData: ScannerData): ScannerDataDto = scannerData.toScannerDataDto()

    override fun serializeToJson(scannerData: ScannerData): String? {
        val dataDto = scannerData.toScannerDataDto()
        val json = runCatching { Json.encodeToString(dataDto) }.getOrNull()
        Log.e("SerializerRepository", "$json")
        return json
    }
}