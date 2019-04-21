package com.example.livedatapermission

sealed class PermissionResult(requestId: Int) {
    class PermissionGranted(requestId: Int) : PermissionResult(requestId)
    class PermissionDenied(requestId: Int) : PermissionResult(requestId)
    class ShowRational(requestId: Int) : PermissionResult(requestId)
    class PermissionDeniedPermanently(requestId: Int) : PermissionResult(requestId)
}