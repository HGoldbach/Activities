package br.goldbach.activities.data.repository

import br.goldbach.activities.data.model.Activity
import kotlinx.coroutines.flow.Flow


interface ActivitiesRepository {
    fun getCreatedActivities(): Flow<List<Activity>>
    fun getOngoingActivities(): Flow<List<Activity>>
    fun getDoneActivities(): Flow<List<Activity>>
    suspend fun insertActivity(activity: Activity)
    suspend fun deleteActivity(activity: Activity)
    suspend fun updateActivity(activity: Activity)
}