package com.example.prjct_movmnt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prjct_movmnt.data.MovementReading
import com.example.prjct_movmnt.data.ReadingDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecordingViewModel(
    private val dao: ReadingDao
) : ViewModel() {

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _session = mutableListOf<MovementReading>()
    val session: List<MovementReading> get() = _session

    fun startSession() {
        _session.clear()
        _isRecording.value = true
    }

    fun stopSession() {
        _isRecording.value = false
    }

    fun onNewAngle(angle: Float) {
        if (!_isRecording.value) return

        val reading = MovementReading(
            timestamp = System.currentTimeMillis(),
            angle     = angle
        )
        _session += reading

        // Persist immediately
        viewModelScope.launch {
            dao.insert(reading)
        }
    }
}
