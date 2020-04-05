package com.orange_infinity.gratitude.useCase

import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Environment
import java.io.File

class AudioController(
    private val listener: AudioCompleteListener? = null
) {

    private var mediaRecorder: MediaRecorder = MediaRecorder()
    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var fileName: String? = null

    fun recordStart(fileName: String) {
        releaseRecorder()
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

        mediaRecorder.setOutputFile(getMediaPath() + fileName)
        mediaRecorder.prepare()
        mediaRecorder.start()
        this.fileName = fileName
    }

    fun recordStop() {
        mediaRecorder.stop()
    }

    fun releaseRecorder() {
        mediaRecorder.release()
        mediaRecorder = MediaRecorder()
    }

    fun startPlay(fileName: String) {
        releasePlayer()
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(getMediaPath() + fileName)
        mediaPlayer.prepare()
        mediaPlayer.start()

        mediaPlayer.setOnCompletionListener {
            listener?.onAudioComplete()
        }
    }

    fun stopPlay() {
        mediaPlayer.stop()
    }

    fun releasePlayer() {
        mediaRecorder.release()
        mediaRecorder = MediaRecorder()
    }

    fun isAudioExist(fileName: String): Boolean {
        return File(getMediaPath() + fileName).exists()
    }

    fun deleteAudio(fileName: String) {
        val file = File(getMediaPath() + fileName)
        if (file.exists()) {
            file.delete()
        }
    }

    private fun getMediaPath(): String {
        return "${Environment.getExternalStorageDirectory()}/"
    }
}