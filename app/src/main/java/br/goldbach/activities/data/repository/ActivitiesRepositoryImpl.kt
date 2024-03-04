package br.goldbach.activities.data.repository

import br.goldbach.activities.data.dao.ActivitiesDao
import br.goldbach.activities.data.model.Activity
import br.goldbach.activities.data.model.ActivityStats
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject



class ActivitiesRepositoryImpl @Inject constructor(private val activitiesDao: ActivitiesDao) : ActivitiesRepository {
    override fun getCreatedActivities(): Flow<List<Activity>> {
        return activitiesDao.getCreatedActivities()
    }

    override fun getOngoingActivities(): Flow<List<Activity>> {
        return activitiesDao.getOngoingActivities()
    }

    override fun getDoneActivities(): Flow<List<Activity>> {
        return activitiesDao.getDoneActivities()
    }

    override fun getCountOfActivities(): Flow<List<ActivityStats>> {
        return activitiesDao.getCountOfActivities()
    }

    override suspend fun insertActivity(activity: Activity) {
        activitiesDao.insertActivity(activity)
    }

    override suspend fun deleteActivity(activity: Activity) {
        activitiesDao.deleteActivity(activity)
    }

    override suspend fun updateActivity(activity: Activity) {
        activitiesDao.updateActivity(activity)
    }
}