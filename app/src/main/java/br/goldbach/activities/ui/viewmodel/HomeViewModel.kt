package br.goldbach.activities.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.goldbach.activities.data.model.Activity
import br.goldbach.activities.data.model.ActivityStats
import br.goldbach.activities.data.model.ActivityStatus
import br.goldbach.activities.data.model.User
import br.goldbach.activities.data.repository.ActivitiesRepository
import br.goldbach.activities.data.repository.UserRepository
import br.goldbach.activities.ui.fragment.EditUserDialogFragment
import br.goldbach.activities.ui.fragment.HomeFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface ActivityUiState {
    data class Success(
        val activities: List<Activity> = listOf(),
        val activitiesStats: List<ActivityStats> = listOf()
    ) : ActivityUiState

    data class Error(
        val message: String = ""
    ) : ActivityUiState
    data object Loading : ActivityUiState
}


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val activitiesRepository: ActivitiesRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private var _activitiesUiState = MutableLiveData<ActivityUiState>()
    val activitiesUiState: LiveData<ActivityUiState> get() = _activitiesUiState

    private var _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private var _createdStatus = MutableLiveData<Exception?>()
    val createdStatus: LiveData<Exception?> get() = _createdStatus

    init {
        getUser()
    }

    fun getUser() {
        viewModelScope.launch {
            try {
                userRepository.getCurrentUser().collect {
                    _user.postValue(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getActivityStats() {
        viewModelScope.launch {
            _activitiesUiState.postValue(ActivityUiState.Loading)
            try {
                activitiesRepository.getCountOfActivities().collect {
                    _activitiesUiState.postValue(ActivityUiState.Success(activitiesStats = it))
                }
            } catch (e: Exception) {
                _activitiesUiState.postValue(ActivityUiState.Error(e.message ?: "Error in API call"))
            }
        }
    }

    fun insertActivity(title: String, description: String) {
        viewModelScope.launch {
            try {
                activitiesRepository.insertActivity(Activity(0, title, description, ActivityStatus.CREATED))
                _createdStatus.postValue(null)
            } catch (e: Exception) {
                _createdStatus.postValue(e)
            }
        }
    }

    fun deleteActivity(activity: Activity) {
        viewModelScope.launch {
            try {
                activitiesRepository.deleteActivity(activity)
            } catch (e: Exception) {
                throw RuntimeException(e.message, e)
            }
        }
    }

    fun changeActivityStatus(activity: Activity) {
        viewModelScope.launch {
            try {
                activitiesRepository.updateActivity(activity)
            } catch (e: Exception) {
                throw RuntimeException(e.message, e)
            }
        }
    }

    fun getCreatedActivities() {
        viewModelScope.launch {
            _activitiesUiState.postValue(ActivityUiState.Loading)
            try {
                activitiesRepository.getCreatedActivities().collect {
                    _activitiesUiState.postValue(ActivityUiState.Success(it))
                }
            } catch (e: Exception) {
                _activitiesUiState.postValue(ActivityUiState.Error(e.message ?: "Error in API call"))
            }
        }
    }

    fun getOngoingActivities() {
        viewModelScope.launch {
            _activitiesUiState.postValue(ActivityUiState.Loading)
            try {
                activitiesRepository.getOngoingActivities().collect {
                    _activitiesUiState.postValue(ActivityUiState.Success(it))
                }
            } catch (e: Exception) {
                _activitiesUiState.postValue(ActivityUiState.Error(e.message ?: "Error in API call"))
            }
        }
    }

    fun getDoneActivities() {
        viewModelScope.launch {
            _activitiesUiState.postValue(ActivityUiState.Loading)
            try {
                activitiesRepository.getDoneActivities().collect {
                    _activitiesUiState.postValue(ActivityUiState.Success(it))
                }
            } catch (e: Exception) {
                _activitiesUiState.postValue(ActivityUiState.Error(e.message ?: "Problem in API call"))
            }
        }
    }

    fun updateUser(name: String, imageUri: Uri) {
        viewModelScope.launch {
            try {
                userRepository.updateCurrentUser(User(name, imageUri.toString()))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



}