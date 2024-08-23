package org.mikyegresl.appscanner.feature.screen

import kotlinx.coroutines.flow.StateFlow
import org.mikyegresl.appscanner.domain.scanner.ScannerRepository
import org.mikyegresl.appscanner.domain.scanner.ViewData
import org.mikyegresl.appscanner.domain.scanner.ViewType

internal class ComposeScannerRepository: ScannerRepository {
    override val viewDataState: StateFlow<Set<ViewData>>
        get() = TODO("Not yet implemented")

    override val supportedViewTypes: Set<ViewType>
        get() = TODO("Not yet implemented")

    override fun updateViews(viewData: ViewData) {
        TODO("Not yet implemented")
    }


}