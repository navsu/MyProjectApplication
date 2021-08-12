package com.memandis.remote.utils.app

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import java.util.*


class UtilsApp private constructor() {
    /**
     * On foreground state change callback.
     */
    interface OnForegroundChangeListener {
        fun onForegroundChange(isForeground: Boolean)
    }

    companion object {
        private const val TAG = "UtilsApp"
        private var app: Application? = null

        /**
         * Is current app foreground
         *
         * @return is foreground
         */
        var isAppForeGround = false
            private set
        private val onForegroundChangeListeners: MutableList<OnForegroundChangeListener> =
            ArrayList()

        /**
         * Get the application
         *
         * @return the application
         */
        fun getApp(): Application? {
            checkNotNull(app) { "Sorry, you must call UtilsApp#init() method at first!" }
            return app
        }

        /**
         * Initialize with given application, the app is usually used to get the app context.
         *
         * @param app the app used to initialize the utils library.
         */
        fun init(app: Application) {
            Companion.app = app
            app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                private var activityCount = 0
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    AppUtils.attachActivity(activity)
                }

                override fun onActivityStarted(activity: Activity) {
                    activityCount++
                    if (activity != null) {
                        AppUtils.attachForeActivity(activity)
                    }
                }

                override fun onActivityResumed(activity: Activity) {
                    if (!isAppForeGround) {
                        isAppForeGround = true
                        notifyForegroundChange(true)
                    }
                }

                override fun onActivityPaused(activity: Activity) {
                    // no-op
                }

                override fun onActivityStopped(activity: Activity) {
                    if (activity != null) {
                        AppUtils.detachForeActivity(activity)
                    }
                    activityCount--
                    if (activityCount == 0) {
                        isAppForeGround = false
                        notifyForegroundChange(false)
                        Log.i(TAG, "Activity foreground: " + System.currentTimeMillis())
                    }
                }

                override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
                    // no-op
                }

                override fun onActivityDestroyed(activity: Activity) {
                    if (activity != null) {
                        AppUtils.detachActivity(activity)
                    }
                }


            } )
        }

        /**
         * Register app foreground state change listener.
         *
         * @param l listener
         */
        fun registerForegroundChangeListener(l: OnForegroundChangeListener) {
            if (!onForegroundChangeListeners.contains(l)) {
                onForegroundChangeListeners.add(l)
            }
        }

        /**
         * Unregister app foreground state change listener.
         *
         * @param l listener
         */
        fun unRegisterForegroundChangeListener(l: OnForegroundChangeListener) {
            onForegroundChangeListeners.remove(l)
        }

        private fun notifyForegroundChange(isForeGround: Boolean) {
            for (l in onForegroundChangeListeners) {
                l.onForegroundChange(isForeGround)
            }
        }
    }

    init {
        throw UnsupportedOperationException("u can't initialize me!")
    }
}