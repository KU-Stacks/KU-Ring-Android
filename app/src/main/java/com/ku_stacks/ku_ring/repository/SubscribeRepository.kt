package com.ku_stacks.ku_ring.repository

import com.ku_stacks.ku_ring.data.api.request.SubscribeRequest
import com.ku_stacks.ku_ring.data.api.response.DefaultResponse
import io.reactivex.rxjava3.core.Single

interface SubscribeRepository {
    fun fetchSubscriptionFromRemote(token: String): Single<List<String>>
    fun saveSubscriptionToRemote(subscribeRequest: SubscribeRequest)
    fun getSubscriptionFromLocal(): Set<String>
    fun saveSubscriptionToLocal(stringArray: ArrayList<String>)
}