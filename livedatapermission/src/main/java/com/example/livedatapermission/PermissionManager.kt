package com.example.livedatapermission

import android.arch.lifecycle.LiveData
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.annotation.MainThread
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.example.livedatapermission.model.PermissionResult

/**
 * Permission manager which handles checking permission is granted or not and if not then will request permission.
 * This is nothing but a headless fragment which wraps the boilerplate code for checking and requesting permission
 * and expose the result of permission request as [LiveData]
 * A simple [Fragment] subclass.
 */
class PermissionManager : Fragment() {

    private val permissionResultLiveEvent: SingleLiveEvent<PermissionResult> by lazy {
        SingleLiveEvent<PermissionResult>()
    }

    private val rationalRequest = mutableMapOf<Int, Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            permissionResultLiveEvent.postValue(PermissionResult.PermissionGranted(requestCode))
        } else if (permissions.any { shouldShowRequestPermissionRationale(it) }) {
            permissionResultLiveEvent.postValue(PermissionResult.PermissionDenied(requestCode))
        } else {
            permissionResultLiveEvent.postValue(PermissionResult.PermissionDeniedPermanently(requestCode))
        }
    }

    private fun requestPermissions(requestId: Int, vararg permissions: String) {

        rationalRequest[requestId]?.let {
            requestPermissions(permissions, requestId)
            rationalRequest.remove(requestId)
            return
        }

        val notGranted = permissions.filter {
            ContextCompat.checkSelfPermission(requireActivity(), it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        when {
            notGranted.isEmpty() -> permissionResultLiveEvent.postValue(PermissionResult.PermissionGranted(requestId))
            shouldShowRequestPermissionRationale(notGranted[0]) -> {
                rationalRequest[requestId] = true
                permissionResultLiveEvent.postValue(PermissionResult.ShowRational(requestId))
            }
            else -> {
                requestPermissions(notGranted, requestId)
            }
        }

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        (context as PermissionObserver).setupObserver(permissionResultLiveEvent)
    }

    companion object {

        private const val TAG = "PermissionManager"

        /**
         * A static factory method to request permission from activity/fragment.
         * Your activity/fragment must implement [PermissionObserver]
         *
         * @param activity an instance of [AppCompatActivity] which is also [PermissionObserver]
         * @param requestId Request ID for permission request
         * @param permissions Permission(s) to request
         *
         * @throws [IllegalArgumentException] if your activity/fragment doesn't implement [PermissionObserver]
         */
        @JvmStatic
        @MainThread
        fun requestPermissions(activity: AppCompatActivity, requestId: Int, vararg permissions: String) {
            if (activity.supportFragmentManager.findFragmentByTag(TAG) != null) {
                (activity.supportFragmentManager.findFragmentByTag(TAG) as PermissionManager).requestPermissions(
                    requestId,
                    *permissions
                )
            } else {
                if (activity !is PermissionObserver) {
                    throw IllegalArgumentException("Activity/Fragment must implement PermissionObserver")
                } else {
                    val permissionManager = PermissionManager()
                    activity.supportFragmentManager.beginTransaction().add(permissionManager, TAG).commitNow()
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
