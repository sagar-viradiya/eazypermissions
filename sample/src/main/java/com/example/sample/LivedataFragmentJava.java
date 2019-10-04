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

package com.example.sample;


import android.Manifest;
import android.app.AlertDialog;
import androidx.lifecycle.LiveData;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.eazypermissions.common.model.PermissionResult;
import com.eazypermissions.livedatapermission.PermissionManager;
import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class LivedataFragmentJava extends Fragment implements PermissionManager.PermissionObserver {


    public LivedataFragmentJava() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ui, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.location_permission_btn).setOnClickListener(view1 ->
                PermissionManager.requestPermissions(this, 1, Manifest.permission.ACCESS_FINE_LOCATION)
        );
        view.findViewById(R.id.contact_permission_btn).setOnClickListener(view1 ->
                PermissionManager.requestPermissions(this, 2, Manifest.permission.READ_CONTACTS)
        );
        view.findViewById(R.id.camera_permission_btn).setOnClickListener(view1 ->
                PermissionManager.requestPermissions(this, 3, Manifest.permission.CAMERA)
        );
        view.findViewById(R.id.location_contact_camera_permission_btn).setOnClickListener(view1 ->
                PermissionManager.requestPermissions(
                        this,
                        4,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.CAMERA)
        );
    }

    @Override
    public void setupObserver(@NotNull LiveData<PermissionResult> permissionResultLiveData) {
        permissionResultLiveData.observe(this, permissionResult -> {
            if (permissionResult instanceof PermissionResult.PermissionGranted) {
                Toast.makeText(requireContext(), "Granted", Toast.LENGTH_SHORT).show();
            } else if (permissionResult instanceof PermissionResult.PermissionDenied) {
                Toast.makeText(requireContext(), "Denied", Toast.LENGTH_SHORT).show();
            } else if (permissionResult instanceof PermissionResult.PermissionDeniedPermanently) {
                Toast.makeText(requireContext(), "Denied permanently", Toast.LENGTH_SHORT).show();
            } else if (permissionResult instanceof PermissionResult.ShowRational) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext())
                        .setMessage("We need permission")
                        .setTitle("Rational")
                        .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
                switch (permissionResult.getRequestCode()) {
                    case 1:
                        alertDialogBuilder.setPositiveButton("OK", (dialogInterface, i) ->
                                PermissionManager.requestPermissions(
                                        this,
                                        1,
                                        Manifest.permission.ACCESS_FINE_LOCATION)
                        ).create().show();
                        break;
                    case 2:
                        alertDialogBuilder.setPositiveButton("OK", (dialogInterface, i) ->
                                PermissionManager.requestPermissions(
                                        this,
                                        2,
                                        Manifest.permission.READ_CONTACTS)
                        ).create().show();
                        break;
                    case 3:
                        alertDialogBuilder.setPositiveButton("OK", (dialogInterface, i) ->
                                PermissionManager.requestPermissions(
                                        this,
                                        3,
                                        Manifest.permission.CAMERA)
                        ).create().show();
                        break;
                    case 4:
                        alertDialogBuilder.setPositiveButton("OK", (dialogInterface, i) ->
                                PermissionManager.requestPermissions(
                                        this,
                                        4,
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.READ_CONTACTS,
                                        Manifest.permission.CAMERA)
                        ).create().show();
                        break;
                }
            }
        });
    }
}
