package br.goldbach.activities.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activities")
data class Activity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    var id: Int,
    @ColumnInfo("title")
    var title: String,
    @ColumnInfo("description")
    var description: String,
    @ColumnInfo("status")
    var status: ActivityStatus
)
