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

package com.example.sample


import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.eazypermissions.common.model.PermissionResult
import com.eazypermissions.coroutinespermission.PermissionManager
import kotlinx.android.synthetic.main.fragment_ui.*
import kotlinx.coroutines.*

/**
 * A simple [Fragment] subclass.
 *
 */
class CoroutinesFragment : Fragment() {

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(parentJob + Dispatchers.Default)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ui, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        location_permission_btn.setOnClickListener {
            coroutineScope.launch {
                withContext(Dispatchers.Main) {
                    handleResult(
                        PermissionManager.requestPermissions(
                            this@CoroutinesFragment, 1,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    )
                }
            }
        }

        contact_permission_btn.setOnClickListener {
            coroutineScope.launch {
                withContext(Dispatchers.Main) {
                    handleResult(
                        PermissionManager.requestPermissions(
                            this@CoroutinesFragment, 2,
                            Manifest.permission.READ_CONTACTS
                        )
                    )
                }
            }
        }

        camera_permission_btn.setOnClickListener {
            coroutineScope.launch {
                withContext(Dispatchers.Main) {
                    handleResult(
                        PermissionManager.requestPermissions(
                            this@CoroutinesFragment, 3,
                            Manifest.permission.CAMERA
                        )
                    )
                }
            }
        }

        location_contact_camera_permission_btn.setOnClickListener {
            coroutineScope.launch {
                withContext(Dispatchers.Main) {
                    handleResult(
                        PermissionManager.requestPermissions(
                            this@CoroutinesFragment, 4,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.CAMERA
                        )
                    )
                }
            }
        }

    }

    private fun handleResult(permissionResult: PermissionResult) {
        when (permissionResult) {
            is PermissionResult.PermissionGranted -> {
                Toast.makeText(requireContext(), "Granted", Toast.LENGTH_SHORT).show()
            }
            is PermissionResult.PermissionDenied -> {
                Toast.makeText(requireContext(), "Denied", Toast.LENGTH_SHORT).show()
            }
            is PermissionResult.ShowRational -> {
                val alertDialog = AlertDialog.Builder(requireContext())
                    .setMessage("We need permission")
                    .setTitle("Rational")
                    .setPositiveButton("OK") { _, _ ->
                        when (permissionResult.requestCode) {
                            1 -> {
                                coroutineScope.launch(Dispatchers.Main) {
                                    handleResult(
                                        PermissionManager.requestPermissions(
                                            this@CoroutinesFragment,
                                            1,
                                            Manifest.permission.ACCESS_FINE_LOCATION
                                        )
                                    )
                                }
                            }
                            2 -> {
                                coroutineScope.launch(Dispatchers.Main) {
                                    handleResult(
                                        PermissionManager.requestPermissions(
                                            this@CoroutinesFragment,
                                            2,
                                            Manifest.permission.READ_CONTACTS
                                        )
                                    )
                                }
                            }
                            3 -> {
                                coroutineScope.launch(Dispatchers.Main) {
                                    handleResult(
                                        PermissionManager.requestPermissions(
                                            this@CoroutinesFragment,
                                            3,
                                            Manifest.permission.CAMERA
                                        )
                                    )
                                }
                            }
                            4 -> {
                                coroutineScope.launch(Dispatchers.Main) {
                                    handleResult(
                                        PermissionManager.requestPermissions(
                                            this@CoroutinesFragment,
                                            4,
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.READ_CONTACTS,
                                            Manifest.permission.CAMERA
                                        )
                                    )
                                }
                            }
                        }

                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }.create()
                alertDialog.show()
            }
            is PermissionResult.PermissionDeniedPermanently -> {
                Toast.makeText(requireContext(), "Denied permanently", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        parentJob.cancel()
    }
}
