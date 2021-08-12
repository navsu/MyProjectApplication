package com.memandis.remote.utils.app

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.graphics.Point
import android.util.Log
import android.view.WindowManager
import java.lang.ref.WeakReference
import java.util.*

//@author WngShhng shouheng2015@gmail.com
class AppUtils private constructor() {
    companion object {
        private val activityStack: MutableList<WeakReference<Activity?>>? = LinkedList()
        private val foreActivityStack = LinkedList<Activity>()

        fun getScreenWidth(context: Context): Int {
            return getPointSize(context).x
        }

        fun getScreenHeight(context: Context): Int {
            return getPointSize(context).y
        }

        private fun getPointSize(context: Context): Point {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            return size
        }

        /*--------------------------------install and uninstall----------------------------------*/
        fun isInstall(pkgName: String?): Boolean {
            val pm = UtilsApp.getApp()!!.packageManager
            return try {
                if (pkgName != null) {
                    pm.getPackageInfo(pkgName, PackageManager.GET_ACTIVITIES)
                }
                true
            } catch (e: NameNotFoundException) {
                e.printStackTrace()
                false
            }
        }
//        /*-------------------------------------get app info-------------------------------------*/
//        /**
//         * API 17
//         *
//         * @return true if above API 17
//         */
//        val isJellyBeanMR1: Boolean
//            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
//
//        /**
//         * API 18
//         *
//         * @return true if above API 18
//         */
//        val isJellyBeanMR2: Boolean
//            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
//
//        /**
//         * API 19
//         *
//         * @return true if above API 18
//         */
//        val isKitKat: Boolean
//            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
//
//        /**
//         * API 21
//         *
//         * @return true if above API 21
//         */
//        val isLollipop: Boolean
//            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
//
//        /**
//         * API 23
//         *
//         * @return true if above API 23
//         */
//        val isMarshmallow: Boolean
//            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
//
//        /**
//         * Get whether this app got the root permission
//         *
//         * @return true if rooted
//         */
//        val isAppRoot: Boolean
//            get() {
//                val result: ShellUtils.CommandResult = ShellUtils.execCmd("echo root", true)
//                if (result.result === 0) return true
//                if (result.errorMsg != null) {
//                    Log.d("AppUtils", "isAppRoot() called" + result.errorMsg)
//                }
//                return false
//            }
//        val isAppDebug: Boolean
//            get() = isAppDebug(UtilsApp.getApp()!!.packageName)
//
//        /**
//         * Get whether given app is a debug app.
//         *
//         * @param pkgName the package name
//         * @return true if it is
//         */
//        fun isAppDebug(pkgName: String?): Boolean {
//            return if (StringUtils.isSpace(pkgName)) false else try {
//                val pm = UtilsApp.getApp()!!.packageManager
//                val ai = pm.getApplicationInfo(pkgName, 0)
//                ai != null && ai.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
//            } catch (e: NameNotFoundException) {
//                e.printStackTrace()
//                false
//            }
//        }
//
//        val isAppSystem: Boolean
//            get() = isAppSystem(UtilsApp.getApp()!!.packageName)
//
//        /**
//         * Get whether given app is a system app.
//         *
//         * @param pkgName the package name
//         * @return true if it is
//         */
//        fun isAppSystem(pkgName: String?): Boolean {
//            return if (StringUtils.isSpace(pkgName)) false else try {
//                val pm = UtilsApp.getApp()!!.packageManager
//                val ai = pm.getApplicationInfo(pkgName, 0)
//                ai != null && ai.flags and ApplicationInfo.FLAG_SYSTEM != 0
//            } catch (e: NameNotFoundException) {
//                e.printStackTrace()
//                false
//            }
//        }
//
//        /**
//         * Get the application class name of current App.
//         *
//         * @return the application name
//         */
//        val applicationName: String?
//            get() = getApplicationName(UtilsApp.getApp()!!.packageName)
//
//        fun getApplicationName(pkgName: String?): String? {
//            return if (StringUtils.isSpace(pkgName)) null else try {
//                val pm = UtilsApp.getApp()!!.packageManager
//                val ai = pm.getApplicationInfo(pkgName, 0)
//                if (ai == null) null else ai.className
//            } catch (e: NameNotFoundException) {
//                e.printStackTrace()
//                null
//            }
//        }
//
//        val appIcon: Drawable?
//            get() = getAppIcon(UtilsApp.getApp()!!.packageName)
//        val appLauncher: String?
//            get() = getAppLauncher(UtilsApp.getApp()!!.packageName)
//
//        /**
//         * Get launcher activity name
//         *
//         * @param pkgName the package name
//         * @return        the launcher name
//         */
//        fun getAppLauncher(pkgName: String?): String? {
//            return if (StringUtils.isSpace(pkgName)) null else try {
//                val pm = UtilsApp.getApp()!!.packageManager
//                val pi = pm.getPackageInfo(pkgName, 0) ?: return null
//                val resolveIntent = Intent(Intent.ACTION_MAIN, null)
//                resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER)
//                resolveIntent.setPackage(pi.packageName)
//                val resolveInfoList = pm.queryIntentActivities(resolveIntent, 0)
//                val resolveInfo = resolveInfoList.iterator().next()
//                resolveInfo?.activityInfo?.name
//            } catch (e: NameNotFoundException) {
//                e.printStackTrace()
//                null
//            }
//        }
//
//        /**
//         * Get icon of given app
//         *
//         * @param pkgName the package name of given app
//         * @return the icon drawable
//         */
//        fun getAppIcon(pkgName: String?): Drawable? {
//            return if (StringUtils.isSpace(pkgName)) null else try {
//                val pm = UtilsApp.getApp()!!.packageManager
//                val pi = pm.getPackageInfo(pkgName, 0)
//                pi?.applicationInfo?.loadIcon(pm)
//            } catch (e: NameNotFoundException) {
//                e.printStackTrace()
//                null
//            }
//        }
//
//        val packageName: String
//            get() = UtilsApp.getApp()!!.packageName
//        val appName: String?
//            get() = getAppName(UtilsApp.getApp()!!.packageName)
//
//        /**
//         * Get app name of given app
//         *
//         * @param pkgName package name of given app
//         * @return the app name
//         */
//        fun getAppName(pkgName: String?): String? {
//            return if (StringUtils.isSpace(pkgName)) "" else try {
//                val pm = UtilsApp.getApp()!!.packageManager
//                val pi = pm.getPackageInfo(pkgName, 0)
//                pi?.applicationInfo?.loadLabel(pm)?.toString()
//            } catch (e: NameNotFoundException) {
//                e.printStackTrace()
//                ""
//            }
//        }
//
//        val appUid: Int
//            get() = getAppUid(UtilsApp.getApp()!!.packageName)
//
//        fun getAppUid(packageName: String?): Int {
//            return try {
//                val pm = UtilsApp.getApp()!!.packageManager
//                val pi = pm.getPackageInfo(packageName, 0)
//                if (pi != null && pi.applicationInfo != null) pi.applicationInfo.uid else 0
//            } catch (e: NameNotFoundException) {
//                e.printStackTrace()
//                0
//            }
//        }
//
//        val appVersionName: String?
//            get() = getAppVersionName(UtilsApp.getApp()!!.packageName)
//
//        fun getAppVersionName(packageName: String?): String? {
//            return if (StringUtils.isSpace(packageName)) "" else try {
//                val pm = UtilsApp.getApp()!!.packageManager
//                val pi = pm.getPackageInfo(packageName, 0)
//                pi?.versionName
//            } catch (e: NameNotFoundException) {
//                e.printStackTrace()
//                ""
//            }
//        }
//
//        val appVersionCode: Int
//            get() = getAppVersionCode(UtilsApp.getApp()!!.packageName)
//
//        fun getAppVersionCode(packageName: String?): Int {
//            return if (StringUtils.isSpace(packageName)) -1 else try {
//                val pm = UtilsApp.getApp()!!.packageManager
//                val pi = pm.getPackageInfo(packageName, 0)
//                pi?.versionCode ?: -1
//            } catch (e: NameNotFoundException) {
//                e.printStackTrace()
//                -1
//            }
//        }
//
//        val appTargetSdkVersion: Int
//            get() = getAppTargetSdkVersion(UtilsApp.getApp()!!.packageName)
//
//        fun getAppTargetSdkVersion(packageName: String?): Int {
//            return try {
//                val pm = UtilsApp.getApp()!!.packageManager
//                val ai = pm.getApplicationInfo(packageName, 0)
//                ai?.targetSdkVersion ?: 0
//            } catch (e: NameNotFoundException) {
//                e.printStackTrace()
//                0
//            }
//        }
//
//        @get:RequiresApi(api = Build.VERSION_CODES.N)
//        val appMinSdkVersion: Int
//            get() = getAppMinSdkVersion(UtilsApp.getApp()!!.packageName)
//
//        @RequiresApi(api = Build.VERSION_CODES.N)
//        fun getAppMinSdkVersion(packageName: String?): Int {
//            return try {
//                val pm = UtilsApp.getApp()!!.packageManager
//                val ai = pm.getApplicationInfo(packageName, 0)
//                ai?.minSdkVersion ?: 0
//            } catch (e: NameNotFoundException) {
//                e.printStackTrace()
//                0
//            }
//        }
//
//        fun getMetaData(key: String?): String? {
//            return getMetaData(UtilsApp.getApp()!!.packageName, key)
//        }
//
//        fun getMetaData(packageName: String?, key: String?): String? {
//            return try {
//                val pm = UtilsApp.getApp()!!.packageManager
//                val ai = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
//                if (ai != null && ai.metaData != null) ai.metaData.getString(key) else null
//            } catch (e: NameNotFoundException) {
//                e.printStackTrace()
//                null
//            }
//        }
//
//        val appSignature: Array<Any>?
//            get() = getAppSignature(UtilsApp.getApp()!!.packageName)
//
//        fun getAppSignature(packageName: String?): Array<Signature>? {
//            return if (StringUtils.isSpace(packageName)) null else try {
//                val pm = UtilsApp.getApp()!!.packageManager
//                @SuppressLint("PackageManagerGetSignatures") val pi =
//                    pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
//                pi?.signatures
//            } catch (e: NameNotFoundException) {
//                e.printStackTrace()
//                null
//            }
//        }
//
//        val appSignatureSHA1: String
//            get() = getAppSignatureSHA1(UtilsApp.getApp()!!.packageName)
//
//        fun getAppSignatureSHA1(packageName: String): String {
//            return getAppSignatureHash(packageName, "SHA1")
//        }
//
//        val appSignatureSHA256: String
//            get() = getAppSignatureSHA256(UtilsApp.getApp()!!.packageName)
//
//        fun getAppSignatureSHA256(packageName: String): String {
//            return getAppSignatureHash(packageName, "SHA256")
//        }
//
//        val appSignatureMD5: String
//            get() = getAppSignatureMD5(UtilsApp.getApp()!!.packageName)
//
//        fun getAppSignatureMD5(packageName: String): String {
//            return getAppSignatureHash(packageName, "MD5")
//        }
//
//        private fun getAppSignatureHash(packageName: String, algorithm: String): String {
//            if (StringUtils.isSpace(packageName)) return ""
//            val signature: Array<Signature>? = getAppSignature(packageName)
//            return if (signature == null || signature.size <= 0) "" else StringUtils.bytes2HexString(
//                EncryptUtils.hashTemplate(
//                    signature[0].toByteArray(), algorithm
//                )
//            )
//                .replaceAll("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0")
//        }
//
//        val appFirstInstallTime: Long
//            get() = getAppFirstInstallTime(UtilsApp.getApp()!!.packageName)
//
//        fun getAppFirstInstallTime(packageName: String?): Long {
//            return if (StringUtils.isSpace(packageName)) -1 else try {
//                val pm = UtilsApp.getApp()!!.packageManager
//                val pi = pm.getPackageInfo(packageName, 0)
//                pi?.firstInstallTime ?: -1
//            } catch (e: NameNotFoundException) {
//                e.printStackTrace()
//                -1
//            }
//        }
//
//        val appLastUpdateTime: Long
//            get() = getAppLastUpdateTime(UtilsApp.getApp()!!.packageName)
//
//        fun getAppLastUpdateTime(packageName: String?): Long {
//            return if (StringUtils.isSpace(packageName)) -1 else try {
//                val pm = UtilsApp.getApp()!!.packageManager
//                val pi = pm.getPackageInfo(packageName, 0)
//                pi?.lastUpdateTime ?: -1
//            } catch (e: NameNotFoundException) {
//                e.printStackTrace()
//                -1
//            }
//        }
//
//        val appSize: Long
//            get() = getAppSize(UtilsApp.getApp()!!.packageName)
//
//        fun getAppSize(packageName: String?): Long {
//            return try {
//                val pm = UtilsApp.getApp()!!.packageManager
//                val ai = pm.getApplicationInfo(packageName, 0)
//                if (ai == null) 0 else File(ai.sourceDir).length()
//            } catch (e: NameNotFoundException) {
//                e.printStackTrace()
//                0
//            }
//        }
//
//        val appSourceDir: String?
//            get() = getAppSourceDir(UtilsApp.getApp()!!.packageName)
//
//        fun getAppSourceDir(packageName: String?): String? {
//            return try {
//                val pm = UtilsApp.getApp()!!.packageManager
//                val ai = pm.getApplicationInfo(packageName, 0)
//                if (ai == null) null else ai.sourceDir
//            } catch (e: NameNotFoundException) {
//                e.printStackTrace()
//                null
//            }
//        }
//        /*-------------------------------------launch and exit-------------------------------------*/
//        /**
//         * Launch app of given package name
//         *
//         * @param pkgName package name
//         */
//        fun launchApp(pkgName: String?) {
//            if (StringUtils.isSpace(pkgName)) return
//            UtilsApp.getApp()!!.startActivity(IntentUtils.getLaunchAppIntent(pkgName, true))
//        }
//
//        /**
//         * Launch app of given package name and get result from it
//         *
//         * @param activity the activity to receive the result
//         * @param pkgName the package name of given app
//         * @param requestCode the request code
//         */
//        fun launchApp(activity: Activity, pkgName: String?, requestCode: Int) {
//            if (StringUtils.isSpace(pkgName)) return
//            activity.startActivityForResult(
//                IntentUtils.getLaunchAppIntent(pkgName, false),
//                requestCode
//            )
//        }
//
//        /**
//         * Relaunch current app
//         */
//        fun relaunchApp() {
//            val i: Intent =
//                IntentUtils.getLaunchAppIntent(UtilsApp.getApp()!!.packageName, false) ?: return
//            val cn = i.component
//            val ii = Intent.makeRestartActivityTask(cn)
//            UtilsApp.getApp()!!.startActivity(ii)
//            System.exit(0)
//        }
//
//        /**
//         * Launch setting page of given app
//         *
//         * @param pkgName the package name of given app
//         */
//        @JvmOverloads
//        fun launchAppSettings(pkgName: String? = UtilsApp.getApp()!!.packageName) {
//            if (StringUtils.isSpace(pkgName)) return
//            UtilsApp.getApp()!!.startActivity(IntentUtils.getLaunchSettingIntent(pkgName, true))
//        }
//
//        /*-----------------------------------activity stack--------------------------------------*/
//        fun exitApplication() {
//            finishActivity()
//        }
//
//
//
//        fun finishActivity() {
//            for (i in activityStack!!.indices.reversed()) {
//                val activity = activityStack[i]
//                if (activity?.get() != null) activity.get()!!.finish()
//            }
//            activityStack.clear()
//        }
//
//        fun finishOtherActivity(activityName: String?) {
//            while (activityStack!!.size > 1) {
//                val remove = activityStack.removeAt(0)
//                val activity = remove.get() ?: continue
//                if (TextUtils.equals(activity.localClassName, activityName)) {
//                    activityStack.add(remove)
//                } else {
//                    activity.finish()
//                }
//            }
//        }
//
//        fun finishOtherActivity() {
//            if (activityStack!!.size > 1) {
//                for (i in activityStack.size - 2 downTo 0) {
//                    val activity = activityStack[i]
//                    if (activity != null && activity.get() != null) {
//                        activity.get()!!.finish()
//                    }
//                }
//            }
//        }
//
//        fun finishLastActivity(finishNum: Int) {
//            if (activityStack == null) return
//            var num = 1
//            val activityStacks = ArrayList<WeakReference<Activity?>>()
//            val size = activityStack.size
//            for (i in size - 1 downTo 0) {
//                val activity = activityStack[i]
//                if (activity.get() != null) {
//                    activity.get()!!.finish()
//                    activityStacks.add(activity)
//                    if (num++ == finishNum) break
//                }
//            }
//            for (activity in activityStacks) {
//                activityStack.remove(activity)
//            }
//            activityStacks.clear()
//        }
//
//        val foreActivity: Activity?
//            get() = if (foreActivityStack.isEmpty()) null else foreActivityStack.peek()

        fun attachActivity(activity: Activity?) {
            val act = WeakReference(activity)
            if (activityStack!!.indexOf(act) == -1) activityStack.add(act)
        }

        fun attachForeActivity(activity: Activity) {
            if (foreActivityStack.indexOf(activity) == -1) foreActivityStack.push(activity)
        }

        fun detachActivity(activity: Activity) {
            val activityStacks = ArrayList<WeakReference<Activity?>>()
            for (i in activityStack!!.indices.reversed()) {
                try {
                    val activityWeakReference = activityStack[i]
                    if (activityWeakReference != null && activityWeakReference.get() === activity) {
                        activityStacks.add(activityWeakReference)
                    }
                } catch (e: Exception) {
                    Log.e("detachActivity error: ", e.toString())
                }
            }
            try {
                if (!activityStacks.isEmpty()) {
                    activityStack.removeAll(activityStacks)
                }
            } catch (e: Exception) {
                Log.e("detachActivity error: ", e.toString())
            }
            activityStacks.clear()
        }

        fun detachForeActivity(activity: Activity) {
            foreActivityStack.remove(activity)
        }

    }

    /*-------------------------------------inner methods----------------------------------------*/
    init {
        throw UnsupportedOperationException("u can't initialize me!")
    }
}