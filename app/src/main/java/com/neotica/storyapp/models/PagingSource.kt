package com.neotica.storyapp.models

import android.content.Context
import com.neotica.storyapp.retrofit.ApiService
import com.neotica.storyapp.retrofit.response.story.Story
import androidx.paging.PagingSource
import androidx.paging.PagingState

class PagingSource(
    private val apiService: ApiService,
    private val context: Context
) : PagingSource<Int, Story>() {
    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val position = params.key ?: 1
            val pageSize = params.loadSize
            val token = LoginPreferences(context).getToken()
            val response = apiService.getStory("Bearer $token", page = position, size = pageSize)

            val stories = response.body()?.listStory ?: emptyList()

            val prevKey = if (position == 1) null else position - 1
            val nextKey = if (stories.isEmpty()) null else position + 1

            LoadResult.Page(
                data = stories,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}