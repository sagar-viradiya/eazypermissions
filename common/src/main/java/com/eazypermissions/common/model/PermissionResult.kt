/*
 * Copyright 2019 Sagar Viradiya
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eazypermissions.common.model

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