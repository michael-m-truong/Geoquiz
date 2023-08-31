package com.bignerdranch.android.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)

        // Set button click listeners
        trueButton.setOnClickListener { view: View ->
            // Do something in response to the "True" button click
        }

        falseButton.setOnClickListener { view: View ->
            // Do something in response to the "False" button click
        }
    }

}
