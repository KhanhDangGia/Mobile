package com.example.ghi_chu

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder


class NoteService : Service() {

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): NoteService = this@NoteService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    fun countWords(note: String): Int =
        note.trim().split(Regex("\\s+")).count { it.isNotEmpty() }
}
