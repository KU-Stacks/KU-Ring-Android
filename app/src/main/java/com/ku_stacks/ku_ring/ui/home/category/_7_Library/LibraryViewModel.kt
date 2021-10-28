package com.ku_stacks.ku_ring.ui.home.category._7_Library

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
class LibraryViewModel @Inject constructor(
    private val repository: NoticeRepository
): ViewModel() {

    init {
        Timber.e("LibraryViewModel injected")
    }

    fun getNotices(): Flowable<PagingData<Notice>> {
        return repository
            .getNotices("lib")
            .cachedIn(viewModelScope)
    }
}