package br.goldbach.activities

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.goldbach.activities.data.dao.ActivitiesDao
import br.goldbach.activities.data.db.ActivitiesDatabase
import br.goldbach.activities.data.model.Activity
import br.goldbach.activities.data.model.ActivityStats
import br.goldbach.activities.data.model.ActivityStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ActivitiesDaoTest {

    private lateinit var activitiesDao: ActivitiesDao
    private lateinit var activitiesDatabase: ActivitiesDatabase
    private var activities: List<Activity> = listOf(
        Activity(1, "Created1-Title", "Created1-Desc", ActivityStatus.CREATED),
        Activity(2, "Created2-Title", "Created2-Desc", ActivityStatus.CREATED),
        Activity(3, "Ongoing1-Title", "Ongoing1-Desc", ActivityStatus.ONGOING),
        Activity(4, "Ongoing2-Title", "Ongoing2-Desc", ActivityStatus.ONGOING),
        Activity(5, "Done1-Title", "Done1-Desc", ActivityStatus.DONE),
        Activity(6, "Done2-Title", "Done2-Desc", ActivityStatus.DONE)
    )

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        activitiesDatabase = Room.inMemoryDatabaseBuilder(context, ActivitiesDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        activitiesDao = activitiesDatabase.ActivitiesDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        activitiesDatabase.close()
    }

    private suspend fun addOneActivityToDb() {
        activitiesDao.insertActivity(activities[0])
    }

    private suspend fun addAllActivitiesToDb() {
        for(activity in activities) {
            activitiesDao.insertActivity(activity)
        }
    }


    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsCreatedActivityIntoDb() = runBlocking {
        addOneActivityToDb()
        val allActivities = activitiesDao.getCreatedActivities().first()
        assertEquals(allActivities[0], activities[0])
    }


    @Test
    @Throws(Exception::class)
    fun daoGetActivityById_returnsActivityByIdFromDb() = runBlocking {
        addOneActivityToDb()
        val activityActual = activitiesDao.getActivityById(activities[0].id.toString()).first()
        assertEquals(activities[0], activityActual)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllCreatedActivities_returnsAllCreatedActivitiesFromDb() = runBlocking {
        addAllActivitiesToDb()
        val allActivities = activitiesDao.getCreatedActivities().first()
        assertEquals(allActivities[0], activities[0])
        assertEquals(allActivities[1], activities[1])
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllOngoingActivities_returnsAllOngoingActivitiesFromDb() = runBlocking {
        addAllActivitiesToDb()
        val allOngoingActivities = activitiesDao.getOngoingActivities().first()
        assertEquals(allOngoingActivities[0], activities[2])
        assertEquals(allOngoingActivities[1], activities[3])
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllDoneActivities_returnsAllDoneActivitiesFromDb() = runBlocking {
        addAllActivitiesToDb()
        val allDoneActivities = activitiesDao.getDoneActivities().first()
        assertEquals(allDoneActivities[0], activities[4])
        assertEquals(allDoneActivities[1], activities[5])
    }

    @Test
    @Throws(Exception::class)
    fun daoGetCountOfActivities_returnCountOfActivitiesFromDb() = runBlocking {
        addAllActivitiesToDb()
        val countOfActivities = activitiesDao.getCountOfActivities().first()
        assertEquals(countOfActivities[0], ActivityStats(ActivityStatus.CREATED, 2))
        assertEquals(countOfActivities[1], ActivityStats(ActivityStatus.DONE, 2))
        assertEquals(countOfActivities[2], ActivityStats(ActivityStatus.ONGOING, 2))
    }

    @Test
    @Throws(Exception::class)
    fun daoDelete_DeleteActivityFromDb() = runBlocking {
        addAllActivitiesToDb()
        activitiesDao.deleteActivity(activities[0])
        val allActivities = activitiesDao.getCreatedActivities().first()
        assertEquals(allActivities[0], activities[1])
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdate_UpdateActivityFromDb() = runBlocking {
        val activity = Activity(1, "Activity1-Title", "Activity1-Desc", ActivityStatus.CREATED)
        activitiesDao.insertActivity(activity)
        activity.status = ActivityStatus.ONGOING
        activitiesDao.updateActivity(activity)
        val activityResult = activitiesDao.getActivityById(activity.id.toString()).first()
        assertEquals(activityResult, activity)
    }

}
