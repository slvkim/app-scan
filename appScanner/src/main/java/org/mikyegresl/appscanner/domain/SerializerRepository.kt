package org.mikyegresl.appscanner.domain

import org.mikyegresl.appscanner.data.ScannerDataDto
import org.mikyegresl.appscanner.feature.ScannerData

internal interface SerializerRepository {
    fun serialize(scannerData: ScannerData): ScannerDataDto
    fun serializeToJson(scannerData: ScannerData): String?
}