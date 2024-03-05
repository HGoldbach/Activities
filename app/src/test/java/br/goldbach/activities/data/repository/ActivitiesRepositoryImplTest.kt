package br.goldbach.activities.data.repository

import br.goldbach.activities.data.model.Activity
import br.goldbach.activities.data.model.ActivityStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ActivitiesRepositoryImplTest {

    private val activity1 = Activity(1,"Title - 1", "Description - 1", ActivityStatus.CREATED)
    private val activity2 = Activity(2,"Title - 2", "Description - 2", ActivityStatus.CREATED)
    private val activity3 = Activity(3,"Title - 3", "Description - 3", ActivityStatus.CREATED)
    private val activity4 = Activity(4,"Title - 4", "Description - 4", ActivityStatus.ONGOING)
    private val activity5 = Activity(5,"Title - 5", "Description - 5", ActivityStatus.ONGOING)
    private val activity6 = Activity(6,"Title - 6", "Description - 6", ActivityStatus.DONE)
    private val activity7 = Activity(7,"Title - 7", "Description - 7", ActivityStatus.DONE)

    private val createdActivities = listOf(
        activity1, activity2, activity3
    )

    private val ongoingActivities = listOf(
        activity4, activity5
    )

    private val doneActivities = listOf(
        activity6, activity7
    )

    private val activities = listOf(
        activity1,
        activity2,
        activity3,
        activity4,
        activity5,
        activity6,
        activity7,
    )

    private lateinit var dao: FakeDao
    private lateinit var activitiesRepository: ActivitiesRepository

    @Before
    fun createRepository() {
        dao = FakeDao(activities.toMutableList())
        activitiesRepository = ActivitiesRepositoryImpl(dao)
    }

    @Test
    fun getCreatedActivities_requestAllCreatedActivitiesFromDb() = runTest {
        val result = activitiesRepository.getCreatedActivities().first()
        assertEquals(createdActivities, result)
    }

    @Test
    fun getOngoingActivities_requestAllOngoingActivitiesFromDb() = runTest {
        val result = activitiesRepository.getOngoingActivities().first()
        assertEquals(ongoingActivities, result)
    }

    @Test
    fun getDoneActivities_requestAllDoneActivitiesFromDb() = runTest {
        val result = activitiesRepository.getDoneActivities().first()
        assertEquals(doneActivities, result)
    }

    @Test
    fun addActivity_insertActivityIntoDb() = runTest {
        val activity = Activity(10, "Title - 10", "Description - 10", ActivityStatus.CREATED)
        activitiesRepository.insertActivity(activity)
        val result = activitiesRepository.getCreatedActivities().first()
        assertEquals(createdActivities.size + 1, result.size)
    }

    @Test
    fun removeActivity_deleteActivityFromDb() = runTest {
        activitiesRepository.deleteActivity(ongoingActivities[0])
        val result = activitiesRepository.getOngoingActivities().first()
        assertEquals(ongoingActivities.size - 1, result.size)
    }

    @Test
    fun updateStatusActivity_updateActivityFromDb() = runTest {
        ongoingActivities[0].status = ActivityStatus.DONE
        activitiesRepository.updateActivity(ongoingActivities[0])
        val resultOngoing = activitiesRepository.getOngoingActivities().first()
        val resultDone = activitiesRepository.getDoneActivities().first()

        assertEquals(1, resultOngoing.size)
        assertEquals(3, resultDone.size)
    }

}