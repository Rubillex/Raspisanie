package dev.prokrostinatorbl.raspisanie.new_version.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.drm.DrmStore.Playback.START
import android.os.Build
import android.support.v4.media.session.PlaybackStateCompat
import android.telephony.ServiceState
import android.widget.Toast
import kotlinx.coroutines.flow.SharingCommand

class BootCompleteReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {



        val action = intent.action

        if (action.equals("android.intent.action.BOOT_COMPLETED", ignoreCase = true)){
            Toast.makeText(context, "REBOOT", Toast.LENGTH_SHORT).show()
            val service = Intent(context, TimeTableChecker::class.java)
            context?.startService(service)
        }

        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            val i = Intent(context, TimeTableChecker::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(i)
        }

    }
}