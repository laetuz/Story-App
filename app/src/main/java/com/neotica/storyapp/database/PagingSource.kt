package com.neotica.storyapp.database

import android.content.Context
import com.neotica.storyapp.retrofit.ApiService
import com.neotica.storyapp.ui.response.Story
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.neotica.storyapp.models.LoginPreferences

class PagingSource(
    private val apiService: ApiService,
    private val context: Context
) : PagingSource<Int, Story>() {
    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1)?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val position = params.key ?: 1
            val pageSize = params.loadSize
            val token = LoginPreferences(context).getToken()

            // Make the network request
            val response = apiService.getStory("Bearer $token", page = position, size = pageSize)

            // Extract the results from the response
            val stories = response.body()?.listStory ?: emptyList()

            // Calculate the previous and next keys
            val prevKey = if (position == 1) null else position - 1
            val nextKey = if (stories.isEmpty()) null else position + 1

            LoadResult.Page(
                data = stories,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            // Handle errors
            LoadResult.Error(e)
        }
    }

    private companion object{
        const val INITIAL_PAGE_INDEX = 1
    }
}