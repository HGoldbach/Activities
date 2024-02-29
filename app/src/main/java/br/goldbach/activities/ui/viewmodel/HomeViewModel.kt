package br.goldbach.activities.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.goldbach.activities.data.model.Activity
import br.goldbach.activities.data.model.ActivityStatus
import br.goldbach.activities.data.repository.ActivitiesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: ActivitiesRepository) : ViewModel() {

    private var _createdActivities = MutableLiveData<List<Activity>>()
    val createdActivities: LiveData<List<Activity>> get() = _createdActivities
    private var _ongoingActivities = MutableLiveData<List<Activity>>()
    val ongoingActivities: LiveData<List<Activity>> get() = _ongoingActivities
    private var _doneActivities = MutableLiveData<List<Activity>>()
    val doneActivities: LiveData<List<Activity>> get() = _doneActivities

    private var _status = MutableLiveData<String>()
    val status: LiveData<String?> get() = _status

    fun insertActivity(title: String, description: String) {
        viewModelScope.launch {
            try {
                repository.insertActivity(Activity(0,title, description, ActivityStatus.CREATED))
                _status.postValue("Created")
            } catch (e: Exception) {
                _status.postValue(e.message)
            }
        }
    }

    fun deleteActivity(activity: Activity) {
        viewModelScope.launch {
            try {
                repository.deleteActivity(activity)
                _status.postValue("Deleted")
            } catch (e: Exception) {
                _status.postValue(e.message)
            }
        }
    }

    fun changeActivityStatus(activity: Activity) {
        viewModelScope.launch {
            try {
                repository.updateActivity(activity)
                _status.postValue("Changed")
            } catch (e: Exception) {
                _status.postValue(e.message)
            }
        }
    }

    fun getCreatedActivities() {
        viewModelScope.launch {
            try {
                repository.getCreatedActivities().collect {
                    _createdActivities.postValue(it)
                }
            } catch (e: Exception) {
                throw RuntimeException(e.message, e)
            }
        }
    }

    fun getOngoingActivities() {
        viewModelScope.launch {
            try {
                repository.getOngoingActivities().collect {
                    _ongoingActivities.postValue(it)
                }
            } catch(e: Exception) {
                throw RuntimeException(e.message, e)
            }
        }
    }

    fun getDoneActivities() {
        viewModelScope.launch {
            try {
                repository.getDoneActivities().collect {
                    _doneActivities.postValue(it)
                }
            } catch (e: Exception) {
                throw RuntimeException(e.message, e)
            }
        }
    }


}