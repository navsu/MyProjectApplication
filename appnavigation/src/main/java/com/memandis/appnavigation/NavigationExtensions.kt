package com.memandis.appnavigation

import android.os.Bundle
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions

const val REQ_KEY_MAIN_DESTINATION = "mainNavigationResult"

private const val PARAM_DATA = "bundleData"

/**
 * Indicates the desire to open a different destination
 */
fun Fragment.navigateToDestination(destination : Destination) {
    requireActivity().supportFragmentManager.setFragmentResult(
        REQ_KEY_MAIN_DESTINATION, bundleOf(PARAM_DATA to destination))
}

/**
 * Some sugar to simplify how the fragment listener is set.
 * In this way we use a fixed key for navigation events while the fragment can still use Result API
 * to deliver other results which are not related to navigation
 */
fun FragmentManager.setFragmentNavigationListener(lifecycleOwner: LifecycleOwner,
      listener: (destination : Destination) -> Any) {
    setFragmentResultListener(REQ_KEY_MAIN_DESTINATION, lifecycleOwner) { _, bundle ->
        val destination = bundle.getParcelable<Destination>(PARAM_DATA)!!
        listener.invoke(destination)
    }
}

/**
 * Shares a result with will be delivered to MainActivity.
 * For some reason using parentFragmentManager does not deliver the result.
 */
fun Fragment.setMainFragmentResult(requestKey: String, bundle: Bundle) {
    requireActivity().supportFragmentManager.setFragmentResult(requestKey, bundle)
}

//DEEP Link

fun buildDeepLink(destination: DeepLinkDestination) =
    NavDeepLinkRequest.Builder
        .fromUri(destination.address.toUri())
        .build()

fun NavController.deepLinkNavigateTo(
    deepLinkDestination: DeepLinkDestination,
    popUpTo: Boolean = false
) {
    val builder = NavOptions.Builder()

    if (popUpTo) {
//        builder.setPopUpTo(graph.startDestination, true)
    }

    navigate(
        buildDeepLink(deepLinkDestination),
        builder.build()
    )
}

sealed class DeepLinkDestination(val address: String) {
    class Dashboard(msg: String) : DeepLinkDestination("example://dashboard/exampleArgs?msg=${msg}")
    object Next : DeepLinkDestination("example://next")
}