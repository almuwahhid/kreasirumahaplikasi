package com.kreasirumahaplikasi.mahasiswakreasi.base

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kreasirumahaplikasi.mahasiswakreasi.R
import com.kreasirumahaplikasi.mahasiswakreasi.utils.ext.hideKeyboard
import lib.alframeworkx.Activity.Interfaces.PermissionResultInterface
import java.util.*


abstract class BaseBottomSheetFragment : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    private var KEY_PERMISSION = 0
    private var permissionResultInterface: PermissionResultInterface? = null
    lateinit var permissionsAsk: Array<String>

    fun askPermissions( permissions:Array<String>): BaseBottomSheetFragment? {
        permissionsAsk = permissions
        return this@BaseBottomSheetFragment
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme)

    @LayoutRes
    protected abstract fun setView(): Int

    protected abstract fun initView(view: View, savedInstanceState: Bundle?)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (setView() != 0) {
            val view = inflater.inflate(setView(), container, false)
            dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            return view
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setOnShowListener { d ->
            val frame =
                dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val behaviour = BottomSheetBehavior.from(frame)
            behaviour.apply {
                state = BottomSheetBehavior.STATE_EXPANDED
                peekHeight = 0
                isHideable = true
                skipCollapsed = true
            }
        }
        initView(view, savedInstanceState)
    }

    open fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!)
        val layoutParams = bottomSheet.layoutParams
        val windowHeight = getWindowHeight()
        if (layoutParams != null) {
            layoutParams.height = windowHeight
        }
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    open fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    override fun onPause() {
        hideKeyboard()
        super.onPause()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == KEY_PERMISSION) {
            var granted = true
            for (grantResult in grantResults) {
                if (!(grantResults.size > 0 && grantResult == PackageManager.PERMISSION_GRANTED)) granted = false
            }
            if (permissionResultInterface != null) {
                if (granted) {
                    permissionResultInterface!!.permissionGranted()
                } else {
                    permissionResultInterface!!.permissionDenied()
                }
            } else {
                Log.e("ManagePermission", "permissionResultInterface callback was null")
            }
        }
    }

    open fun askCompactPermission(permission: String, permissionResultInterface: PermissionResultInterface) {
        KEY_PERMISSION = 200
        permissionsAsk = arrayOf(permission)
        this.permissionResultInterface = permissionResultInterface
        internalRequestPermission(permissionsAsk)
    }

    open fun askCompactPermissions(permissions: Array<String>, permissionResultInterface: PermissionResultInterface) {
        KEY_PERMISSION = 200
        permissionsAsk = permissions
        this.permissionResultInterface = permissionResultInterface
        internalRequestPermission(permissionsAsk)
    }

    private fun internalRequestPermission(permissionAsk: Array<String>) {
        var arrayPermissionNotGranted: Array<String?>
        val permissionsNotGranted = ArrayList<String>()
        for (i in permissionAsk.indices) {
            if (!isPermissionGranted(activity, permissionAsk[i])) {
                permissionsNotGranted.add(permissionAsk[i])
            }
        }
        if (permissionsNotGranted.isEmpty()) {
            if (permissionResultInterface != null) permissionResultInterface!!.permissionGranted()
        } else {
            arrayPermissionNotGranted = arrayOfNulls(permissionsNotGranted.size)
            arrayPermissionNotGranted = permissionsNotGranted.toArray(arrayPermissionNotGranted)
            requestPermissions(arrayPermissionNotGranted, KEY_PERMISSION)
        }
    }

    private fun isPermissionGranted(context: Context?, permission: String?): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(requireContext(), permission!!) == PackageManager.PERMISSION_GRANTED
    }
}