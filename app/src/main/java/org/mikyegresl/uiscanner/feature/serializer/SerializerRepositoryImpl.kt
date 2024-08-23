package org.mikyegresl.uiscanner.feature.serializer

import android.util.Log
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.mikyegresl.uiscanner.data.ScannerDataDto
import org.mikyegresl.uiscanner.domain.SerializerRepository
import org.mikyegresl.uiscanner.feature.ScannerData
import org.mikyegresl.uiscanner.feature.toScannerDataDto

internal class SerializerRepositoryImpl: SerializerRepository {

    override fun serialize(scannerData: ScannerData): ScannerDataDto {
        val dataDto = scannerData.toScannerDataDto()
        val json = runCatching { Json.encodeToString(dataDto) }.getOrNull()
        Log.e("SerializerRepository", "${json}")
        return dataDto
    }
}