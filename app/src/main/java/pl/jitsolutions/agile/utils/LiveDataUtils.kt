package pl.jitsolutions.agile.utils

import androidx.lifecycle.MutableLiveData

inline fun <reified T> mutableLiveData(initialValue: T): MutableLiveData<T> {
    return MutableLiveData<T>().apply { value = initialValue }
}