package com.khiaroslav.composition.domain.uscases

import com.khiaroslav.composition.domain.entity.Question
import com.khiaroslav.composition.domain.repository.GameRepository

class GenerateQuestionsUseCase(private val repository: GameRepository) {

    operator fun invoke(maxSumValue: Int): Question {
        return repository.generateQuestions(maxSumValue, COUNT_OF_OPTIONS)
    }

    private companion object {
        private const val COUNT_OF_OPTIONS = 6
    }
}