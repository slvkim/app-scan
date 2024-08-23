package org.mikyegresl.appscanner.domain.user_action

import android.view.View
import kotlinx.coroutines.flow.Flow
import org.mikyegresl.appscanner.domain.scanner.UserAction

interface UserActionRepository {

    val userActions: Flow<Set<UserAction>>

    fun registerForUserActions(view: View)
}