# Kotlin DSL support
You can request permissions from Activity/Fragment as shown below.

```kotlin
requestPermissions(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.READ_CONTACTS,
    Manifest.permission.CAMERA
) {
    requestCode = 4
    resultCallback = {
        when(this) {
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
}
```
[`requestPermissions`](https://github.com/sagar-viradiya/eazypermissions/blob/a3218042f2659fde267c021f5fbefd3069250dcd/dsl/src/main/java/com/eazypermissions/dsl/extension/Extensions.kt#L28) is an extension function on Activity/Fragment. It takes two arguments
1. `vararg` of permissions.
2. Lambda with a receiver on [`PermissionRequest`](dsl/src/main/java/com/eazypermissions/dsl/model/PermissionRequest.kt). 

Within lambda you need to initialize `requestCode` and `resultCallback`. Library will invoke `resultCallback` upon permission result. `resultCallback` is lambda with a receiver on [`PermissionResult`](common/src/main/java/com/eazypermissions/common/model/PermissionResult.kt) so you have direct access to [`PermissionResult`](common/src/main/java/com/eazypermissions/common/model/PermissionResult.kt) as `this` within lambda. [`PermissionResult`](common/src/main/java/com/eazypermissions/common/model/PermissionResult.kt) is simple sealed class which wraps all possible outcomes as shown in above code snippet.

If you are requesting permissions outside Activity/Fragment then use the following DSL.

```kotlin
PermissionManager.requestPermissions(
    fragment,   //Instance of Activity or Fragment
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.READ_CONTACTS,
    Manifest.permission.CAMERA
) {
    requestCode = 4
    resultCallback = {
        when(this) {
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
}
```
The only difference here is you need to call `PermissionManager.requestPermissions` which takes an instance of Fragment/Activity as an additional parameter.
