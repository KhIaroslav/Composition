package com.khiaroslav.composition.domain.uscases

import com.khiaroslav.composition.domain.entity.GameSettings
import com.khiaroslav.composition.domain.entity.Level
import com.khiaroslav.composition.domain.repository.GameRepository

class GetGameSettingsUseCase(
    private val repository: GameRepository
) {

    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }
}