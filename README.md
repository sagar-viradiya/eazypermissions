# LiveData Runtime Permission
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/sagar-viradiya/livedata-permission/blob/master/LICENSE)

A lightweight(Yes, we care about method count and memory usage) Android library which wraps boilerplate code of runtime permission and expose the result as LiveData. With just one simple step(implementing an interface) you are ready to request permission and observe the result of request.

## Requesting permission
Requesting permission is just a simple method call from your Activity/Fragment. It takes 3 parameters.
1. An instance of AppCompactActivity or Fragment depending from where you are requesting permission.
2. Request id.
3. varargs of permission you want to request.

This is how you request for single permission from your Activity/Fragment.
```kotlin
PermissionManager.requestPermissions(
                this,
                REQUEST_ID,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
```

You can also request multiple permissions at once like this
```kotlin
PermissionManager.requestPermissions(
                this,
                REQUEST_ID,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_CONTACTS
            )
```

## Observing permission request result
Your Activity/Fragment must implement [PermissionObserver]() which expose LiveData<[PermissionResult]()>. Here is the definition of [PermissionObserver]()
```kotlin
/**
 * Interface definition for a callback to get [LiveData] of [PermissionResult]
 *
 * Implement this interface to get [LiveData] for observing permission request result.
 */
interface PermissionObserver {
    fun setupObserver(permissionResultLiveData: LiveData<PermissionResult>)
}
```
Just as you would observe other LiveData you can observe LiveData<[PermissionResult]()> as follow
```kotlin
override fun setupObserver(permissionResultLiveData: LiveData<PermissionResult>) {
    permissionResultLiveData.observe(this, Observer<PermissionResult> {
        when (it) {
            is PermissionResult.PermissionGranted -> {
                if (it.requestId == REQUEST_ID) {
                    //Add your logic here after user grants permission(s)
                }
            }
            is PermissionResult.PermissionDenied -> {
                if (it.requestId == REQUEST_ID) {
                    //Add your logic to handle permission denial
                }
            }
            is PermissionResult.PermissionDeniedPermanently -> {
                if (it.requestId == REQUEST_ID) {
                    //Add your logic here if user denied permission(s) permanently.
                    //Ideally you should ask user to manually go to settings and enable permission(s)
                }
            }
            is PermissionResult.ShowRational -> {
                if (it.requestId == REQUEST_ID) {
                    //If user denied permission frequently then she/he is not clear about why you are asking this permission.
                    //This is your chance to explain them why you need permission.
                }
            }
        }
    })
}
```
> It is mandatory to implement [PermissionObserver]() from where you are requesting permission(either Activity or Fragment).
If you don't then library will throw `IllegalArgumentException` stating that you have to implement [PermissionObserver]()

Library will take care of Activity/Fragment recreation so even if user rotates screen or due to some other reason if your Activity/Fragment gets recreated it will call `setupObserver` method to register new observer of LiveData.

Library exposes [PermissionResult]() as result of permission request which is nothing but simple sealed class.
```kotlin
sealed class PermissionResult {
    class PermissionGranted(val requestId: Int) : PermissionResult()
    class PermissionDenied(val requestId: Int, val deniedPermissions: List<String>) : PermissionResult()
    class ShowRational(val requestId: Int) : PermissionResult()
    class PermissionDeniedPermanently(val requestId: Int, val permanentlyDeniedPermissions: List<String>) : PermissionResult()
}
```
Notice `PermissionDenied` and `PermissionDeniedPermanently` are also exposing list of denied permissions and permanently denied permissions respectively so that you can decide your flow based on denied permissions if you want to.

# License

```
Copyright 2019 Sagar Viradiya

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
