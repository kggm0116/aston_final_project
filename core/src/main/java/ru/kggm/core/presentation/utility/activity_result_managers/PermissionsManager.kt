package ru.kggm.core.presentation.utility.activity_result_managers

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

class PermissionsManager(
    private val activityOrFragment: ActivityResultCaller
) {
    fun requestPermission(
        permission: String,
        onDenied: () -> Unit = { },
        onGranted: () -> Unit = { },
    ) {
        onPermissionGranted = onGranted
        onPermissionDenied = onDenied
        when (activityOrFragment) {
            is AppCompatActivity -> {
                when {
                    ActivityCompat.checkSelfPermission(
                        activityOrFragment, permission
                    ) == PackageManager.PERMISSION_GRANTED -> onGranted()
                    activityOrFragment.shouldShowRequestPermissionRationale(permission) -> {
                        permissionRequestLauncher.launch(permission)
                    }
                    else -> onDenied()
                }
            }
            is Fragment -> {
                when {
                    ActivityCompat.checkSelfPermission(
                        activityOrFragment.requireContext(), permission
                    ) == PackageManager.PERMISSION_GRANTED -> onGranted()
                    activityOrFragment.shouldShowRequestPermissionRationale(permission) -> {
                        permissionRequestLauncher.launch(permission)
                    }
                    else -> {
                        permissionRequestLauncher.launch(permission)
//                        onDenied()
                    }
                }

            }
            else -> throw IllegalStateException(
                "PermissionsManager received neither activity nor fragment in constructor"
            )
        }
    }

    fun requestPermissions(
        permissions: List<String>,
        onDenied: (List<String>) -> Unit = { },
        onGranted: () -> Unit = { },
    ) {
        onPermissionsGranted = onGranted
        onPermissionsDenied = onDenied
        when (activityOrFragment) {
            is Activity -> {
                when {
                    permissions.all {
                        ActivityCompat.checkSelfPermission(
                            activityOrFragment,
                            it
                        ) == PackageManager.PERMISSION_GRANTED
                    } -> onGranted()
                    permissions.any {
                        activityOrFragment.shouldShowRequestPermissionRationale(it)
                    } -> permissionsRequestLauncher.launch(permissions.toTypedArray())
                    else -> onDenied(permissions)
                }
            }
            is Fragment -> {
                when {
                    permissions.all {
                        ActivityCompat.checkSelfPermission(
                            activityOrFragment.requireContext(),
                            it
                        ) == PackageManager.PERMISSION_GRANTED
                    } -> onGranted()
                    permissions.any {
                        activityOrFragment.shouldShowRequestPermissionRationale(it)
                    } -> permissionsRequestLauncher.launch(permissions.toTypedArray())
                    else -> onDenied(permissions)
                }
            }
            else -> throw IllegalStateException(
                "PermissionsManager received neither activity nor fragment in constructor"
            )
        }
    }

    private var onPermissionGranted: () -> Unit = { }
    private var onPermissionDenied: () -> Unit = { }
    private var onPermissionsDenied: (List<String>) -> Unit = { }
    private var onPermissionsGranted: () -> Unit = { }

    private val permissionRequestLauncher: ActivityResultLauncher<String> =
        when (activityOrFragment) {
            is AppCompatActivity -> {
                activityOrFragment.registerForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { granted ->
                    if (granted) onPermissionGranted()
                    else onPermissionDenied()
                }
            }
            is Fragment -> {
                activityOrFragment.registerForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { granted ->
                    if (granted) onPermissionGranted()
                    else onPermissionDenied()
                }
            }
            else -> throw IllegalStateException(
                "PermissionsManager received neither activity nor fragment in constructor"
            )
        }

    private val permissionsRequestLauncher: ActivityResultLauncher<Array<String>> =
        activityOrFragment.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { results ->
            results
                .filterValues { granted -> !granted }
                .let { deniedPermissions ->
                    if (deniedPermissions.any())
                        onPermissionsDenied(deniedPermissions.keys.toList())
                    else
                        onPermissionsGranted()
                }
        }
}
