package br.goldbach.activities.data.repository

import br.goldbach.activities.data.model.Activity
import br.goldbach.activities.data.model.ActivityStats
import kotlinx.coroutines.flow.Flow


interface ActivitiesRepository {
    fun getCreatedActivities(): Flow<List<Activity>>
    fun getOngoingActivities(): Flow<List<Activity>>
    fun getDoneActivities(): Flow<List<Activity>>
    fun getCountOfActivities() : Flow<List<ActivityStats>>
    suspend fun insertActivity(activity: Activity)
    suspend fun deleteActivity(activity: Activity)
    suspend fun updateActivity(activity: Activity)
}