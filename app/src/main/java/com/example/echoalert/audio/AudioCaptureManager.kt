package com.example.echoalert.audio

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import androidx.core.content.ContextCompat
import java.io.File

class AudioCaptureManager(private val context: Context) {

    private var recorder: MediaRecorder? = null

    fun hasAudioPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun createOutputFile(): String {
        val file = File(
            context.cacheDir,
            "echoalert_${System.currentTimeMillis()}.mp4"
        )

        return file.absolutePath
    }

    fun startRecording(outputFile: String): Boolean {

        if (!hasAudioPermission()) {
            return false
        }

        return try {
            recorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(outputFile)

                prepare()
                start()
            }

            true

        } catch (e: Exception) {
            recorder?.release()
            recorder = null
            false
        }
    }

    fun stopRecording(): Boolean {

        return try {
            recorder?.apply {
                stop()
                release()
            }

            recorder = null
            true

        } catch (e: Exception) {
            recorder?.release()
            recorder = null
            false
        }
    }
}