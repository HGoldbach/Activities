package br.goldbach.activities.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.goldbach.activities.data.model.Activity
import br.goldbach.activities.data.model.ActivityStats
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivitiesDao {
    @Query("SELECT * FROM activities WHERE status = 'CREATED' ORDER BY title ASC")
    fun getCreatedActivities() : Flow<List<Activity>>

    @Query("SELECT * FROM activities WHERE status = 'ONGOING' ORDER BY title ASC")
    fun getOngoingActivities() : Flow<List<Activity>>

    @Query("SELECT * FROM activities WHERE status = 'DONE' ORDER BY title ASC")
    fun getDoneActivities() : Flow<List<Activity>>

    @Query("SELECT status, count(*) as total FROM activities GROUP BY status")
    fun getCountOfActivities() : Flow<List<ActivityStats>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertActivity(activity: Activity)

    @Delete
    suspend fun deleteActivity(activity: Activity)

    @Update
    suspend fun updateActivity(activity: Activity)
}