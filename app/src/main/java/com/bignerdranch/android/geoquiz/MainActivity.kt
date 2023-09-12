package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.bignerdranch.android.geoquiz.databinding.ActivityMainBinding
import com.bignerdranch.android.geoquiz.databinding.TrueFalseButtonsBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var includedBinding: TrueFalseButtonsBinding

    private val quizViewModel: QuizViewModel by viewModels()

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater =
                result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        includedBinding = TrueFalseButtonsBinding.bind(binding.root)

        // Set button click listeners for the included layout components
        includedBinding.trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
            quizViewModel.increaseQuestionsAnswered()
            checkIfQuizFinished()
        }

        includedBinding.falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
            quizViewModel.increaseQuestionsAnswered()
            checkIfQuizFinished()
        }

        binding.prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            val questionTextResId = quizViewModel.currentQuestionText
            binding.questionTextView.setText(questionTextResId)
            checkQuestionAnswered()
            updateNavButtons()
        }

        binding.nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            val questionTextResId = quizViewModel.currentQuestionText
            binding.questionTextView.setText(questionTextResId)
            checkQuestionAnswered()
            updateNavButtons()
        }

        binding.cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            cheatLauncher.launch(intent)
        }

        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)

        checkQuestionAnswered()
        updateNavButtons()
    }

    private fun checkQuestionAnswered() {
        if (quizViewModel.checkAnswered() == true) {
            includedBinding.trueButton.visibility = View.INVISIBLE
            includedBinding.falseButton.visibility = View.INVISIBLE
            binding.questionAnswered.visibility = View.VISIBLE
            generateAnswerColor()
        }
        else {
//            includedBinding.root.visibility = View.VISIBLE
            includedBinding.trueButton.visibility = View.VISIBLE
            includedBinding.falseButton.visibility = View.VISIBLE
            binding.questionAnswered.visibility = View.INVISIBLE
        }

    }

    private fun generateAnswerColor() {
        val isAnswerCorrect = quizViewModel.getAnswer()

        // Get a reference to the TextView
        val textView = binding.questionAnswered

        if (quizViewModel.isCheater) {
            textView.setBackgroundColor(Color.RED)
            textView.text =getString(R.string.judgment_toast)
        }

        else if (isAnswerCorrect == true) {
            // If the answer is correct, set background color to green and change the text
            textView.setBackgroundColor(Color.parseColor("#39c678"))
            textView.text = getString(R.string.answered_correct)
        } else {
            // If the answer is incorrect, set background color to red and change the text
            textView.setBackgroundColor(Color.RED)
            textView.text = getString(R.string.answered_incorrect)
        }
    }



    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        if (userAnswer == correctAnswer) {
            quizViewModel.increaseCorrectAnswers()
            quizViewModel.setAnswer(true)
        }
        else {
            quizViewModel.setAnswer(false)
        }
        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()

        checkQuestionAnswered()
    }

    private fun checkIfQuizFinished() {

        val numOfQuestions = quizViewModel.numOfQuestions

        if (quizViewModel.currentQuestionsAnswered == numOfQuestions) {
            val score = (quizViewModel.currentCorrectQuestionsAnswered.toDouble() / numOfQuestions) * 100
            Toast.makeText(this, "Quiz Score: ${quizViewModel.currentCorrectQuestionsAnswered} / ${numOfQuestions}, ${String.format("%.2f", score)}%", Toast.LENGTH_SHORT).show()
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
