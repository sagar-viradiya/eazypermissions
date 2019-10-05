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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.eazypermissions.common.model.PermissionResult
import com.eazypermissions.dsl.extension.requestPermissions
import kotlinx.android.synthetic.main.fragment_ui.*

/**
 * A simple [Fragment] subclass.
 */
class DSLFragment : Fragment() {

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
            requestPermissions(Manifest.permission.ACCESS_FINE_LOCATION) {
                requestCode = 1
                resultCallback = {
                    handlePermissionsResult(this)
                }
            }
        }

        contact_permission_btn.setOnClickListener {
            requestPermissions(Manifest.permission.READ_CONTACTS) {
                requestCode = 2
                resultCallback = {
                    handlePermissionsResult(this)
                }
            }
        }

        camera_permission_btn.setOnClickListener {
            requestPermissions(Manifest.permission.CAMERA) {
                requestCode = 3
                resultCallback = {
                    handlePermissionsResult(this)
                }
            }
        }

        location_contact_camera_permission_btn.setOnClickListener {
            requestPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CAMERA
            ) {
                requestCode = 4
                resultCallback = {
                    handlePermissionsResult(this)
                }
            }
        }
    }

    private fun handlePermissionsResult(permissionResult: PermissionResult) {
        when (permissionResult) {
            is PermissionResult.PermissionGranted -> {
                Toast.makeText(requireContext(), "Granted!", Toast.LENGTH_LONG).show()
            }
            is PermissionResult.PermissionDenied -> {
                Toast.makeText(requireContext(), "Denied", Toast.LENGTH_SHORT).show()
            }
            is PermissionResult.ShowRational -> {
                AlertDialog.Builder(requireContext())
                    .setMessage("We need permission")
                    .setTitle("Rational")
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("OK") { _, _ ->
                        val permissions = when (permissionResult.requestCode) {
                            1 -> {
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                            2 -> {
                                arrayOf(Manifest.permission.READ_CONTACTS)
                            }
                            3 -> {
                                arrayOf(Manifest.permission.CAMERA)
                            }
                            4 -> {
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.READ_CONTACTS,
                                    Manifest.permission.CAMERA
                                )
                            }
                            else -> {
                                arrayOf()
                            }
                        }
                        requestPermissions(*permissions) {
                            requestCode = permissionResult.requestCode
                            resultCallback = {
                                handlePermissionsResult(this)
                            }
                        }
                    }
                    .create()
                    .show()
            }
            is PermissionResult.PermissionDeniedPermanently -> {
                Toast.makeText(requireContext(), "Denied permanently", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}
