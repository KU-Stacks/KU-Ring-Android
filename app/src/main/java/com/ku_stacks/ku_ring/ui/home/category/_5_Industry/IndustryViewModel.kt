package com.ku_stacks.ku_ring.ui.home.category._5_Industry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.ku_stacks.ku_ring.data.entity.Notice
import com.ku_stacks.ku_ring.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Flowable
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class IndustryViewModel @Inject constructor(
    private val repository: NoticeRepository
): ViewModel() {

    init {
        Timber.e("IndustryViewModel injected")
    }

    fun getNotices(): Flowable<PagingData<Notice>> {
        return repository
            .getNotices("ind")
            .cachedIn(viewModelScope)
    }
}