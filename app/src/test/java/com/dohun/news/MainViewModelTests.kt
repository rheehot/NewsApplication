package com.dohun.news

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dohun.model.Result.Failure
import com.dohun.model.Result.Success
import com.dohun.model.repository.NewsRepository
import com.dohun.news.dummy.NewsModelDummy
import com.dohun.news.viewModel.MainViewModel
import com.jraska.livedata.test
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class MainViewModelTests {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    @Mock
    private lateinit var newsRepository: NewsRepository

    private lateinit var viewModel: MainViewModel

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        viewModel = MainViewModel(newsRepository)
    }

    @Test
    fun `initialLoad 성공 테스트`() = runBlockingTest {
        `when`(newsRepository.getNewsList()).thenReturn(Success(NewsModelDummy.newsList))

        viewModel.initialLoad()

        viewModel.newsList.test().assertValue(NewsModelDummy.newsList)
    }

    @Test
    fun `initialLoad 로컬 테스트`() = runBlockingTest {
        `when`(newsRepository.getNewsList()).thenReturn(Success(NewsModelDummy.newsList, isLocal = true))

        viewModel.initialLoad()

        viewModel.newsList.test().assertValue(NewsModelDummy.newsList)
        viewModel.retrySnackbarEvent.test().assertHasValue()
    }

    @Test
    fun `initialLoad 실패 테스트`() = runBlockingTest {
        `when`(newsRepository.getNewsList()).thenReturn(Failure(RuntimeException()))

        viewModel.initialLoad()

        viewModel.newsList.test().assertNoValue()
        viewModel.retrySnackbarEvent.test().assertHasValue()
    }
}