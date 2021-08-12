package com.memandis.remote.utils.app

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Patterns
import java.security.MessageDigest
import java.util.regex.Pattern

class StringUtils private constructor() {

    companion object {

        fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()

        fun String.toSHA256(): String {
            val md = MessageDigest.getInstance("SHA-256")
            md.update(this.toByteArray())
            val digest = md.digest()
            return digest.contentToString()
        }

        fun CharSequence?.isValidUsername(): Boolean {
            val patterns = Pattern.compile("^(?=.{8,20}\$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])\$")
            val a = patterns.matcher(this ?: "")
            return a.find()
        }

        private val HEX_DIGITS = charArrayOf(
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9',
            'A',
            'B',
            'C',
            'D',
            'E',
            'F'
        )
        private val BASE_64_DIGITS = charArrayOf(
            '0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
            'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
            'Z', '+', '/'
        )
        /*----------------------------------normal strings--------------------------------------*/
        /**
         * 判断指定字符是否为空白字符，空白符包含：空格、tab 键、换行符
         *
         * @param s 要判断的字符串
         * @return 当字符串为空或者字符串中所有的字符都是空白字符的时候返回 true
         */
        fun isSpace(s: String?): Boolean {
            if (s == null) return true
            var i = 0
            val len = s.length
            while (i < len) {
                if (!Character.isWhitespace(s[i])) {
                    return false
                }
                ++i
            }
            return true
        }

        /**
         * 指定的字符串是否为空，null 或者长度为 0
         *
         * @param s 字符串
         * @return true 表示为空
         */
        fun isEmpty(s: CharSequence?): Boolean {
            return s == null || s.length == 0
        }

        /**
         * 指定的字符串 [String.trim] 之后是否为空
         *
         * @param s 字符串
         * @return true 表示为空
         */
        fun isTrimEmpty(s: String?): Boolean {
            return s == null || s.trim { it <= ' ' }.length == 0
        }
//
//        /**
//         * 判断两个 CharSequence 是否相等
//         *
//         * @param s1 CharSequence 1
//         * @param s2 CharSequence 2
//         * @return true 表示相等
//         */
//        fun equals(s1: CharSequence?, s2: CharSequence?): Boolean {
//            if (s1 === s2) return true
//            var length: Int
//            return if (s1 != null && s2 != null && s1.length.also { length = it } == s2.length) {
//                if (s1 is String && s2 is String) {
//                    s1 == s2
//                } else {
//                    for (i in 0 until length) {
//                        if (s1[i] != s2[i]) return false
//                    }
//                    true
//                }
//            } else false
//        }
//
//        /**
//         * 忽略大小写之后，判断两个 String 是否相等
//         *
//         * @param s1 String 1
//         * @param s2 String 2
//         * @return true 表示相等
//         */
//        fun equalsIgnoreCase(s1: String?, s2: String?): Boolean {
//            return s1?.equals(s2, ignoreCase = true) ?: (s2 == null)
//        }
//
//        /**
//         * 获取 CharSequence 的长度
//         *
//         * @param s CharSequence
//         * @return null 的话返回 0，否则返回字符串长度
//         */
//        fun length(s: CharSequence?): Int {
//            return s?.length ?: 0
//        }
//
//        /**
//         * 字符串的首字符大写
//         *
//         * @param s 字符串
//         * @return 处理之后的字符串
//         */
//        fun upperFirstLetter(s: String?): String {
//            if (s == null || s.length == 0) return ""
//            if (!Character.isLowerCase(s[0])) return s
//            return (s[0].toInt() - 32) as Char.toString() + s.substring(1)
//        }
//
//        /**
//         * 字符串的首字符小写
//         *
//         * @param s 字符串
//         * @return 处理之后的字符串
//         */
//        fun lowerFirstLetter(s: String?): String {
//            if (s == null || s.length == 0) return ""
//            if (!Character.isUpperCase(s[0])) return s
//            return (s[0].toInt() + 32) as Char.toString() + s.substring(1)
//        }
//
//        /**
//         * 字符串反转
//         *
//         * @param s 字符串
//         * @return 反转之后的字符串
//         */
//        fun reverse(s: String?): String {
//            if (s == null) return ""
//            val len = s.length
//            if (len <= 1) return s
//            val mid = len shr 1
//            val chars = s.toCharArray()
//            var c: Char
//            for (i in 0 until mid) {
//                c = chars[i]
//                chars[i] = chars[len - i - 1]
//                chars[len - i - 1] = c
//            }
//            return String(chars)
//        }
//
//        /**
//         * 获取指定的字节数组对应的十六进制字符串，按照 ASCII 码表计算
//         * 比如 ABCDEFGHIJKLMNOPQRSTUVWXYZ
//         * 将得到 4142434445464748494A4B4C4D4E4F505152535455565758595A
//         *
//         * @param bytes 字节数组
//         * @return 十六进制字符串
//         */
////        fun bytes2HexString(bytes: ByteArray?): String {
////            if (bytes == null) return ""
////            val len = bytes.size
////            if (len <= 0) return ""
////            val ret = CharArray(len shl 1)
////            var i = 0
////            var j = 0
////            while (i < len) {
////
////                // 字节的高八位
////                ret[j++] = HEX_DIGITS[bytes[i] shr 4 and 0x0f]
////                // 字节的低八位
////                ret[j++] = HEX_DIGITS[bytes[i] and 0x0f]
////                i++
////            }
////            return String(ret)
////        }
//
//        /**
//         * 将十六进制字符串转换回字节数组
//         *
//         * @param hexString 十六进制字符串
//         * @return 字节数组
//         */
//        fun hexString2Bytes(hexString: String): ByteArray {
//            val len = hexString.length
//            val data = ByteArray(len / 2)
//            var i = 0
//            while (i < len) {
//                data[i / 2] = ((Character.digit(hexString[i], 16) shl 4)
//                        + Character.digit(hexString[i + 1], 16)).toByte()
//                i += 2
//            }
//            return data
//        }
//
//        /**
//         * 将数字转换成六十四进制字符串
//         *
//         * @param number 数字
//         * @return 字符串
//         */
//        fun long2Base64String(number: Long): String {
//            var number = number
//            val buf = CharArray(64)
//            var charPos = 64
//            val radix = 1 shl 6
//            val mask = radix - 1L // 截取后几位，在 [0,63] 之间
//            do {
//                buf[--charPos] = BASE_64_DIGITS[(number and mask).toInt()]
//                number = number ushr 6
//            } while (number != 0L)
//            return String(buf, charPos, 64 - charPos)
//        }
//
//        /**
//         * 将六十四进制字符串还原回数字
//         *
//         * @param base64String 六十四进制字符串
//         * @return 数字
//         */
//        fun base64String2Long(base64String: String): Long {
//            var result: Long = 0
//            val length = base64String.length
//            for (i in length - 1 downTo 0) {
//                for (j in BASE_64_DIGITS.indices) {
//                    if (base64String[i] == BASE_64_DIGITS[j]) {
//                        result += j.toLong() shl 6 * (base64String.length - 1 - i)
//                    }
//                }
//            }
//            return result
//        }
//
//        /**
//         * 使用指定的字符串将容器中的元素拼接起来
//         *
//         * @param c         容器
//         * @param connector 连接的字符串
//         * @param <T>       容器元素类型
//         * @return          拼接结果
//        </T> */
//        fun <T> connect(c: Collection<T>?, connector: String?): String {
//            return connect(c, connector, object : StringFunction<T>() {
//                fun apply(from: T): String {
//                    return from.toString()
//                }
//            })
//        }
//
//        /**
//         * 将传入的列表按照指定的格式拼接起来
//         *
//         * @param c         容器
//         * @param connector 连接的字符串
//         * @param function  对象到字符串映射格式
//         * @param <T>       对象类型
//         * @return          拼接结果
//        </T> */
////        fun <T> connect(
////            c: Collection<T>?,
////            connector: String?,
////            function: StringFunction<T>
////        ): String {
////            if (c == null || c.isEmpty()) return ""
////            val sb = StringBuilder()
////            var count = 0
////            val size = c.size
////            for (element in c) {
////                if (count++ != size - 1) {
////                    sb.append(function.apply(element)).append(connector)
////                } else {
////                    sb.append(function.apply(element))
////                }
////            }
////            return sb.toString()
////        }
//
//        /*----------------------------------android resources--------------------------------------*/
//        fun getString(@StringRes id: Int): String {
//            return UtilsApp.getApp()!!.resources.getString(id)
//        }
//
//        fun getString(@StringRes id: Int, vararg formatArgs: Any?): String {
//            return UtilsApp.getApp()!!.resources.getString(id, formatArgs)
//        }
//
//        fun getText(@StringRes id: Int): CharSequence {
//            return UtilsApp.getApp()!!.resources.getText(id)
//        }
//
//        fun getQuantityText(@PluralsRes id: Int, quantity: Int): CharSequence {
//            return UtilsApp.getApp()!!.resources.getQuantityText(id, quantity)
//        }
//
//        fun getQuantityString(@PluralsRes id: Int, quantity: Int): String {
//            return UtilsApp.getApp()!!.resources.getQuantityString(id, quantity)
//        }
//
//        fun getQuantityString(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any?): String {
//            return UtilsApp.getApp()!!.resources.getQuantityString(id, quantity, formatArgs)
//        }
//
//        fun getTextArray(@ArrayRes id: Int): Array<CharSequence> {
//            return UtilsApp.getApp()!!.resources.getTextArray(id)
//        }
//
//        fun getStringArray(@ArrayRes id: Int): Array<String> {
//            return UtilsApp.getApp()!!.resources.getStringArray(id)
//        }
//
//        fun format(@StringRes resId: Int, vararg arg: Any?): String {
//            return try {
//                java.lang.String.format(UtilsApp.getApp()!!.getString(resId), arg)
//            } catch (e: Exception) {
//                UtilsApp.getApp()!!.getString(resId)
//            }
//        }
//
//        /**
//         * Get text from html
//         *
//         * @param html the html text
//         * @return     the spanned text
//         */
        fun fromHtml(html: String?): Spanned {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(html)
            }
        }

    }

    /*----------------------------------inner methods--------------------------------------*/
    init {
        throw UnsupportedOperationException("u can't initialize me!")
    }
}