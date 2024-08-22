package org.mikyegresl.uiscanner.domain.user_action

import android.view.View
import kotlinx.coroutines.flow.Flow
import org.mikyegresl.uiscanner.domain.scanner.UserAction

interface UserActionRepository {

    val userActions: Flow<Set<UserAction>>

    fun registerForUserActions(view: View)
}