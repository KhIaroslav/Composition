package com.khiaroslav.composition.domain.repository

import com.khiaroslav.composition.domain.entity.GameSettings
import com.khiaroslav.composition.domain.entity.Level
import com.khiaroslav.composition.domain.entity.Question

interface GameRepository {
    fun generateQuestions(
        maxSumValue: Int,
        countOfOption: Int
    ): Question

    fun getGameSettings(lvl: Level): GameSettings
}