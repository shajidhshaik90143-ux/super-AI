package com.example.superai

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var appOpener: AppOpener   // ✅ connect AppOpener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSpeak = findViewById<Button>(R.id.btnSpeak)

        tts = TextToSpeech(this, this)

        // ✅ Initialize AppOpener
        appOpener = AppOpener(this, tts)

        btnSpeak.setOnClickListener {
            startVoiceInput()
        }
    }

    // 🎤 Voice Input
    private val speechLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == RESULT_OK && result.data != null) {
            val resultList = result.data!!
                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            val command = resultList?.get(0)?.lowercase(Locale.getDefault()) ?: ""

            processCommand(command)
        }
    }

    private fun startVoiceInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak command...")

        speechLauncher.launch(intent)
    }

    // 🧠 Process Voice Command
    private fun processCommand(command: String) {

        println("User said: $command")

        when {

            // 📱 OPEN APPS
            command.contains("open") ||
                    command.contains("start") ||
                    command.contains("launch") -> {

                val appName = command
                    .replace("open", "")
                    .replace("start", "")
                    .replace("launch", "")
                    .replace("please", "")
                    .replace("the", "")
                    .trim()

                appOpener.openAppByName(appName)
            }

            // 🕒 TIME
            command.contains("time") -> {
                val time = java.text.SimpleDateFormat("hh:mm a", Locale.getDefault())
                    .format(Date())
                speak("Current time is $time")
            }

            // 📅 DATE
            command.contains("date") -> {
                val date = java.text.SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                    .format(Date())
                speak("Today is $date")
            }

            // 🤖 GREETING
            command.contains("hello") || command.contains("hi") -> {
                speak("Hello, I am your AI assistant")
            }

            // 🌐 GOOGLE SEARCH
            command.contains("search") -> {
                val query = command.replace("search", "").trim()
                val intent = Intent(Intent.ACTION_WEB_SEARCH)
                intent.putExtra("query", query)
                startActivity(intent)
            }

            // ❓ GENERAL AI RESPONSE
            else -> {
                speak(getAIResponse(command))
            }
        }
    }
    private fun getAIResponse(command: String): String {

        return when {

            command.contains("your name") -> "I am your Super AI assistant"

            command.contains("who created you") -> "I was created by Shajidh"

            command.contains("how are you") -> "I am functioning perfectly"

            command.contains("what can you do") -> "I can open apps, answer questions, and assist you"

            else -> "Sorry, I don't know that yet, but I am learning"
        }
    }


    // 🔊 Text to Speech
    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts1")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.US
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}