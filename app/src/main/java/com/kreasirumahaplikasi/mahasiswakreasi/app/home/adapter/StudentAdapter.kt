package com.kreasirumahaplikasi.mahasiswakreasi.app.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kreasirumahaplikasi.mahasiswakreasi.R
import com.kreasirumahaplikasi.mahasiswakreasi.data.entity.Student
import kotlinx.android.synthetic.main.adapter_student.view.*
import java.lang.Exception


class StudentAdapter(val context: Context, onStudentAction: OnStudentAction) : RecyclerView.Adapter<StudentAdapter.Holder>() {
    var list : MutableList<Student>
    var isLoading = false
    var onStudentAction: OnStudentAction

    init{
        list = ArrayList()
        this.onStudentAction = onStudentAction
    }

    fun initLoading(isLoading: Boolean) {
        this.isLoading = isLoading
        notifyDataSetChanged()
    }

    fun addData(listdata: List<Student>){
        list.clear()
        list.addAll(listdata)
        notifyDataSetChanged()
    }

    fun updateData(student: Student){
        for(i in 0 until list.size){
            if(list.get(i).id == student.id){
                list.set(i, student)
            }
        }
        notifyDataSetChanged()
    }

    fun deleteData(student: Student){
        for(i in 0 until list.size){
            if(list.get(i).id == student.id){
                list.removeAt(i)
            }
        }
        notifyDataSetChanged()
    }

    fun deleteData(i : Int){
        list.removeAt(i)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val layoutView: View
        layoutView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_student, parent, false)
        return Holder(layoutView, viewType)
    }

    override fun getItemCount(): Int {
        if(isLoading){
            return 3
        } else {
            return list.size
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if(isLoading){
            holder.bindLoading()
        } else {
            val s = list.get(position)
            with(holder.itemView){
                toggle.setInOutAnimation(R.anim.pull_in_right, R.anim.push_out_right)
                adapter.visibility = View.VISIBLE
                loading.visibility = View.GONE
                tv_nama.setText(s.name)
                tv_nim.setText(s.nim)
                adapter.setOnClickListener({
                    list.get(position).isClicked = !s.isClicked
                    notifyDataSetChanged()
                })
                btn_delete.setOnClickListener({
                    onStudentAction.onDeleteStudent(position, s)
                })
                btn_edit.setOnClickListener({
                    onStudentAction.onEditStudent(position, s)
                })
                adapter.setOnLongClickListener {
                    list.get(position).isClicked = !s.isClicked
                    notifyDataSetChanged()
                    true
                }
                showAction(s.isClicked, this)
            }
        }
    }

    private fun showAction(isShow : Boolean, view : View){
        if(isShow){
            view.toggle.displaying(view.lay_action)
            view.adapter.setBackgroundColor(ContextCompat.getColor(context, R.color.grey_200))
        } else {
            if(view.toggle.isDisplaying(view.lay_action)){
                view.toggle.hide(view.lay_action)
            }
            view.adapter.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }
    }

    class Holder (itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {
        fun bindLoading() : Unit = with(itemView) {
            adapter.visibility = View.GONE
            loading.visibility = View.VISIBLE
        }
    }

    interface OnStudentAction{
        fun onEditStudent(position: Int, student: Student)
        fun onDeleteStudent(position: Int, student: Student)
    }
}