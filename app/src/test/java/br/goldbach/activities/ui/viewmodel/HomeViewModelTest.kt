package br.goldbach.activities.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.goldbach.activities.LiveDataTestUtil
import br.goldbach.activities.MainDispatcherRule
import br.goldbach.activities.data.model.Activity
import br.goldbach.activities.data.model.ActivityStats
import br.goldbach.activities.data.model.ActivityStatus
import br.goldbach.activities.data.model.User
import br.goldbach.activities.data.repository.ActivitiesRepository
import br.goldbach.activities.data.repository.UserRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.notNull

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var activitiesRepository: ActivitiesRepository

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = HomeViewModel(activitiesRepository, userRepository)
    }

    @Test
    fun `getUser should update user LiveData`() = runTest {
        // Given
        val mockUser = User("John Doe", "fake_uri")

        // When
        `when`(userRepository.getCurrentUser()).thenReturn(flowOf(mockUser))
        viewModel.getUser()

        // Then
        val observedUser = LiveDataTestUtil.getValue(viewModel.user)
        assertEquals(mockUser, observedUser)
    }

    @Test
    fun `getActivityStats should update activitiesUiState LiveData on error`() = runTest {
        // Given
        val errorMessage = "Error in API call"

        // When
        `when`(activitiesRepository.getCountOfActivities()).thenThrow(RuntimeException(errorMessage))
        viewModel.getActivityStats()

        // Then
        val observedUiState = LiveDataTestUtil.getValue(viewModel.activitiesUiState)
        assertNotNull(observedUiState)
        assertTrue(observedUiState is ActivityUiState.Error && observedUiState.message == errorMessage)
    }

    @Test
    fun `getActivityStats should update activitiesUiState LiveData on Success`() = runTest {
        // Given
        val activitiesStats: List<ActivityStats> = listOf(
            ActivityStats(ActivityStatus.CREATED, 5),
            ActivityStats(ActivityStatus.ONGOING, 4),
            ActivityStats(ActivityStatus.DONE, 6)
        )

        // When
        `when`(activitiesRepository.getCountOfActivities()).thenReturn(flowOf(activitiesStats))
        viewModel.getActivityStats()

        // Then
        val observedUiState = LiveDataTestUtil.getValue(viewModel.activitiesUiState)
        assertTrue(observedUiState is ActivityUiState.Success && observedUiState.activitiesStats == activitiesStats)
    }

    @Test
    fun `insertActivity should update _createdStatus LiveData on Success `() = runTest {
        // Given
        val title = "Title"
        val description = "Description"

        // When
        viewModel.insertActivity(title, description)

        // Then
        val observedStatus = LiveDataTestUtil.getValue(viewModel.createdStatus)
        assertEquals(null, observedStatus)
    }

    @Test
    fun `insertActivity should update _createdStatus LiveData on Error`() = runTest {
        // Given
        val title = "Title"
        val description = "Description"
        val errorMessage = "Error in API call"
        val activity = Activity(0, title, description, ActivityStatus.CREATED)

        // When
        `when`(activitiesRepository.insertActivity(activity)).thenThrow(
            RuntimeException(
                errorMessage
            )
        )
        viewModel.insertActivity(title, description)

        // Then
        val observedStatus = LiveDataTestUtil.getValue(viewModel.createdStatus)
        assertEquals(errorMessage, observedStatus?.message)
    }


    @Test
    fun `getCreatedActivities should update ActivitiesUiState on Success`() = runTest {
        // Given
        val createdActivities: List<Activity> = listOf(
            Activity(0, "Title 1", "Description - 1", ActivityStatus.CREATED),
            Activity(0, "Title 2", "Description - 3", ActivityStatus.CREATED),
            Activity(0, "Title 3", "Description - 3", ActivityStatus.CREATED),
        )

        // When
        `when`(activitiesRepository.getCreatedActivities()).thenReturn(flowOf(createdActivities))
        viewModel.getCreatedActivities()

        // Then
        val observedActivities = LiveDataTestUtil.getValue(viewModel.activitiesUiState)
        assertTrue(observedActivities is ActivityUiState.Success && observedActivities.activities == createdActivities)
    }

    @Test
    fun `getCreatedActivities should update ActivityUiState on Error`() = runTest {
        // Given
        val errorMessage = "Error in API call"

        // When
        `when`(activitiesRepository.getCreatedActivities()).thenThrow(RuntimeException(errorMessage))
        viewModel.getCreatedActivities()

        // Then
        val observedActivity = LiveDataTestUtil.getValue(viewModel.activitiesUiState)
        assertTrue(observedActivity is ActivityUiState.Error && observedActivity.message == errorMessage)
    }

    @Test
    fun `getOngoingActivities should update ActivitiesUiState on Success`() = runTest {
        // Given
        val ongoingActivities: List<Activity> = listOf(
            Activity(0, "Title 1", "Description - 1", ActivityStatus.ONGOING),
            Activity(0, "Title 2", "Description - 3", ActivityStatus.ONGOING),
            Activity(0, "Title 3", "Description - 3", ActivityStatus.ONGOING),
        )

        // When
        `when`(activitiesRepository.getOngoingActivities()).thenReturn(flowOf(ongoingActivities))
        viewModel.getOngoingActivities()

        // Then
        val observedActivities = LiveDataTestUtil.getValue(viewModel.activitiesUiState)
        assertTrue(observedActivities is ActivityUiState.Success && observedActivities.activities == ongoingActivities)
    }

    @Test
    fun `getOngoingActivities should update ActivityUiState on Error`() = runTest {
        // Given
        val errorMessage = "Error in API call"

        // When
        `when`(activitiesRepository.getOngoingActivities()).thenThrow(RuntimeException(errorMessage))
        viewModel.getOngoingActivities()

        // Then
        val observedActivity = LiveDataTestUtil.getValue(viewModel.activitiesUiState)
        assertTrue(observedActivity is ActivityUiState.Error && observedActivity.message == errorMessage)
    }

    @Test
    fun `getDoneActivities should update ActivitiesUiState on Success`() = runTest {
        // Given
        val doneActivities: List<Activity> = listOf(
            Activity(0, "Title 1", "Description - 1", ActivityStatus.DONE),
            Activity(0, "Title 2", "Description - 3", ActivityStatus.DONE),
            Activity(0, "Title 3", "Description - 3", ActivityStatus.DONE),
        )

        // When
        `when`(activitiesRepository.getDoneActivities()).thenReturn(flowOf(doneActivities))
        viewModel.getDoneActivities()

        // Then
        val observedActivities = LiveDataTestUtil.getValue(viewModel.activitiesUiState)
        assertTrue(observedActivities is ActivityUiState.Success && observedActivities.activities == doneActivities)
    }

    @Test
    fun `getDoneActivities should update ActivityUiState on Error`() = runTest {
        // Given
        val errorMessage = "Error in API call"

        // When
        `when`(activitiesRepository.getDoneActivities()).thenThrow(RuntimeException(errorMessage))
        viewModel.getDoneActivities()

        // Then
        val observedActivity = LiveDataTestUtil.getValue(viewModel.activitiesUiState)
        assertTrue(observedActivity is ActivityUiState.Error && observedActivity.message == errorMessage)
    }


}