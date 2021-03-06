package com.ku_stacks.ku_ring.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.ku_stacks.ku_ring.MockUtil.mockDefaultResponse
import com.ku_stacks.ku_ring.MockUtil.mockSubscribeListResponse
import com.ku_stacks.ku_ring.MockUtil.mockSubscribeRequest
import com.ku_stacks.ku_ring.SchedulersTestRule
import com.ku_stacks.ku_ring.data.api.NoticeClient
import com.ku_stacks.ku_ring.util.PreferenceUtil
import com.ku_stacks.ku_ring.util.WordConverter
import io.reactivex.rxjava3.core.Single
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [23])
class SubscribeRepositoryTest {

    private lateinit var repository: SubscribeRepository
    private val client: NoticeClient = Mockito.mock(NoticeClient::class.java)
    private lateinit var pref: PreferenceUtil

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val testSchedulersRule = SchedulersTestRule()

    @Before
    fun setup() {
        pref = PreferenceUtil(getApplicationContext())
        repository = SubscribeRepositoryImpl(client, pref)
    }

    @Test
    fun `fetch Subscription From Remote Test`() {
        // given
        val mockToken = "AAAAn6eQM_Y:APA91bES4rjrFwPY5i_Hz-kT0u32SzIUxreYm9qaQHZeYKGGV_BmHZNJhHvlDjyQA6LveNdxCVrwzsq78jgsnCw8OumbtM5L3cc17XgdqZ_dlpsPzR7TlJwBFTXRFLPst663IeX27sb0"
        val mockSubscribeList = mockSubscribeListResponse()

        Mockito.`when`(client.fetchSubscribe(mockToken)).thenReturn(Single.just(mockSubscribeList))
        val expected = mockSubscribeList.categoryList.map { category ->
            WordConverter.convertEnglishToKorean(category)
        }

        // when + then
        repository.fetchSubscriptionFromRemote(mockToken)
            .test()
            .assertNoErrors()
            .assertValue(expected)

        Mockito.verify(client, Mockito.atLeastOnce()).fetchSubscribe(mockToken)
    }

    @Test
    fun `save Subscription To Remote Test`() {
        // given
        val mockRequest = mockSubscribeRequest()
        val mockResponse = mockDefaultResponse()

        Mockito.`when`(client.saveSubscribe(mockRequest)).thenReturn(Single.just(mockResponse))

        // when
        repository.saveSubscriptionToRemote(mockRequest)

        // then
        Mockito.verify(client, times(1)).saveSubscribe(mockRequest)
        assertEquals(false, pref.firstRunFlag)
    }

    @Test
    fun `save Subscription To Local Test`() {
        // given
        val mockData = arrayListOf("??????", "?????????")

        // when
        repository.saveSubscriptionToLocal(mockData)

        // then
        val expected = arrayListOf("bch", "emp").toSet()
        assertEquals(expected, pref.subscription)
    }
}