package com.vtoroychadaev.app2suffer

import android.app.ComponentCaller
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.toColorInt
import kotlin.random.Random

//intent.putExtra(label, content)

class MainActivity : AppCompatActivity() {
    override fun onResume() {
        super.onResume()
        val intent = Intent(this, ActivityA::class.java)
        startActivity(intent)
   }

}

class ActivityA : AppCompatActivity() {
    lateinit var backgroundView: ConstraintLayout
    val colorString = "#123ABC"
    var color: Int = 0
    lateinit var buttonOpenActivityB: Button
    lateinit var buttonGenerateColor: Button
    lateinit var editText: EditText
    var colorWasChanged: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val previousColor: Int? = savedInstanceState?.getInt("color")
        val previousInput: String? = savedInstanceState?.getString("userInput")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_a)

        backgroundView = findViewById(R.id.activity_a)
        buttonOpenActivityB = findViewById(R.id.buttonOpenB)
        buttonGenerateColor = findViewById(R.id.buttonGenerateColor)
        editText = findViewById(R.id.colorEditText)

        color = if (previousColor != null) {
            previousColor
        } else {
            colorString.toColorInt()
        }

        if (previousInput != null) {
            editText.setText(previousInput.toCharArray(), 0, previousInput.length)
        }

        try {
            backgroundView.setBackgroundColor(color)
        } catch(e: IllegalArgumentException) {
            //Do sth
        }
    }

    override fun onResume() {
        super.onResume()
        buttonOpenActivityB.setOnClickListener {
            val intent = Intent(this, ActivityB::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (colorWasChanged) {
                intent.putExtra("colorFromActivityA", color)
            }

            startActivity(intent)
        }

        buttonGenerateColor.setOnClickListener {
            try {
                val userText = editText.getText().toString()
                color = userText.toColorInt()
                backgroundView.setBackgroundColor(color)
                colorWasChanged = true
                editText.setText("".toCharArray(), 0, 0)
            } catch(e: Exception) {
                color = Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
                backgroundView.setBackgroundColor(color)
                colorWasChanged = true
                val hexColorString = String.format("#%06x", color)
                editText.setText(hexColorString.toCharArray(), 0, hexColorString.length)
            }
        }
    }

    override fun onRestart() {
        colorWasChanged = false
        super.onRestart()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("color", color)
        outState.putString("userInput", editText.getText().toString())
    }
}

class ActivityB : AppCompatActivity() {
    lateinit var backgroundView: ConstraintLayout
    lateinit var buttonOpenActivityC: Button
    val colorString = "#973CFC"
    var color = 0

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        val colorFromActivityA: Int = intent.getIntExtra("colorFromActivityA", 0)
        if (colorFromActivityA != 0) {
            color = colorFromActivityA
        } else {
            color = colorString.toColorInt()
            backgroundView.setBackgroundColor(color)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b)
        enableEdgeToEdge()
        backgroundView = findViewById(R.id.activity_b)
        buttonOpenActivityC = findViewById(R.id.buttonOpenC)

        val colorFromActivityA: Int = intent.getIntExtra("colorFromActivityA", 0)
        if (colorFromActivityA != 0) {
            color = colorFromActivityA
        } else {
            color = colorString.toColorInt()
        }

        try {
            backgroundView.setBackgroundColor(color)
        } catch(e: IllegalArgumentException) {
            //Do sth
        }
    }

    override fun onResume() {
        super.onResume()
        buttonOpenActivityC.setOnClickListener {
            val intent = Intent(this, ActivityC::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}

class ActivityC : AppCompatActivity() {
    lateinit var backgroundView: ConstraintLayout
    lateinit var buttonOpenActivityA: Button
    val colorString = "#127DEA"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_c)

        backgroundView = findViewById(R.id.activity_c)
        buttonOpenActivityA = findViewById(R.id.buttonOpenA)

        try {
            backgroundView.setBackgroundColor(colorString.toColorInt())
        } catch(e: IllegalArgumentException) {
            //Do sth
        }
    }

    override fun onResume() {
        super.onResume()

        buttonOpenActivityA.setOnClickListener {
            val intent = Intent(this, ActivityA::class.java)
            startActivity(intent)
        }
    }
}