package rs.ac.bg.etf.todolist

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import rs.ac.bg.etf.todolist.broadcastreceivers.ObligationBroadcastReceiver
import rs.ac.bg.etf.todolist.ui.elements.ToDoApp
import rs.ac.bg.etf.todolist.ui.elements.theme.ToDoListTheme
import rs.ac.bg.etf.todolist.ui.stateholders.ObligationViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var broadcastReceiver: ObligationBroadcastReceiver
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val obligationViewModel: ObligationViewModel by viewModels()
        broadcastReceiver = ObligationBroadcastReceiver(obligationViewModel)
        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_USER_PRESENT)
        }
        ContextCompat.registerReceiver(
            this,
            broadcastReceiver,
            intentFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(POST_NOTIFICATIONS)
            }
        }

        setContent {
            ToDoListTheme {
                ToDoApp(obligationViewModel)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }
}