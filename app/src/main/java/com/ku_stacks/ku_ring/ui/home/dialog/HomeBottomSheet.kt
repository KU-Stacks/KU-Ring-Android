package com.ku_stacks.ku_ring.ui.home.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.analytics.EventAnalytics
import com.ku_stacks.ku_ring.databinding.DialogHomeBottomSheetBinding
import com.ku_stacks.ku_ring.ui.feedback.FeedbackActivity
import com.ku_stacks.ku_ring.ui.notion.NotionViewActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HomeBottomSheet: BottomSheetDialogFragment() {

    @Inject
    lateinit var analytics : EventAnalytics

    private lateinit var binding: DialogHomeBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DialogHomeBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newContentsLayout.setOnClickListener {
            startNewContentsActivity()
        }
        binding.feedbackLayout.setOnClickListener {
            startFeedbackActivity()
        }
        binding.openSourceLayout.setOnClickListener {
            startOpenSourceActivity()
        }
        binding.personalDataLayout.setOnClickListener {
            startPersonalInformationActivity()
        }
        binding.termsOfServiceLayout.setOnClickListener {
            startTermsOfServiceActivity()
        }
    }

    private fun startNewContentsActivity() {
        analytics.click("bottomSheet_NewContents btn", "HomeActivity")

        val intent = Intent(activity, NotionViewActivity::class.java)
        intent.putExtra(NotionViewActivity.NOTION_URL, getString(R.string.notion_new_contents_url))
        startActivity(intent)
        dialog?.dismiss()
    }

    private fun startFeedbackActivity() {
        analytics.click("bottomSheet_Feedback btn", "HomeActivity")

        val intent = Intent(activity, FeedbackActivity::class.java)
        startActivity(intent)
        dialog?.dismiss()
    }

    private fun startOpenSourceActivity() {
        analytics.click("bottomSheet_OpenSource btn", "HomeActivity")

        startActivity(Intent(activity, OssLicensesMenuActivity::class.java))
        OssLicensesMenuActivity.setActivityTitle("오픈소스 라이선스")
        dialog?.dismiss()
    }

    private fun startPersonalInformationActivity() {
        analytics.click("bottomSheet_PersonalInformation btn", "HomeActivity")

        val intent = Intent(activity, NotionViewActivity::class.java)
        intent.putExtra(NotionViewActivity.NOTION_URL, getString(R.string.notion_privacy_policy_url))
        startActivity(intent)
        dialog?.dismiss()
    }

    private fun startTermsOfServiceActivity() {
        analytics.click("bottomSheet_TermsOfService btn", "HomeActivity")

        val intent = Intent(activity, NotionViewActivity::class.java)
        intent.putExtra(NotionViewActivity.NOTION_URL, getString(R.string.notion_terms_of_service_url))
        startActivity(intent)
        dialog?.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.e("HomeBottomSheet destroyed")
    }
}