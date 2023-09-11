package com.bignerdranch.android.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import com.bignerdranch.android.geoquiz.databinding.ActivityMainBinding
import com.bignerdranch.android.geoquiz.databinding.TrueFalseButtonsBinding

class MainActivity : AppCompatActivity() {
    private var correctAnswers = 0
    private var questionsAnswered = 0
    private lateinit var binding: ActivityMainBinding
    private lateinit var includedBinding: TrueFalseButtonsBinding

    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        includedBinding = TrueFalseButtonsBinding.bind(binding.root)

        // Set button click listeners for the included layout components
        includedBinding.trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
            this.questionsAnswered++
        }

        includedBinding.falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
            this.questionsAnswered++
        }

        binding.prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            val questionTextResId = quizViewModel.currentQuestionText
            binding.questionTextView.setText(questionTextResId)
            updateNavButtons()
        }

        binding.nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            val questionTextResId = quizViewModel.currentQuestionText
            binding.questionTextView.setText(questionTextResId)
            updateNavButtons()
        }

        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)

        updateNavButtons()
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        if (userAnswer == correctAnswer) {
            this.correctAnswers++
        }
        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()

        val numOfQuestions = quizViewModel.numOfQuestions

        if (questionsAnswered == numOfQuestions) {
            val score = (correctAnswers.toDouble() / numOfQuestions) * 100
            Toast.makeText(this, "Quiz Score: ${correctAnswers} / ${numOfQuestions}, ${String.format("%.2f", score)}%", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateNavButtons() {
        val questionTextResId = quizViewModel.currentQuestionText
        val currentQuestionIndex = quizViewModel.currentQuestionIndex
        val numOfQuestions = quizViewModel.numOfQuestions

        binding.questionTextView.setText(questionTextResId)

        // Set the visibility of the "Prev" button based on whether it's the first question
        binding.prevButton.visibility = if (currentQuestionIndex == 0) View.INVISIBLE else View.VISIBLE

        // Set the visibility of the "Next" button based on whether it's the last question
        binding.nextButton.visibility = if (currentQuestionIndex == numOfQuestions - 1) View.INVISIBLE else View.VISIBLE
    }

}
