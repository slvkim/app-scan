package org.mikyegresl.appscanner.feature.screen

import android.content.res.Resources
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mikyegresl.appscanner.domain.user_action.PointerCoordinates
import org.mikyegresl.appscanner.domain.user_action.PointerId
import org.mikyegresl.appscanner.domain.user_action.UserActionRepository

internal class UserActionRepositoryImpl(private val resources: Resources): UserActionRepository {

    private val _userActions = MutableStateFlow(setOf<org.mikyegresl.appscanner.domain.scanner.UserAction>())
    override val userActions = _userActions.asStateFlow()

    private val touchedPointers = hashMapOf<PointerId, PointerCoordinates>()

    private val touchScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var touchDebounce: Job? = null

    private val onTouchListener = OnTouchListener { v, event ->
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN ->
                dispatchTouchEvent(v, event)
            MotionEvent.ACTION_UP -> {}
        }
        return@OnTouchListener when (v) {
            is Button, is EditText -> v.performClick()
            is TextView -> true
            else -> true
        }
    }

    override fun registerForUserActions(view: View) {
        view.setOnTouchListener(onTouchListener)
    }

    private fun dispatchTouchEvent(touchedView: View, event: MotionEvent) {
        touchDebounce?.cancel()
        for (i in 0..<event.pointerCount) {
            val pointerId = event.getPointerId(i)
            val (x: Float, y: Float) = event.findPointerIndex(pointerId).let { pointerIndex ->
                event.getX(pointerIndex) to event.getY(pointerIndex)
            }
            touchedPointers += PointerId(touchedView.id, pointerId) to PointerCoordinates(x, y)
        }
        val actions = touchedPointers.entries.map { entry ->
            org.mikyegresl.appscanner.domain.scanner.toUserAction(
                resources,
                entry.key.viewId,
                event,
                entry.value.x.toInt(),
                entry.value.y.toInt()
            )
        }.toSet()
        touchDebounce = touchScope.launch {
            delay(100)
            touchedPointers.clear()
            _userActions.update { actions }
        }
    }

    companion object {
        private const val TAG = "UserActionManager"
    }
}