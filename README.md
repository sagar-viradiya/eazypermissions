# Eazy Runtime Permission
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/sagar-viradiya/eazypermissions/blob/master/LICENSE) [![Build Status](https://travis-ci.com/sagar-viradiya/eazypermissions.svg?token=VppdY5VoQBEp72REmqxi&branch=master)](https://travis-ci.com/sagar-viradiya/eazypermissions) [![API](https://img.shields.io/badge/API-20%2B-orange.svg?style=flat)](https://android-arsenal.com/api?level=20) [![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/) [![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-eazypermissions-green.svg?style=flat )]( https://android-arsenal.com/details/1/7799 )

A lightweight Android library which wraps boilerplate code of runtime permission and allows you to request permissions 
1. from coroutines. (No callbacks yay :tada:)
2. request and observe permissions through LiveData.
3. through clean and concise Kotlin DSL.

> From release `2.0.0` onwards library is migrated to AndroidX. If you are still using support library and haven't migrated to AndroidX then check out non-androidX version of the [library](https://github.com/sagar-viradiya/eazypermissions/tree/master-nonandroidx).

## Including in your project
Eazy permissions is available in the Jcenter and divided into three modules so that based on your need you can include either coroutines or livedata or Kotlin DSL support in your project

```groovy
//For coroutines
implementation 'com.sagar:coroutinespermission:[latest_version]'

//For LiveData
implementation 'com.sagar:livedatapermission:[latest_version]'

//For Kotlin DSL
implementation 'com.sagar:dslpermission:[latest_version]'
```
- `latest_version` for coroutines - [ ![Download](https://api.bintray.com/packages/sagar-viradiya/eazypermissions/coroutinespermission/images/download.svg) ](https://bintray.com/sagar-viradiya/eazypermissions/coroutinespermission/_latestVersion)
- `latest_version` for livedata - [ ![Download](https://api.bintray.com/packages/sagar-viradiya/eazypermissions/livedatapermission/images/download.svg) ](https://bintray.com/sagar-viradiya/eazypermissions/livedatapermission/_latestVersion)
- `latest_version` for Kotlin DSL - [ ![Download](https://api.bintray.com/packages/sagar-viradiya/eazypermissions/dslpermission/images/download.svg) ](https://bintray.com/sagar-viradiya/eazypermissions/dslpermission/_latestVersion)

## Coroutines support
This is how you would request permission within coroutines.

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

Read more about coroutines support [*here*](coroutinespermission)

## Kotlin DSL support
This is how you would request permission through clean and concise Kotlin DSL.

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

Read more about Kotlin DSL support [*here*](dslpermission)

## LiveData support
This is how you would request permission within Acivity/Fragment.

```kotlin
PermissionManager.requestPermissions(
                this,
                REQUEST_ID,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_CONTACTS
            )
```

Observing permission result requires your Actvity/Fragment to implement [`PermissionObserver`](https://github.com/sagar-viradiya/eazypermissions/blob/e1a36d5fb3ad487ac22da9b18e9b4c848cfcb74c/livedatapermission/src/main/java/com/eazypermissions/livedatapermission/PermissionManager.kt#L115)

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
Just as you would observe other LiveData you can observe LiveData<[`PermissionResult`](common/src/main/java/com/eazypermissions/common/model/PermissionResult.kt)> as follow.
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
Read more about LiveData support [*here*](livedatapermission)

## Contributing
Have suggestions for improvements and want to contribute? or Found any issues?  
Head over to [Contribution guidelines](CONTRIBUTING.md) to know more about contributing to this library.

## Screenshots of sample
![sc1](screenshots/SS1.png) ![sc2](screenshots/SS2.png)   
![sc1](screenshots/SS3.png) ![sc2](screenshots/SS4.png)

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
