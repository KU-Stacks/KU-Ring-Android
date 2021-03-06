package com.ku_stacks.ku_ring.network

import com.ku_stacks.ku_ring.data.api.FeedbackService
import com.ku_stacks.ku_ring.data.api.request.FeedbackRequest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FeedbackServiceTest : ApiAbstract<FeedbackService>() {

    private lateinit var service: FeedbackService

    @Before
    fun initService() {
        service = createService(FeedbackService::class.java)
    }

    @Test
    fun `send Feedback Test`() {
        // given
        enqueueResponse("/DefaultResponse.json")

        // when
        val mockRequest = FeedbackRequest(
            token = "mockToken",
            content = "쿠링은 좋은 어플리케이션입니다."
        )
        val response = service.sendFeedback(mockRequest).blockingGet()
        mockWebServer.takeRequest()

        // then
        assertEquals(true, response.isSuccess)
        assertEquals("성공", response.resultMsg)
        assertEquals(201, response.resultCode)
    }
}