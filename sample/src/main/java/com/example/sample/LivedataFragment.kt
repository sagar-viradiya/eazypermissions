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
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.easypermissions.common.model.PermissionResult
import com.easypermissions.livedatapermission.PermissionManager
import kotlinx.android.synthetic.main.fragment_ui.*

/**
 * A simple [Fragment] subclass.
 *
 */
class LivedataFragment : Fragment(), PermissionManager.PermissionObserver {

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
            PermissionManager.requestPermissions(this, 1, Manifest.permission.ACCESS_FINE_LOCATION)
        }

        contact_permission_btn.setOnClickListener {
            PermissionManager.requestPermissions(this, 2, Manifest.permission.READ_CONTACTS)
        }

        camera_permission_btn.setOnClickListener {
            PermissionManager.requestPermissions(this, 3, Manifest.permission.CAMERA)
        }

        location_contact_camera_permission_btn.setOnClickListener {
            PermissionManager.requestPermissions(
                this,
                4,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CAMERA
            )
        }
    }

    override fun setupObserver(permissionResultLiveData: LiveData<PermissionResult>) {
        permissionResultLiveData.observe(this, Observer {
            when (it) {
                is PermissionResult.PermissionGranted -> {
                    Toast.makeText(requireContext(), "Granted", Toast.LENGTH_SHORT).show()
                }
                is PermissionResult.PermissionDenied -> {
                    Toast.makeText(requireContext(), "Denied", Toast.LENGTH_SHORT).show()
                }
                is PermissionResult.ShowRational -> {
                    val alertDialogBuilder = AlertDialog.Builder(requireContext())
                        .setMessage("We need permission")
                        .setTitle("Rational")
                        .setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                        }
                    when (it.requestId) {
                        1 -> {
                            alertDialogBuilder
                                .setPositiveButton("OK") { _, _ ->
                                    PermissionManager.requestPermissions(
                                        this,
                                        1,
                                        Manifest.permission.ACCESS_FINE_LOCATION
                                    )
                                }.create().show()
                        }
                        2 -> {
                            alertDialogBuilder
                                .setPositiveButton("OK") { _, _ ->
                                    PermissionManager.requestPermissions(
                                        this,
                                        2,
                                        Manifest.permission.READ_CONTACTS
                                    )
                                }.create().show()
                        }
                        3 -> {
                            alertDialogBuilder
                                .setPositiveButton("OK") { _, _ ->
                                    PermissionManager.requestPermissions(
                                        this,
                                        3,
                                        Manifest.permission.CAMERA
                                    )
                                }.create().show()
                        }
                        4 -> {
                            alertDialogBuilder
                                .setPositiveButton("OK") { _, _ ->
                                    PermissionManager.requestPermissions(
                                        this,
                                        4,
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.READ_CONTACTS,
                                        Manifest.permission.CAMERA
                                    )
                                }.create().show()
                        }
                    }
                }
                is PermissionResult.PermissionDeniedPermanently -> {
                    Toast.makeText(requireContext(), "Denied permanently", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
