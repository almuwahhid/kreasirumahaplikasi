package com.kreasirumahaplikasi.mahasiswakreasi.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    val isError = MutableLiveData<Throwable>()
    val isEmpty = MutableLiveData<Boolean>()
}