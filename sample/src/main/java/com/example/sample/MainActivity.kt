package com.example.sample

import android.Manifest
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Toast
import com.example.livedatapermission.PermissionManager
import com.example.livedatapermission.PermissionResult
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), PermissionManager.PermissionObserver {

    companion object {
        const val TAG = "MainActivity"
    }

    override fun setupObserver(permissionResultLiveData: LiveData<PermissionResult>) {
        permissionResultLiveData.observe(this, Observer<PermissionResult> {
            when (it) {
                is PermissionResult.PermissionGranted -> {
                    if (it.requestId == 1) {
                        Log.i(TAG, "Permission granted")
                        Toast.makeText(this@MainActivity, "Permission granted", Toast.LENGTH_SHORT).show()
                    }
                }
                is PermissionResult.PermissionDenied -> {
                    if (it.requestId == 1) {
                        Log.i(TAG, "Permission denied")
                        Toast.makeText(this@MainActivity, "Permission denied", Toast.LENGTH_SHORT).show()
                    }
                }
                is PermissionResult.PermissionDeniedPermanently -> {
                    if (it.requestId == 1) {
                        Log.i(TAG, "Permission denied permanently")
                        Toast.makeText(this@MainActivity, "Permission denied permanently", Toast.LENGTH_SHORT).show()
                    }
                }
                is PermissionResult.ShowRational -> {
                    if (it.requestId == 1) {
                        val alertDialog = AlertDialog.Builder(this@MainActivity)
                            .setMessage("We need permission")
                            .setTitle("Rational")
                            .setPositiveButton("OK") { _, _ ->
                                PermissionManager.requestPermissions(
                                    this@MainActivity,
                                    1,
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.READ_CONTACTS
                                )
                            }
                            .setNegativeButton("Cancel") { dialog, _ ->
                                dialog.dismiss()
                            }.create()
                        alertDialog.show()
                        Log.i(TAG, "Show rational")
                    }
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            PermissionManager.requestPermissions(
                this@MainActivity,
                1,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_CONTACTS
            )
        }
    }
}
