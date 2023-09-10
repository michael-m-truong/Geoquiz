package com.bignerdranch.android.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.bignerdranch.android.geoquiz.databinding.ActivityMainBinding
import com.bignerdranch.android.geoquiz.databinding.TrueFalseButtonsBinding

class MainActivity : AppCompatActivity() {

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

        // Initialize views
        //trueButton = findViewById(R.id.true_button)
        //falseButton = findViewById(R.id.false_button)

        // Set button click listeners

        //binding.root.addView(includedBinding.root)

        // Set button click listeners for the included layout components
        includedBinding.trueButton.setOnClickListener { view: View ->
            Toast.makeText(
                this,
                R.string.correct_toast,
                Toast.LENGTH_SHORT
            ).show()
        }

        includedBinding.falseButton.setOnClickListener { view: View ->
            Toast.makeText(
                this,
                R.string.correct_toast,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}
