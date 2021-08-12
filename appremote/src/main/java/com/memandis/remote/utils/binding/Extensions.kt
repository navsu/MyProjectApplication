package com.memandis.remote.utils.binding

import kotlinx.coroutines.channels.SendChannel

/**
 * Tries to send an element to a Channel and ignores the exception.
 */
fun <E> SendChannel<E>.tryOffer(element: E): Boolean = try {
    trySend(element).isSuccess
//    offer(element)
} catch (t: Throwable) {
    false // Ignore
}