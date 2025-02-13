package com.campus.rent.utils.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.campus.rent.database.api.PropertyApi
import com.campus.rent.database.model.property.getProperty.Data

class PropertyPagingSource(private val propertyApi: PropertyApi,private val latitude: Double,private  val longitude: Double) : PagingSource<Int, Data>() {
    override fun getRefreshKey(state: PagingState<Int, Data>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
        return try {

            val position = params.key ?: 1
            val response = propertyApi.getProperty(position,latitude,longitude)
            LoadResult.Page(
                data = response.data,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (position == response.totalPages) null else position + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}