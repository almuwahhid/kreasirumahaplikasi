package com.kreasirumahaplikasi.mahasiswakreasi.app.studentform

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import com.afollestad.vvalidator.form
import com.kreasirumahaplikasi.mahasiswakreasi.R
import com.kreasirumahaplikasi.mahasiswakreasi.base.BaseBottomSheetFragment
import com.kreasirumahaplikasi.mahasiswakreasi.data.entity.Student
import kotlinx.android.synthetic.main.fragment_tambah_student.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class StudentFormFragment: BaseBottomSheetFragment(){
    var isAdd = true

    lateinit var onStudentEvent: OnStudentEvent
    var student : Student? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setView(): Int {
        return R.layout.fragment_tambah_student
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(isAdd){
            tv_title.setText("Tambah Mahasiswa")
        } else {
            tv_title.setText("Edit Mahasiswa")
            edt_nama.setText(student?.name)
            edt_nim.setText(student?.nim)
        }

        form {
            input(edt_nama, "Name") {
                isNotEmpty().description(R.string.error_empty_field)
            }
            input(edt_nim, "NIM") {
                isNotEmpty().description(R.string.error_empty_field)
            }

            submitWith(btn_lanjut) {
                if (it.success()) {
                    student!!.nim = edt_nim.text.toString()
                    student!!.name = edt_nama.text.toString()
                    if(isAdd) onStudentEvent.onAddStudent(student!!)
                    else onStudentEvent.onEditStudent(student!!)
                    dismissAllowingStateLoss()
                }
            }
        }
    }

    companion object{
        public fun newInstance(onStudentEvent: OnStudentEvent): StudentFormFragment {
            val fragment = StudentFormFragment()
            fragment.isAdd = true
            fragment.student = Student()
            fragment.onStudentEvent = onStudentEvent
            return fragment
        }

        public fun newInstance(student: Student, onStudentEvent: OnStudentEvent): StudentFormFragment {
            val fragment = StudentFormFragment()
            fragment.student = student
            fragment.isAdd = false
            fragment.onStudentEvent = onStudentEvent
            return fragment
        }
    }

    interface OnStudentEvent{
        fun onEditStudent(student: Student)
        fun onAddStudent(student: Student)
    }
}