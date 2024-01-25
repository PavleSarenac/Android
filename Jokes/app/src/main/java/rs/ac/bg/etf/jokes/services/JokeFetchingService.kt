package rs.ac.bg.etf.jokes.services

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import rs.ac.bg.etf.jokes.ui.stateholders.JokesViewModel
import rs.ac.bg.etf.jokes.ui.stateholders.UiState
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import rs.ac.bg.etf.jokes.R
import rs.ac.bg.etf.jokes.data.retrofit.models.Joke

const val CHANNEL_ID = "Jokes Notification Channel Id"
const val CHANNEL_NAME = "Jokes Notification Channel Name"
const val CHANNEL_DESCRIPTION = "Jokes Notification Channel Description"
const val NOTIFICATION_TITLE = "New joke"
const val NOTIFICATION_ID = 1

class JokeFetchingService : LifecycleService() {
    private val binder = LocalBinder()
    private var job: Job? = null

    inner class LocalBinder : Binder() {
        fun getService(): JokeFetchingService = this@JokeFetchingService
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return binder
    }

    fun startFetchingJokes(jokesViewModel: JokesViewModel, uiState: UiState) {
        job = lifecycle.coroutineScope.launch(Dispatchers.IO) {
            while (isActive) {
                showNotification(
                    this@JokeFetchingService,
                    jokesViewModel.getJokes()
                )
                delay(uiState.jokeShowingFrequencyInSeconds.toLong() * 1000)
            }
        }
    }

    fun stopFetchingJokes() {
        job?.cancel()
    }

    @SuppressLint("MissingPermission")
    fun showNotification(context: Context, jokes: List<Joke>) {
        var message = ""
        jokes.forEach {
            message += ((it.joke ?: ((it.setup + "\n" + it.delivery))) + "\n")
        }
        val notificationChannel =
            NotificationChannelCompat.Builder(CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_HIGH)
                .setName(CHANNEL_NAME)
                .setDescription(CHANNEL_DESCRIPTION).build()
        NotificationManagerCompat.from(context).createNotificationChannel(notificationChannel)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground).setContentTitle(NOTIFICATION_TITLE)
            .setContentText(message).setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0)).build()
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }
}