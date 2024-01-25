package rs.ac.bg.etf.todolist.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import rs.ac.bg.etf.todolist.ui.stateholders.ObligationViewModel

class ObligationBroadcastReceiver(
    private val obligationViewModel: ObligationViewModel
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (
            intent?.action == Intent.ACTION_SCREEN_ON ||
            intent?.action == Intent.ACTION_USER_PRESENT
            ) {
            obligationViewModel.notifyUserOfFirstUnfinishedObligationForToday(context!!)
        }
    }

}