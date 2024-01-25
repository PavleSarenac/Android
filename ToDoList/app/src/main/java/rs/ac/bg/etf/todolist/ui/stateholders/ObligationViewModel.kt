package rs.ac.bg.etf.todolist.ui.stateholders

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.ac.bg.etf.todolist.data.ObligationRepository
import rs.ac.bg.etf.todolist.data.retrofit.models.ObligationModelRetrofit
import rs.ac.bg.etf.todolist.data.room.ObligationModelRoom
import javax.inject.Inject
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import rs.ac.bg.etf.todolist.R
import java.text.SimpleDateFormat

const val SYNCHRONIZATION_PERIOD = 5000L

const val CHANNEL_ID = "VERBOSE_NOTIFICATION"
const val CHANNEL_NAME = "Verbose Obligation Notifications"
const val CHANNEL_DESCRIPTION = "Shows notifications whenever user uses the phone"
const val NOTIFICATION_TITLE = "Next unfinished obligation for today"
const val NOTIFICATION_ID = 1

data class UiState(
    val obligationFromLocalDatabase: ObligationModelRoom? = null,
    val obligationFromKtorServer: ObligationModelRetrofit? = null
)

@HiltViewModel
class ObligationViewModel @Inject constructor(
    private val obligationRepository: ObligationRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    val allObligations = obligationRepository.allObligations

    fun addNewObligation(obligation: ObligationModelRetrofit) =
        viewModelScope.launch(Dispatchers.IO) {
            obligationRepository.addNewObligationToKtorServer(obligation)
        }

    fun turnPeriodicSyncOn() =
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                val allObligationsKtor = obligationRepository.getAllObligationsFromKtorServer()
                var allObligationsRoom = listOf<ObligationModelRoom>()
                allObligationsKtor.forEach {
                    allObligationsRoom = allObligationsRoom.plusElement(
                        ObligationModelRoom(
                            id = it.id,
                            isDone = it.isDone,
                            informalName = it.informalName,
                            dateTime = it.dateTime
                        )
                    )
                }
                obligationRepository.addObligationsToLocalDatabase(allObligationsRoom)
                delay(SYNCHRONIZATION_PERIOD)
            }
        }

    fun getObligationFromLocalDatabase(obligationId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            val obligation = obligationRepository.getObligationFromLocalDatabase(obligationId)
            withContext(Dispatchers.Main) {
                _uiState.update { it.copy(obligationFromLocalDatabase = obligation) }
            }
        }

    fun getObligationFromKtorServer(obligationId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            val obligation = obligationRepository.getObligationFromKtorServer(obligationId)
            withContext(Dispatchers.Main) {
                _uiState.update { it.copy(obligationFromKtorServer = obligation) }
            }
        }

    fun markObligationAsDone(obligationId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            obligationRepository.markObligationAsDoneRoom(obligationId)
            withContext(Dispatchers.Main) {
                _uiState.update {
                    it.copy(
                        obligationFromLocalDatabase = _uiState.value.obligationFromLocalDatabase?.copy(isDone = true),
                        obligationFromKtorServer = _uiState.value.obligationFromKtorServer?.copy(isDone = true)
                    )
                }
            }
        }

    fun notifyUserOfFirstUnfinishedObligationForToday(context: Context) {
        val currentDateInMillis = System.currentTimeMillis()
        /*
        * TODO: ostala implementacija logike - dovuci iz baze sve neodradjene obaveze za danas, pa
        * TODO: ovde mogu da proverim koje su stupile na snagu i da samo uzmem prvu i
        * TODO: nju prikazem kroz notifikaciju; ako nema takvih, nista
        * */
        //showNotification(context, "poruka")
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(context: Context, message: String) {
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