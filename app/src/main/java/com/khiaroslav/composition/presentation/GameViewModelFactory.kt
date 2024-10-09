package com.khiaroslav.composition.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khiaroslav.composition.domain.entity.Level

class GameViewModelFactory(
    private val level: Level,
    private val application: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameFragmentViewModel::class.java)) {
            return GameFragmentViewModel(application, level) as T
        }
        throw RuntimeException("Unknown viewModel class $modelClass")
    }
}