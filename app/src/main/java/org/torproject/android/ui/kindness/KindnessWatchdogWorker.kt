package org.torproject.android.ui.kindness

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.torproject.android.Regionalization
import org.torproject.android.util.Prefs
import java.util.concurrent.TimeUnit

/**
 * Brings the Snowflake proxy service back when the system has killed it but the
 * user still wants it running (#1799, #1783). START_STICKY alone is not enough:
 * restarts get throttled and eventually abandoned, and some OEMs never deliver
 * them at all, which is how Kindness Mode ends up off until somebody notices.
 */
class KindnessWatchdogWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        if (!Prefs.beSnowflakeProxy) {
            // The user turned Kindness Mode off; nothing left to watch.
            cancel(applicationContext)
            return Result.success()
        }
        if (shouldRestart(
                wantsProxy = Prefs.beSnowflakeProxy,
                serviceRunning = SnowflakeProxyService.isRunning,
                regionBlocked = Regionalization.isKindnessModeDisabledForCountry(Prefs.bridgeCountry)
            )
        ) {
            try {
                SnowflakeProxyService.startSnowflakeProxyForegroundService(applicationContext)
            } catch (e: IllegalStateException) {
                // Background foreground-service starts can be denied on API 31+
                // when the app holds no exemption. The next boot, app open, or
                // watchdog run after the user grants one will pick it back up.
            }
        }
        return Result.success()
    }

    companion object {
        private const val WORK_NAME = "kindness_watchdog"

        fun shouldRestart(wantsProxy: Boolean, serviceRunning: Boolean, regionBlocked: Boolean) =
            wantsProxy && !serviceRunning && !regionBlocked

        fun schedule(context: Context) = WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            PeriodicWorkRequestBuilder<KindnessWatchdogWorker>(15, TimeUnit.MINUTES).build()
        )

        fun cancel(context: Context) =
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }
}
