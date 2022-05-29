package com.example.carsapp.features.presentation.utils

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.work.*

fun observeConnection(context: Context): LiveData<WorkInfo> {
    val workManager: WorkManager = WorkManager.getInstance(context)
    val connectedConstrains : Constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
    val connectedInternetWorker: OneTimeWorkRequest = OneTimeWorkRequest.Builder(InternetWorker::class.java).setConstraints(
        connectedConstrains
    ).build()
    workManager.enqueue(connectedInternetWorker)
    return workManager.getWorkInfoByIdLiveData(connectedInternetWorker.id)
}