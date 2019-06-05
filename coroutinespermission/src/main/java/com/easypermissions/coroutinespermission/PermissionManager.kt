package com.easypermissions.coroutinespermission

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.easypermissions.common.BasePermissionManager
import com.easypermissions.common.model.PermissionResult
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 *
 */
class PermissionManager : BasePermissionManager() {

    private lateinit var completableDeferred: CompletableDeferred<PermissionResult>

    override fun onPermissionResult(permissionResult: PermissionResult) {
        completableDeferred.complete(permissionResult)
    }

    companion object {

        private const val TAG = "PermissionManager"
        private const val DEFAULT_PERMISSION_REQ = 1

        suspend fun requestPermissions(
            activity: AppCompatActivity,
            vararg permissions: String
        ): PermissionResult {
            return withContext(Dispatchers.Main) {
                return@withContext _requestPermissions(activity, *permissions)
            }
        }

        suspend fun requestPermissions(
            fragment: Fragment,
            vararg permissions: String
        ): PermissionResult {
            return withContext(Dispatchers.Main) {
                return@withContext _requestPermissions(fragment, *permissions)
            }
        }

        private suspend fun _requestPermissions(
            activityOrFragment: Any,
            vararg permissions: String
        ): PermissionResult {
            val fragmentManager = if (activityOrFragment is AppCompatActivity) {
                activityOrFragment.supportFragmentManager
            } else {
                (activityOrFragment as Fragment).childFragmentManager
            }
            return if (fragmentManager.findFragmentByTag(TAG) != null) {
                val permissionManager = fragmentManager.findFragmentByTag(TAG) as PermissionManager
                permissionManager.completableDeferred = CompletableDeferred()
                permissionManager.requestPermissions(
                    DEFAULT_PERMISSION_REQ,
                    *permissions
                )
                permissionManager.completableDeferred.await()
            } else {
                val permissionManager = PermissionManager().apply {
                    completableDeferred = CompletableDeferred()
                }
                fragmentManager.beginTransaction().add(
                    permissionManager,
                    TAG
                ).commitNow()
                permissionManager.requestPermissions(DEFAULT_PERMISSION_REQ, *permissions)
                permissionManager.completableDeferred.await()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (completableDeferred.isActive) {
            completableDeferred.cancel()
        }
    }
}