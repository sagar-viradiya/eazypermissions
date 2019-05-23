package com.example.livedatapermission.model

sealed class PermissionResult {
    class PermissionGranted(val requestId: Int) : PermissionResult()
    class PermissionDenied(val requestId: Int, val deniedPermissions: List<String>) : PermissionResult()
    class ShowRational(val requestId: Int) : PermissionResult()
    class PermissionDeniedPermanently(val requestId: Int, val permanentlyDeniedPermissions: List<String>) :
        PermissionResult()
}