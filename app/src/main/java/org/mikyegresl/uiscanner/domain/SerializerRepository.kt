package org.mikyegresl.uiscanner.domain

import org.mikyegresl.uiscanner.data.ScannerDataDto
import org.mikyegresl.uiscanner.feature.ScannerData

internal interface SerializerRepository {
    fun serialize(scannerData: ScannerData): ScannerDataDto
}