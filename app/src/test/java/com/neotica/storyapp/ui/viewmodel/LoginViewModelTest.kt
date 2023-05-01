package com.neotica.storyapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.neotica.storyapp.models.ApiResult
import com.neotica.storyapp.retrofit.response.auth.UserLogin
import com.neotica.storyapp.ui.viewmodel.repository.LoginRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: LoginRepository

    @Test
    fun `when Get LoginResponse Should Not Null and Return Success`() {
        val expectedLoginResponse = UserLogin("martinus", "238478923789", "martinusID")
        val expectedApiResult = ApiResult.Success(expectedLoginResponse)
        val expectedLiveData = MutableLiveData<ApiResult<UserLogin>>()
        expectedLiveData.value = expectedApiResult

        `when`(repository.responseLogin).thenReturn(expectedLiveData)

        val viewModel = LoginViewModel(repository)
        viewModel.loginUser("martinus@neotica.com", "martinus")

        val actualLiveData = viewModel.responseLogin
        assertNotNull(actualLiveData.value)
        assertEquals(expectedLiveData.value, actualLiveData.value)
    }
}