package com.easypermissions.coroutinespermission

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.easypermissions.common.model.PermissionResult
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 *
 */
class PermissionManager : com.easypermissions.common.PermissionManager() {

    private val completableDeferred = CompletableDeferred<PermissionResult>()

    override fun onPermissionResult(permissionResult: PermissionResult) {
        completableDeferred.complete(permissionResult)
    }

    companion object {

        private const val TAG = "PermissionManager"

        suspend fun requestPermissions(
            activity: AppCompatActivity,
            requestId: Int,
            vararg permissions: String
        ): PermissionResult {
            return withContext(Dispatchers.Main) {
                return@withContext _requestPermissions(activity, requestId, *permissions)
            }
        }

        suspend fun requestPermissions(
            fragment: Fragment,
            requestId: Int,
            vararg permissions: String
        ): PermissionResult {
            return withContext(Dispatchers.Main) {
                return@withContext _requestPermissions(fragment, requestId, *permissions)
            }
        }

        private suspend fun _requestPermissions(
            activityOrFragment: Any,
            requestId: Int,
            vararg permissions: String
        ): PermissionResult {
            val fragmentManager = if (activityOrFragment is AppCompatActivity) {
                activityOrFragment.supportFragmentManager
            } else {
                (activityOrFragment as Fragment).childFragmentManager
            }
            return if (fragmentManager.findFragmentByTag(TAG) != null) {
                val permissionManager = fragmentManager.findFragmentByTag(TAG) as PermissionManager
                permissionManager.requestPermissions(
                    requestId,
                    *permissions
                )
                permissionManager.completableDeferred.await()
            } else {
                val permissionManager = PermissionManager()
                fragmentManager.beginTransaction().add(permissionManager,
                    TAG
                ).commitNow()
                permissionManager.requestPermissions(requestId, *permissions)
                permissionManager.completableDeferred.await()
            }
        }

    }

}
