package com.memandis.project

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.memandis.remote.ServiceLocator

fun Fragment.getViewModelFactory(): com.memandis.project.SelfViewModelFactory {
    val repository = ServiceLocator.provideRealtimeDatabaseRepository(this.requireContext())
    return com.memandis.project.SelfViewModelFactory(repository)
}

fun getViewModelFactory(context: Context): com.memandis.project.SelfViewModelFactory {
    val repository = ServiceLocator.provideRealtimeDatabaseRepository(context)
    return com.memandis.project.SelfViewModelFactory(repository)
}

inline fun <reified T : ViewModel> Fragment.getParentViewModel(
    factory: ViewModelProvider.Factory = ViewModelProvider.NewInstanceFactory()
) = ViewModelProviders.of(activity!!, factory).get(T::class.java)

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.viewModels(
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: {
        val application = application ?: throw IllegalArgumentException(
            "ViewModel can be accessed only when Activity is attached"
        )
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    return ViewModelLazy(VM::class, { viewModelStore }, factoryPromise)
}