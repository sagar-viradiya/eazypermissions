package com.eazypermissions.livedatapermission

import android.arch.lifecycle.LiveData
import android.content.Context
import android.support.annotation.MainThread
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.eazypermissions.common.BasePermissionManager
import com.eazypermissions.common.model.PermissionResult

/**
 * Permission manager which handles checking permission is granted or not and if not then will request permission.
 * This is nothing but a headless fragment which wraps the boilerplate code for checking and requesting permission
 * and expose the result of permission request as [LiveData].
 * A simple [Fragment] subclass.
 */
class PermissionManager : BasePermissionManager() {

    private val permissionResultLiveEvent: SingleLiveEvent<PermissionResult> by lazy {
        SingleLiveEvent<PermissionResult>()
    }

    override fun onPermissionResult(permissionResult: PermissionResult) {
        permissionResultLiveEvent.postValue(permissionResult)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (parentFragment != null) {
            (parentFragment as PermissionObserver).setupObserver(permissionResultLiveEvent)
        } else {
            (context as PermissionObserver).setupObserver(permissionResultLiveEvent)
        }
    }

    companion object {

        private const val TAG = "PermissionManager"

        /**
         * A static factory method to request permission from activity.
         * Your activity must implement [PermissionObserver]
         *
         * @param activity an instance of [AppCompatActivity] which is also [PermissionObserver]
         * @param requestId Request ID for permission request
         * @param permissions Permission(s) to request
         *
         * @throws [IllegalArgumentException] if your activity doesn't implement [PermissionObserver]
         */
        @JvmStatic
        @MainThread
        fun requestPermissions(activity: AppCompatActivity, requestId: Int, vararg permissions: String) {
            _requestPermissions(
                activity,
                requestId,
                *permissions
            )
        }

        /**
         * A static factory method to request permission from fragment.
         * Your fragment must implement [PermissionObserver]
         *
         * @param fragment an instance of [Fragment] which is also [PermissionObserver]
         * @param requestId Request ID for permission request
         * @param permissions Permission(s) to request
         *
         * @throws [IllegalArgumentException] if your fragment doesn't implement [PermissionObserver]
         */
        @JvmStatic
        @MainThread
        fun requestPermissions(fragment: Fragment, requestId: Int, vararg permissions: String) {
            _requestPermissions(
                fragment,
                requestId,
                *permissions
            )
        }

        private fun _requestPermissions(
            activityOrFragment: Any,
            requestId: Int,
            vararg permissions: String
        ) {
            val fragmentManager = if (activityOrFragment is AppCompatActivity) {
                activityOrFragment.supportFragmentManager
            } else {
                (activityOrFragment as Fragment).childFragmentManager
            }
            if (fragmentManager.findFragmentByTag(TAG) != null) {
                (fragmentManager.findFragmentByTag(TAG) as PermissionManager).requestPermissions(
                    requestId,
                    *permissions
                )
            } else {
                if (activityOrFragment !is PermissionObserver) {
                    throw IllegalArgumentException("Activity/Fragment must implement PermissionObserver")
                } else {
                    val permissionManager = PermissionManager()
                    fragmentManager.beginTransaction().add(
                        permissionManager,
                        TAG
                    ).commitNow()
                    permissionManager.requestPermissions(requestId, *permissions)
                }
            }
        }
    }

    /**
     * Interface definition for a callback to get [LiveData] of [PermissionResult]
     *
     * Implement this interface to get [LiveData] for observing permission request result.
     */
    interface PermissionObserver {
        fun setupObserver(permissionResultLiveData: LiveData<PermissionResult>)
    }
}
