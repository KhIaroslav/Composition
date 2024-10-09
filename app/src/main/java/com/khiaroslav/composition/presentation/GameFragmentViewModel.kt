package com.khiaroslav.composition.presentation

import android.app.Application
import android.content.Context
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khiaroslav.composition.R
import com.khiaroslav.composition.data.GameRepositoryImpl
import com.khiaroslav.composition.domain.entity.GameResult
import com.khiaroslav.composition.domain.entity.GameSettings
import com.khiaroslav.composition.domain.entity.Level
import com.khiaroslav.composition.domain.entity.Question
import com.khiaroslav.composition.domain.uscases.GenerateQuestionsUseCase
import com.khiaroslav.composition.domain.uscases.GetGameSettingsUseCase
import java.util.Locale

class GameFragmentViewModel(
    private val application: Application,
    private val level: Level
) : ViewModel() {

    private val repository = GameRepositoryImpl
    private lateinit var gameSettings: GameSettings

    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)
    private val generateQuestionsUseCase = GenerateQuestionsUseCase(repository)

    //    private val context = application
//    private lateinit var level: Level
    private var timer: CountDownTimer? = null

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _percentOfRighrAnswers = MutableLiveData<Int>()
    val percentOfRighrAnswers: LiveData<Int>
        get() = _percentOfRighrAnswers

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

//    private val _shouldCloseScreen = MutableLiveData<GameResult>()
//    val close: LiveData<GameResult>
//        get() = _shouldCloseScreen

    private val _progressAnswer = MutableLiveData<String>()
    val progressAnswer: LiveData<String>
        get() = _progressAnswer

    private val _enoughCount = MutableLiveData<Boolean>()
    val enoughCount: LiveData<Boolean>
        get() = _enoughCount

    private val _enoughPercent = MutableLiveData<Boolean>()
    val enoughPercent: LiveData<Boolean>
        get() = _enoughPercent

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private var countOfRightAnswers = 0
    private var countOfQuestions = 0
//    private var percentOfRightAnswers = 0

    init {
        startGame()
    }

    private fun startGame() {
        getGameSettings()
        startTime()
        generateQuestions()
        updateProgress()
    }

    private fun getGameSettings() {
        gameSettings = getGameSettingsUseCase.invoke(level)
        _minPercent.value = gameSettings.minPercentOfRightAnswers
    }

    private fun startTime() {
        timer = object : CountDownTimer(
            gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            override fun onTick(millisUntilFinished: Long) {
                _formattedTime.value = secondsToTimeString(millisUntilFinished)
            }

            override fun onFinish() {
                finishGame()
                cancel()
            }
        }

        timer?.start()
    }

    fun chooseAnswer(num: Int) {
        checkAnswer(num)
        updateProgress()
        generateQuestions()
    }

    private fun updateProgress() {
        val percent = calculatePercentOfRightAnswers()
        _percentOfRighrAnswers.value = percent
        _progressAnswer.value = String.format(
            Locale.getDefault(),
            ContextCompat.getString(application, R.string.progress_answers),
            countOfRightAnswers,
            gameSettings.minCountOfRightAnswers
        )
        _enoughCount.value =
            countOfRightAnswers >= gameSettings.minCountOfRightAnswers
        _enoughPercent.value = percent >= gameSettings.minPercentOfRightAnswers
    }

    private fun calculatePercentOfRightAnswers(): Int {
        if (countOfRightAnswers == 0) return 0
        return ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }

    private fun checkAnswer(num: Int) {
        val rightsAnswer = _question.value?.rightAnswer
        if (num == rightsAnswer) {
            countOfRightAnswers++
        }
        countOfQuestions++
    }

    private fun generateQuestions() {
        _question.value = generateQuestionsUseCase(gameSettings.maxSumValue)
    }

    private fun secondsToTimeString(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val remainingSeconds = seconds % SECONDS_IN_MINUTES
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, remainingSeconds)
    }

    private fun finishGame() {
        _gameResult.value = GameResult(
            enoughCount.value == true && enoughPercent.value == true,
            countOfRightAnswers,
            countOfQuestions,
            gameSettings
        )
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {
        private const val MILLIS_IN_SECONDS: Long = 1000
        private const val SECONDS_IN_MINUTES: Long = 60
    }
}