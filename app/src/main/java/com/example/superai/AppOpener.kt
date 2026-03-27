package com.example.superai

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.tts.TextToSpeech

class AppOpener(
    private val context: Context,
    private val tts: TextToSpeech
) {

    // 🚀 OPEN ANY APP
    fun openAppByName(appName: String) {

        val cleanName = appName
            .replace("app", "")
            .replace("the", "")
            .replace("please", "")
            .trim()
            .lowercase()

        val pm: PackageManager = context.packageManager
        val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)

        for (app in apps) {
            val name = pm.getApplicationLabel(app).toString().lowercase()

            // 🔥 Strong matching
            if (name.contains(cleanName) || cleanName.contains(name)) {

                val intent = pm.getLaunchIntentForPackage(app.packageName)

                if (intent != null) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)

                    speak("Opening $name")
                    return
                }
            }
        }

        speak("App not found")
    }

    // 🔊 SPEAK FUNCTION
    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts1")
    }
}