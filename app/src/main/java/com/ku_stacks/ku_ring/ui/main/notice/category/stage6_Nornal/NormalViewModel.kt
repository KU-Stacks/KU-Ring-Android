package com.ku_stacks.ku_ring.ui.main.notice.category.stage6_Nornal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.ku_stacks.ku_ring.data.model.Notice
import com.ku_stacks.ku_ring.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Flowable
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NormalViewModel @Inject constructor(
    private val repository: NoticeRepository
): ViewModel() {

    private var currentNoticeResult: Flowable<PagingData<Notice>>? = null

    init {
        Timber.e("NormalViewModel injected")
    }

    fun getNotices(): Flowable<PagingData<Notice>> {
        val lastResult = currentNoticeResult
        if (lastResult != null) {
            return lastResult
        }
        val newResult = repository.getNotices("nor", viewModelScope)
        currentNoticeResult = newResult
        return newResult
    }
}