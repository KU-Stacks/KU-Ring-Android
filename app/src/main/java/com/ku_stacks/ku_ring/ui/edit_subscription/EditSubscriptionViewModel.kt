package com.ku_stacks.ku_ring.ui.edit_subscription

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.messaging.FirebaseMessaging
import com.ku_stacks.ku_ring.analytics.EventAnalytics
import com.ku_stacks.ku_ring.data.api.request.SubscribeRequest
import com.ku_stacks.ku_ring.repository.SubscribeRepository
import com.ku_stacks.ku_ring.util.PreferenceUtil
import com.ku_stacks.ku_ring.util.WordConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject
import java.util.*
import kotlin.Comparator

@HiltViewModel
class EditSubscriptionViewModel @Inject constructor(
    private val repository: SubscribeRepository,
    private val pref: PreferenceUtil,
    private val analytics: EventAnalytics,
    firebaseMessaging: FirebaseMessaging
) : ViewModel(){

    private val disposable = CompositeDisposable()

    private val _subscriptionList = ArrayList<String>()
    val subscriptionList = MutableLiveData<ArrayList<String>>()
    val isSubscriptionEmpty = MutableLiveData(true)

    private val _unSubscriptionList = ArrayList<String>()
    val unSubscriptionList = MutableLiveData<ArrayList<String>>()

    private val _hasUpdate = MutableLiveData(false)
    val hasUpdate: LiveData<Boolean>
        get() = _hasUpdate

    private var fcmToken: String? = null

    /** 초기 설정이 끝나기 전에 뒤로가기를 하면 빈 목록을 구독하는 경우를 방지하기 위함 */
    private var initFlag = false

    /** 첫 앱 구동자에게 보여지는 온보딩 후의 푸시 세팅을 위한 분기처리 용도 */
    var firstRunFlag = false

    init {
        firebaseMessaging.token.addOnCompleteListener { task ->
            if(!task.isSuccessful) {
                Timber.e("Firebase instanceId fail : ${task.exception}")
                analytics.errorEvent("${task.exception}", className)
            }
            else if (task.result == null) {
                Timber.e("Fcm Token is null")
                analytics.errorEvent("Fcm Token is null!", className)
            } else {
                fcmToken = task.result
                syncWithServer()
            }
        }
    }

    fun syncWithServer() {
        fcmToken?.let {
            _subscriptionList.clear()
            _unSubscriptionList.clear()
            _unSubscriptionList.addAll(listOf(
                "학사", "장학", "취창업", "국제", "학생", "산학", "일반", "도서관"
            ))

            disposable.add(
                repository.fetchSubscriptionFromRemote(fcmToken!!)
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        initialSortSubscription(it)
                        refreshAfterUpdate()
                    },
                        { Timber.e("getSubscribeList fail $it") })
            )
        }
    }

    fun saveSubscribe() {
        if(initFlag == false) {
            return
        }

        fcmToken?.let {
            repository.saveSubscriptionToLocal(_subscriptionList)
            repository.saveSubscriptionToRemote(
                SubscribeRequest(it, _subscriptionList.toList().map { category ->
                    WordConverter.convertKoreanToEnglish(category)
                })
            )
        }
    }

    fun removeSubscription(category: String) {
        _subscriptionList.remove(category)
        _subscriptionList.sortWith(CategoryComparator)
        subscriptionList.postValue(_subscriptionList)
    }

    fun removeUnSubscription(category: String) {
        _unSubscriptionList.remove(category)
        _unSubscriptionList.sortWith(CategoryComparator)
        unSubscriptionList.postValue(_unSubscriptionList)
    }

    fun addSubscription(category: String) {
        if (!_subscriptionList.contains(category)) {
            _subscriptionList.add(category)
        }
        _subscriptionList.sortWith(CategoryComparator)
        subscriptionList.postValue(_subscriptionList)
    }

    fun addUnSubscription(category: String) {
        if (!_unSubscriptionList.contains(category)) {
            _unSubscriptionList.add(category)
        }
        _unSubscriptionList.sortWith(CategoryComparator)
        unSubscriptionList.postValue(_unSubscriptionList)
    }

    fun refreshAfterUpdate() {
        if(firstRunFlag) {
            return
        }
        val localSubscription = repository.getSubscriptionFromLocal()
        if (localSubscription.size != _subscriptionList.size) {
            _hasUpdate.postValue(true)
            return
        }
        for (data in _subscriptionList) {
            if (!localSubscription.contains(WordConverter.convertKoreanToShortEnglish(data))) {
                _hasUpdate.postValue(true)
                return
            }
        }
        _hasUpdate.postValue(false)
    }

    private fun initialSortSubscription(subscribingList: List<String>) {
        for(str in subscribingList){
            _subscriptionList.add(str)
            _unSubscriptionList.remove(str)
        }
        _subscriptionList.sortWith(CategoryComparator)
        _unSubscriptionList.sortWith(CategoryComparator)
        subscriptionList.postValue(_subscriptionList)
        unSubscriptionList.postValue(_unSubscriptionList)
        initFlag = true
    }

    /**
     * 변경될때마다 정렬하는 방식 채택
     * getPriority()가 낮을수록 앞쪽으로 정렬
     */
    object CategoryComparator : Comparator<String> {
        private fun getPriority(category: String): Int {
            return when (category) {
                "학사" -> 1
                "장학" -> 2
                "취창업" -> 3
                "국제" -> 4
                "학생" -> 5
                "산학" -> 6
                "일반" -> 7
                "도서관" -> 8
                else -> 100
            }
        }

        override fun compare(a: String, b: String): Int {
            return getPriority(a) - getPriority(b)
        }
    }

    companion object {
        const val className = "EditSubscriptionViewModel"
    }
}