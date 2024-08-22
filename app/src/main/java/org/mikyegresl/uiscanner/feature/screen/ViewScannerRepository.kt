package org.mikyegresl.uiscanner.feature.screen

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.mikyegresl.uiscanner.domain.scanner.ScannerRepository
import org.mikyegresl.uiscanner.domain.scanner.ViewData
import org.mikyegresl.uiscanner.domain.scanner.ViewType

internal class ViewScannerRepository : ScannerRepository {

    private val _viewDataState = MutableStateFlow(setOf<ViewData>())
    override val viewDataState = _viewDataState.asStateFlow()

    override val supportedViewTypes: Set<ViewType> = ViewType.entries.toSet()

    override fun updateViews(viewData: ViewData) {
        _viewDataState.update { it + viewData }
    }

    companion object { private const val TAG = "XmlViewScanner" }
}