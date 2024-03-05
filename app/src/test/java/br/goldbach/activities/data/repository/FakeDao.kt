package br.goldbach.activities.data.repository

import br.goldbach.activities.data.dao.ActivitiesDao
import br.goldbach.activities.data.model.Activity
import br.goldbach.activities.data.model.ActivityStats
import br.goldbach.activities.data.model.ActivityStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeDao(private val activities: MutableList<Activity>? = mutableListOf()) : ActivitiesDao {
    override fun getCreatedActivities(): Flow<List<Activity>> {
        return flow {
            emit(activities?.filter {
                it.status == ActivityStatus.CREATED
            } ?: emptyList())
        }
    }

    override fun getOngoingActivities(): Flow<List<Activity>> {
        return flow {
            emit(activities?.filter {
                it.status == ActivityStatus.ONGOING
            } ?: emptyList())
        }
    }

    override fun getDoneActivities(): Flow<List<Activity>> {
        return flow {
            emit(activities?.filter {
                it.status == ActivityStatus.DONE
            } ?: emptyList() )
        }
    }

    override fun getCountOfActivities(): Flow<List<ActivityStats>> {
        TODO("Not yet implemented")
    }

    override fun getActivityById(id: String): Flow<Activity> {
        return flow {
            activities?.first {
                it.id.toString() == id
            }
        }
    }

    override suspend fun insertActivity(activity: Activity) {
        activities?.add(activity)
    }

    override suspend fun deleteActivity(activity: Activity) {
        if (activities?.contains(activity) == true) {
            activities.remove(activity)
        } else {
            return
        }
    }

    override suspend fun updateActivity(activity: Activity) {
        val result = activities?.first { it.id == activity.id }
        val index = activities?.indexOf(result)!!
        activities[index] = activity
    }
}