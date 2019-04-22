package com.example.livedatapermission

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.pm.PackageManager
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity

/**
 * A simple [Fragment] subclass.
 */
class PermissionManager : Fragment() {

    private val permissionResultLiveData: MutableLiveData<PermissionResult> by lazy {
        MutableLiveData<PermissionResult>()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permissionResultLiveData.postValue(PermissionResult.PermissionGranted(requestCode))
        } else if (shouldShowRequestPermissionRationale(permissions[0])) {
            permissionResultLiveData.postValue(PermissionResult.PermissionDenied(requestCode))
        } else {
            permissionResultLiveData.postValue(PermissionResult.PermissionDeniedPermanently(requestCode))
        }
    }

    private fun requestPermissions(requestId: Int, vararg permissions: String) {
        val notGranted = permissions.filter {
            ContextCompat.checkSelfPermission(requireActivity(), it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        when {
            notGranted.isEmpty() -> permissionResultLiveData.postValue(PermissionResult.PermissionGranted(requestId))
            shouldShowRequestPermissionRationale(notGranted[0]) ->
                permissionResultLiveData.postValue(PermissionResult.ShowRational(requestId))
            else -> {
                requestPermissions(notGranted, requestId)
            }
        }

    }

    companion object {

        private const val TAG = "PermissionManager"

        @JvmStatic
        fun requestPermissions(activity: AppCompatActivity, requestId: Int, vararg permissions: String) {
            if (activity.supportFragmentManager.findFragmentByTag(TAG) != null) {
                (activity.supportFragmentManager.findFragmentByTag(TAG) as PermissionManager).requestPermissions(requestId, *permissions)
            } else {
                if (activity !is PermissionObserver) {
                    throw IllegalArgumentException("Activity/Fragment must implement PermissionObserver")
                } else {
                    val permissionManager = PermissionManager()
                    activity.supportFragmentManager.beginTransaction().add(permissionManager, TAG).commit()
                    activity.setupObserver(permissionManager.permissionResultLiveData)
                }
            }
        }
    }

    interface PermissionObserver {
        fun setupObserver(permissionResultLiveData: LiveData<PermissionResult>)
    }
}
