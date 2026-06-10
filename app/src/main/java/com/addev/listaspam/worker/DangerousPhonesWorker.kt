package com.addev.listaspam.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.addev.listaspam.util.ApiUtils
import com.addev.listaspam.util.getListaSpamApiLang
import com.addev.listaspam.util.getUnknownPhoneApiKey
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class DangerousPhonesWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val apiKey = getUnknownPhoneApiKey(context) ?: return Result.success()
        val lang = getListaSpamApiLang(context) ?: Locale.getDefault().country.uppercase()
        ApiUtils.updateDangerousPhonesList(context, apiKey, lang, "premium_auto")
        return Result.success()
    }

    companion object {
        private const val WORK_NAME = "dangerous_phones_daily"

        fun schedule(context: Context) {
            val now = Calendar.getInstance()
            val target = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 2)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                if (before(now)) add(Calendar.DAY_OF_YEAR, 1)
            }
            val initialDelayMs = target.timeInMillis - now.timeInMillis

            val request = PeriodicWorkRequestBuilder<DangerousPhonesWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(initialDelayMs, TimeUnit.MILLISECONDS)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }
}
