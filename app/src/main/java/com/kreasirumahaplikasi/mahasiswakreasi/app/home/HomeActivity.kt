package com.kreasirumahaplikasi.mahasiswakreasi.app.home

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.kreasirumahaplikasi.mahasiswakreasi.R
import com.kreasirumahaplikasi.mahasiswakreasi.app.home.adapter.StudentAdapter
import com.kreasirumahaplikasi.mahasiswakreasi.app.studentform.StudentFormFragment
import com.kreasirumahaplikasi.mahasiswakreasi.data.entity.Student
import com.kreasirumahaplikasi.mahasiswakreasi.utils.RecyclerScroll
import com.kreasirumahaplikasi.mahasiswakreasi.utils.ext.hideKeyboard
import com.kreasirumahaplikasi.mahasiswakreasi.utils.ext.showKeyboard
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.helper_base.*
import kotlinx.android.synthetic.main.toolbar_base.*
import lib.alframeworkx.Activity.ActivityGeneral
import lib.alframeworkx.utils.AlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class HomeActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var adapter : StudentAdapter
    private val viewModel by viewModel<HomeViewModel>()
    var linearLayout : LinearLayoutManager? = null
    var menu : Menu? = null
    var isSearch = false
    private var subject_edittext: PublishSubject<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar.let {
            it!!.setDisplayHomeAsUpEnabled(false)
            it!!.setTitle("")
        }

        linearLayout = LinearLayoutManager(this@HomeActivity)
        rv.layoutManager = linearLayout
        rv.addOnScrollListener(object : RecyclerScroll(linearLayout!!) {
            override fun hide() {
                fab.hide()
            }

            override fun show() {
                fab.show()
            }

            override fun loadMore() {

            }
        })
        adapter = StudentAdapter(this@HomeActivity, object : StudentAdapter.OnStudentAction{
            override fun onEditStudent(position: Int, student: Student) {
                val studentForm = StudentFormFragment.newInstance(student, object : StudentFormFragment.OnStudentEvent{
                    override fun onEditStudent(s: Student) {
                        viewModel.edit(s)
                        Handler().postDelayed({
                            viewModel.gets()
                        }, 500)
                    }

                    override fun onAddStudent(student: Student) {

                    }
                })
                studentForm.show(supportFragmentManager, "Edit Mahasiswa")
            }

            override fun onDeleteStudent(position: Int, student: Student) {
                AlertDialogBuilder(this@HomeActivity,
                    "Apakah Anda yakin ingin menghapus data ini?",
                    "Ya",
                    "Tidak",
                    object : AlertDialogBuilder.OnAlertDialog{
                        override fun onPositiveButton(dialog: DialogInterface?) {
                            viewModel.delete(student)
                            adapter.deleteData(position)
                            initComponentDatas()
                        }

                        override fun onNegativeButton(dialog: DialogInterface?) {

                        }

                    })
            }

        })
        edt_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                subject_edittext!!.onNext(p0.toString())
            }

        })
        rv.adapter = adapter
        btn_tambah_mahasiswa.setOnClickListener(this)
        fab.setOnClickListener(this)
        observeViewModel()
        observeRxSearch()
        viewModel.gets()
    }

    fun observeViewModel(){
        viewModel.apply {
            isLoading.observe(this@HomeActivity, Observer {
                if(it!!){
                    adapter.initLoading(true)
                } else {
                    adapter.initLoading(false)
                }
            })

            result.observe(this@HomeActivity, Observer {
                adapter.addData(it)
                initComponentDatas()
            })
        }
    }

    private fun observeRxSearch(){
        subject_edittext = PublishSubject.create()
        subject_edittext!!.debounce(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            // Be notified on the main thread
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : io.reactivex.Observer<String> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(s: String) {
                    if(s.equals("")){
                        viewModel.gets()
                    } else {
                        viewModel.search(s)
                    }
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {

                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        this.menu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_search){
            initSearch()
        }
        return super.onOptionsItemSelected(item)
    }

    fun initSearch(){
        if(!isSearch){
            tv_title.visibility = View.GONE
            edt_search.visibility = View.VISIBLE
            showKeyboard(edt_search)
            this.menu!!.findItem(R.id.action_search).setIcon(R.drawable.ic_baseline_close_24)
            isSearch = true
        } else {
            if(!edt_search.text.toString().equals("")){
                edt_search.setText("")
            } else {
                hideKeyboard(edt_search)
                edt_search.visibility = View.GONE
                tv_title.visibility = View.VISIBLE
                this.menu!!.findItem(R.id.action_search).setIcon(R.drawable.ic_baseline_search_24)
                isSearch = false
            }
        }
    }

    fun initComponentDatas(){
        if(adapter.list.size == 0){
            fab.hide()
            if(edt_search.text.toString().equals("")){
                helper_notfound.visibility = View.VISIBLE
                helper_search_notfound.visibility = View.GONE
            } else {
                helper_notfound.visibility = View.GONE
                helper_search_notfound.visibility = View.VISIBLE
            }
        } else {
            fab.show()
            helper_notfound.visibility = View.GONE
            helper_search_notfound.visibility = View.GONE
        }
    }

    override fun onClick(p0: View?) {
        if(p0!!.id == R.id.btn_tambah_mahasiswa || p0!!.id == R.id.fab ){
            val studentForm = StudentFormFragment.newInstance(object : StudentFormFragment.OnStudentEvent{
                override fun onEditStudent(s: Student) {
                    viewModel.edit(s)
                    Handler().postDelayed({
                        viewModel.gets()
                    }, 500)
                }

                override fun onAddStudent(s: Student) {
                    viewModel.add(s)
                    Handler().postDelayed({
                        viewModel.gets()
                    }, 500)
                }
            })
            studentForm.show(supportFragmentManager, "Edit Mahasiswa")
        }
    }
}