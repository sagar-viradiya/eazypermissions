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

sealed class PermissionResult(val requestId: Int) {
    class PermissionGranted(requestId: Int) : PermissionResult(requestId)
    class PermissionDenied(
        requestId: Int,
        val deniedPermissions: List<String>
    ) : PermissionResult(requestId)

    class ShowRational(requestId: Int) : PermissionResult(requestId)
    class PermissionDeniedPermanently(
        requestId: Int,
        val permanentlyDeniedPermissions: List<String>
    ) : PermissionResult(requestId)
}