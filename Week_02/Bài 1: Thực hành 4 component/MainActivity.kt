package com.example.ghi_chu

import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.ghi_chu.ui.theme.Ghi_chuTheme
import java.io.File

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "NoteApp"
    }

    // ---------- 1.2 Service ----------
    private var noteService by mutableStateOf<NoteService?>(null)
    private var isBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            noteService = (service as? NoteService.LocalBinder)?.getService()
            Log.d(TAG, "Service đã kết nối")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            noteService = null
            Log.d(TAG, "Service bị ngắt kết nối")
        }
    }

    // ---------- 1.3 BroadcastReceiver ----------
    private val powerReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_POWER_CONNECTED) {
                Log.d(TAG, "Đã cắm sạc (ACTION_POWER_CONNECTED)")
                Toast.makeText(this@MainActivity, "Đã cắm sạc", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ---------- 1.1 Activity ----------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Ghi_chuTheme {
                var note by rememberSaveable { mutableStateOf("") }
                var wordCount by rememberSaveable { mutableStateOf<Int?>(null) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Ghi chú học tập",
                            style = MaterialTheme.typography.headlineSmall
                        )

                        OutlinedTextField(
                            value = note,
                            onValueChange = { note = it },
                            label = { Text("Nội dung ghi chú") },
                            minLines = 4,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            enabled = noteService != null,
                            onClick = {
                                wordCount = noteService?.countWords(note)
                            }
                        ) {
                            Text("Đếm số từ")
                        }

                        Text(
                            text = when {
                                noteService == null -> "Đang kết nối tới Service…"
                                wordCount == null -> "Kết quả: chưa đếm"
                                else -> "Số từ: $wordCount"
                            }
                        )

                        Button(onClick = { shareNote(note) }) {
                            Text("Chia sẻ ghi chú")
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        isBound = bindService(
            Intent(this, NoteService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
        // Nếu dòng log này ghi "false" -> Manifest thiếu hoặc sai khai báo <service>
        Log.d(TAG, "bindService trả về: $isBound")

        ContextCompat.registerReceiver(
            this,
            powerReceiver,
            IntentFilter(Intent.ACTION_POWER_CONNECTED),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    override fun onStop() {
        unregisterReceiver(powerReceiver)

        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
        noteService = null

        super.onStop()
    }

    // ---------- 1.4 FileProvider ----------
    private fun shareNote(note: String) {
        // Ghi nội dung vào cacheDir/shared/note.txt
        val directory = File(cacheDir, "shared").apply { mkdirs() }
        val file = File(directory, "note.txt")
        file.writeText(note)

        // Lấy content:// URI
        val uri = FileProvider.getUriForFile(this, "$packageName.fileprovider", file)

        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_STREAM, uri)
            clipData = ClipData.newRawUri("note", uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(sendIntent, "Chia sẻ ghi chú"))
    }
}
