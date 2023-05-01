package com.neotica.storyapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.neotica.storyapp.retrofit.response.story.Story
import com.neotica.storyapp.ui.adapter.MainPagingAdapter
import com.neotica.storyapp.ui.viewmodel.repository.MainRepository
import com.neotica.storyapp.utils.DataDummy
import com.neotica.storyapp.utils.MainDispatcherRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlinx.coroutines.flow.first

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storiesRepository: MainRepository

    @Test
    fun `when Get Story Should Not Null and Return Success`() = runTest {
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val data: PagingData<Story> = StoriesPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<Story>>()

        expectedStory.value = data
        Mockito.`when`(storiesRepository.getStoriesPaging()).thenReturn(expectedStory)

        val pagingViewModel = MainViewModel(storiesRepository)
        val actualStory: PagingData<Story> = runBlocking { pagingViewModel.getStoryPaging().asFlow().first() }

        val differ = AsyncPagingDataDiffer(
            diffCallback = MainPagingAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory, differ.snapshot())
    }

    @Test
    fun `when Get Story amount of data is matching as expected`() = runTest {
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val data: PagingData<Story> = StoriesPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<Story>>()

        expectedStory.value = data
        Mockito.`when`(storiesRepository.getStoriesPaging()).thenReturn(expectedStory)

        val pagingViewModel = MainViewModel(storiesRepository)
        val actualStory: PagingData<Story> = runBlocking { pagingViewModel.getStoryPaging().asFlow().first() }

        val differ = AsyncPagingDataDiffer(
            diffCallback = MainPagingAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
    }

    @Test
    fun `when Get Story returns first data successfully`() = runTest {
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val data: PagingData<Story> = StoriesPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<Story>>()

        expectedStory.value = data
        Mockito.`when`(storiesRepository.getStoriesPaging()).thenReturn(expectedStory)

        val pagingViewModel = MainViewModel(storiesRepository)
        val actualStory: PagingData<Story> = runBlocking { pagingViewModel.getStoryPaging().asFlow().first() }

        val differ = AsyncPagingDataDiffer(
            diffCallback = MainPagingAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Should Return Zero If No Data`() = runTest {
        val dummyStory = emptyList<Story>()
        val data: PagingData<Story> = StoriesPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<Story>>()

        expectedStory.value = data
        Mockito.`when`(storiesRepository.getStoriesPaging()).thenReturn(expectedStory)

        val pagingViewModel = MainViewModel(storiesRepository)
        val actualStory: PagingData<Story> = runBlocking { pagingViewModel.getStoryPaging().asFlow().first() }

        val differ = AsyncPagingDataDiffer(
            diffCallback = MainPagingAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(0, differ.snapshot().size)
    }

}

class StoriesPagingSource : PagingSource<Int, LiveData<List<Story>>>() {
    companion object {
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}