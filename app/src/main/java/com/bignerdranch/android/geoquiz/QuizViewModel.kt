package com.bignerdranch.android.geoquiz

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"

class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val questionBank = listOf(
        Question(R.string.question_basketball_1, true),
        Question(R.string.question_basketball_2, false),
        Question(R.string.question_basketball_3, false),
        Question(R.string.question_basketball_4, false),
        Question(R.string.question_basketball_5, true),
        Question(R.string.question_basketball_6, false))

    val answersCorrect = arrayOfNulls<Boolean?>(questionBank.size)

    private var currentIndex = 0

    private var correctAnswers = 0
    private var questionsAnswered = 0

    var isCheater: Boolean
        get() = savedStateHandle.get(IS_CHEATER_KEY) ?: false
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)

    var increaseCorrectAnswers: () -> Unit = {
        correctAnswers++
    }

    val currentCorrectQuestionsAnswered: Int
        get() = correctAnswers

    var increaseQuestionsAnswered: () -> Unit = {
        questionsAnswered++
    }

    val currentQuestionsAnswered: Int
        get() = questionsAnswered

    var setAnswer: (Boolean) -> Unit = { answer ->
        if (this.currentIndex in 0 until answersCorrect.size) {
            answersCorrect[currentIndex] = answer
        }
    }

    var getAnswer: () -> Boolean? = {
        answersCorrect[currentIndex]
    }

    var checkAnswered: () -> Boolean = {

        Log.d("AnswerStatus", "answersCorrect[i] is null: ${answersCorrect[this.currentIndex] == null}")

        if (answersCorrect[this.currentIndex] == null) {
            Log.d("GOOD","FALSE")
            false
        }
        else {
            true
        }

    }


    val currentQuestionIndex: Int
        get() = currentIndex

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val numOfQuestions: Int
        get() = questionBank.size

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev() {
        currentIndex = (currentIndex - 1) % questionBank.size
    }

}