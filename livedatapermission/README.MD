# LiveData support
Requesting permission is just a simple method call to [`PermissionManager`](../livedatapermission/src/main/java/com/eazypermissions/livedatapermission/PermissionManager.kt) from your Activity/Fragment. It takes 3 parameters.
1. An instance of AppCompactActivity or Fragment depending from where you are requesting permission.
2. Request id.
3. varargs of permission you want to request.

This is how you request permissions from your Activity/Fragment.
```kotlin
PermissionManager.requestPermissions(
                this,
                REQUEST_ID,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_CONTACTS
            )
```

## Observing permission request result
With just one simple step(implementing an interface) you are ready to observe the result of request.
Your Activity/Fragment must implement `setupObserver` method of [`PermissionObserver`](https://github.com/sagar-viradiya/eazypermissions/blob/e1a36d5fb3ad487ac22da9b18e9b4c848cfcb74c/livedatapermission/src/main/java/com/eazypermissions/livedatapermission/PermissionManager.kt#L115) interface which expose LiveData<[`PermissionResult`](../common/src/main/java/com/eazypermissions/common/model/PermissionResult.kt)>. Here is the definition of [`PermissionObserver`](https://github.com/sagar-viradiya/eazypermissions/blob/e1a36d5fb3ad487ac22da9b18e9b4c848cfcb74c/livedatapermission/src/main/java/com/eazypermissions/livedatapermission/PermissionManager.kt#L115)
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
The library will only call `setupObserver` method when you are requesting permission for the first time. All the successive call to `requestPermissions` method will use the same observer.

Just as you would observe other LiveData you can observe LiveData<[`PermissionResult`](../common/src/main/java/com/eazypermissions/common/model/PermissionResult.kt)> as follow
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
> It is mandatory to implement [`PermissionObserver`](https://github.com/sagar-viradiya/eazypermissions/blob/e1a36d5fb3ad487ac22da9b18e9b4c848cfcb74c/livedatapermission/src/main/java/com/eazypermissions/livedatapermission/PermissionManager.kt#L115) from where you are requesting permission(either Activity or Fragment).
If you don't then library will throw `IllegalArgumentException` stating that you have to implement [`PermissionObserver`](https://github.com/sagar-viradiya/eazypermissions/blob/e1a36d5fb3ad487ac22da9b18e9b4c848cfcb74c/livedatapermission/src/main/java/com/eazypermissions/livedatapermission/PermissionManager.kt#L115)

Library will take care of Activity/Fragment recreation so even if user rotates screen or due to some other reason if your Activity/Fragment gets recreated it will call `setupObserver` method to register new observer of LiveData.
