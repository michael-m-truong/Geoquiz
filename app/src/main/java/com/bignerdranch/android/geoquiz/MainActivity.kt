package com.bignerdranch.android.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.bignerdranch.android.geoquiz.databinding.ActivityMainBinding
import com.bignerdranch.android.geoquiz.databinding.TrueFalseButtonsBinding

class MainActivity : AppCompatActivity() {
    private var correctAnswers = 0
    private var questionsAnswered = 0
    private lateinit var binding: ActivityMainBinding
    private lateinit var includedBinding: TrueFalseButtonsBinding

//    private lateinit var trueButton: Button
//    private lateinit var falseButton: Button

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))
    private var currentIndex = 0

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
            currentIndex = (currentIndex - 1) % questionBank.size
            val questionTextResId = questionBank[currentIndex].textResId
            binding.questionTextView.setText(questionTextResId)
            updateNavButtons()
        }

        binding.nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            val questionTextResId = questionBank[currentIndex].textResId
            binding.questionTextView.setText(questionTextResId)
            updateNavButtons()
        }

        val questionTextResId = questionBank[currentIndex].textResId
        binding.questionTextView.setText(questionTextResId)

        updateNavButtons()
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer
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

        if (questionsAnswered == questionBank.size) {
            val score = (correctAnswers.toDouble() / questionBank.size) * 100
            Toast.makeText(this, "Quiz Score: ${correctAnswers} / ${questionBank.size}, ${String.format("%.2f", score)}%", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateNavButtons() {
        val questionTextResId = questionBank[currentIndex].textResId
        binding.questionTextView.setText(questionTextResId)

        // Set the visibility of the "Prev" button based on whether it's the first question
        binding.prevButton.visibility = if (currentIndex == 0) View.INVISIBLE else View.VISIBLE

        // Set the visibility of the "Next" button based on whether it's the last question
        binding.nextButton.visibility = if (currentIndex == questionBank.size - 1) View.INVISIBLE else View.VISIBLE
    }

}
