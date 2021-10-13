package com.ku_stacks.ku_ring.data.api

import com.ku_stacks.ku_ring.data.api.response.TrackListResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ITunesClient @Inject constructor(
    private val iTunesService: ITunesService
) {

    fun fetchTrackList(
        term: String,
        entity: String,
        limit: Int,
        offset: Int
    ): Single<TrackListResponse> = iTunesService.fetchTrackList(term, entity, limit, offset)

}