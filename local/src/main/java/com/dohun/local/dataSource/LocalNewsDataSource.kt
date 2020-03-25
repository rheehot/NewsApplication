package com.dohun.local.dataSource

import com.dohun.local.entity.NewsEntity

interface LocalNewsDataSource {

    suspend fun getNewsList(): List<NewsEntity>

    suspend fun deleteNewsList()

    suspend fun insertNewsList(newsList: List<NewsEntity>)
}