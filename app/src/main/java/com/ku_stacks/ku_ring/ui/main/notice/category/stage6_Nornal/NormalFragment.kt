package com.ku_stacks.ku_ring.ui.main.notice.category.stage6_Nornal

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.ku_stacks.ku_ring.ui.main.notice.category.HomeBaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NormalFragment : HomeBaseFragment() {

    private val viewModel by viewModels<NormalViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposable.add(
            viewModel.getNotices().subscribe {
                pagingAdapter.submitData(lifecycle, it)
            })
    }
}