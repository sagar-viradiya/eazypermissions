# Coroutines support
Requesting permission is just a simple function call to suspending function `requestPermissions` of [`PermissionManager`](../coroutinespermission/src/main/java/com/eazypermissions/coroutinespermission/PermissionManager.kt) from your coroutines or other suspending function which will return [`PermissionResult`](../common/src/main/java/com/eazypermissions/common/model/PermissionResult.kt). It takes 3 parameters.
1. An instance of AppCompactActivity or Fragment depending on from where you are requesting permission.
2. Request id.
3. varargs of permission you want to request.

This is how you would request for permission within coroutines and get result sequentially.

```kotlin
.
.
.
launch {
    //CoroutineScope

    val permissionResult = PermissionManager.requestPermissions(           //Suspends the coroutine
                            this@Fragment,                                  
                            REQUEST_ID,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.CAMERA
                        )
                        
    //Resume coroutine once result is ready
    when(permissionResult) {
        is PermissionResult.PermissionGranted -> {
            //Add your logic here after user grants permission(s)
        }
        is PermissionResult.PermissionDenied -> {
            //Add your logic to handle permission denial
        }
        is PermissionResult.PermissionDeniedPermanently -> {
            //Add your logic here if user denied permission(s) permanently.
            //Ideally you should ask user to manually go to settings and enable permission(s)
        }
        is PermissionResult.ShowRational -> {
            //If user denied permission frequently then she/he is not clear about why you are asking this permission.
            //This is your chance to explain them why you need permission.
        }
    }

}
```
You can request permission from coroutine launched using any dispatcher(IO/Default/Main).

Library exposes [`PermissionResult`](../common/src/main/java/com/eazypermissions/common/model/PermissionResult.kt) as result of permission request which is nothing but simple sealed class which wraps all possible outcomes.
```kotlin
sealed class PermissionResult(val requestCode: Int) {
    class PermissionGranted(requestCode: Int) : PermissionResult(requestCode)
    class PermissionDenied(
        requestCode: Int,
        val deniedPermissions: List<String>
    ) : PermissionResult(requestCode)

    class ShowRational(requestCode: Int) : PermissionResult(requestCode)
    class PermissionDeniedPermanently(
        requestCode: Int,
        val permanentlyDeniedPermissions: List<String>
    ) : PermissionResult(requestCode)
}
```
Notice `PermissionDenied` and `PermissionDeniedPermanently` are also exposing list of denied permissions and permanently denied permissions respectively so that you can decide your flow based on denied permissions if you want to.
