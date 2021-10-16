package com.ku_stacks.ku_ring.data.api

import com.ku_stacks.ku_ring.data.api.response.NoticeListResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NoticeService {
    @GET("notice?")
    fun fetchNoticeList(
        @Query("type") type: String,
        @Query("offset") offset: Int,
        @Query("max") max: Int,
    ): Single<NoticeListResponse>
}