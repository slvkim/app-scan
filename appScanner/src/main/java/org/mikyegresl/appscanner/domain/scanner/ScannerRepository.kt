package org.mikyegresl.appscanner.domain.scanner

import kotlinx.coroutines.flow.Flow

internal interface ScannerRepository {

    val viewDataState: Flow<Set<ViewData>>

    val supportedViewTypes: Set<ViewType>

    fun updateViews(viewData: ViewData)
}