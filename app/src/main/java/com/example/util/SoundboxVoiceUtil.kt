package com.example.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.speech.tts.TextToSpeech
import java.util.Locale

class SoundboxVoiceUtil(private val context: Context) : TextToSpeech.OnInitListener {

  private var tts: TextToSpeech? = null
  private var isInitialized = false

  init {
    try {
      tts = TextToSpeech(context.applicationContext, this)
    } catch (_: Exception) {}
  }

  override fun onInit(status: Int) {
    if (status == TextToSpeech.SUCCESS) {
      isInitialized = true
      try {
        val hindi = Locale("hi", "IN")
        val result = tts?.setLanguage(hindi)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
          tts?.setLanguage(Locale.ENGLISH)
        }
      } catch (_: Exception) {
        tts?.setLanguage(Locale.ENGLISH)
      }
    }
  }

  fun speakPaymentReceived(amount: Double, language: String = "HINDI") {
    triggerVibration()

    val amountInt = amount.toInt()
    val textToSpeak = if (language.equals("HINDI", ignoreCase = true)) {
      "फ़ोनपे पर ${amountInt} रुपये प्राप्त हुए"
    } else {
      "Payment of $amountInt rupees received on PhonePe"
    }

    if (isInitialized && tts != null) {
      try {
        if (language.equals("HINDI", ignoreCase = true)) {
          tts?.language = Locale("hi", "IN")
        } else {
          tts?.language = Locale.ENGLISH
        }
        tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, "phonepe_soundbox_${System.currentTimeMillis()}")
      } catch (_: Exception) {}
    }
  }

  private fun triggerVibration() {
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator?.vibrate(
          VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE)
        )
      } else {
        @Suppress("DEPRECATION")
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          vibrator?.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
          @Suppress("DEPRECATION")
          vibrator?.vibrate(200)
        }
      }
    } catch (_: Exception) {}
  }

  fun shutdown() {
    try {
      tts?.stop()
      tts?.shutdown()
      tts = null
    } catch (_: Exception) {}
  }
}
