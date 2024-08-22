package org.mikyegresl.uiscanner.domain.scanner

import kotlinx.coroutines.flow.StateFlow

internal interface ScannerRepository {

    val viewDataState: StateFlow<Set<ViewData>>

    val supportedViewTypes: Set<ViewType>

    fun updateViews(viewData: ViewData)
}