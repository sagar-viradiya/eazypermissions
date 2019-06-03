package com.example.sample


import android.Manifest
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.easypermissions.common.model.PermissionResult
import com.easypermissions.coroutinespermission.PermissionManager
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
                    handleResult(PermissionManager.requestPermissions(
                        this@CoroutinesFragment, 1,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ))
                }
            }
        }

        contact_permission_btn.setOnClickListener {
            coroutineScope.launch {
                withContext(Dispatchers.Main) {
                    handleResult(PermissionManager.requestPermissions(
                        this@CoroutinesFragment, 2,
                        Manifest.permission.READ_CONTACTS
                    ))
                }
            }
        }

        location_contact_permission_btn.setOnClickListener {
            coroutineScope.launch {
                withContext(Dispatchers.Main) {
                    handleResult(PermissionManager.requestPermissions(
                        this@CoroutinesFragment, 3,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_CONTACTS
                    ))
                }
            }
        }

    }

    private fun handleResult(permissionResult: PermissionResult) {
        when(permissionResult) {
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
                        when(permissionResult.requestId) {
                            1 -> {
                                coroutineScope.launch(Dispatchers.Main) {
                                    handleResult(PermissionManager.requestPermissions(
                                        this@CoroutinesFragment,
                                        1,
                                        Manifest.permission.ACCESS_FINE_LOCATION
                                    ))
                                }
                            }
                            2 -> {
                                coroutineScope.launch(Dispatchers.Main) {
                                    handleResult(PermissionManager.requestPermissions(
                                        this@CoroutinesFragment,
                                        2,
                                        Manifest.permission.READ_CONTACTS
                                    ))
                                }
                            }
                            3 -> {
                                coroutineScope.launch(Dispatchers.Main) {
                                    handleResult(PermissionManager.requestPermissions(
                                        this@CoroutinesFragment,
                                        3,
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.READ_CONTACTS
                                    ))
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
