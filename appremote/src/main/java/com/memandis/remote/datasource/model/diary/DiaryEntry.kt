package com.memandis.remote.datasource.model.diary

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.ColorInt
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import com.memandis.remote.utils.app.calculateForegroundColorToPair
import java.util.*
import kotlin.collections.HashMap
import com.memandis.remote.R

const val DEFAULT_COLOR = -1
const val DEFAULT_ID = "NO___ID"

/**
 * A container class that represents each entry of a DiaryEntry
 *
 * @property id String                      string that represent an ID of each diary entry
 * @property title String                   title of diary entry
 * @property subtitle String                subtitle of diary entry
 * @property content String                 content string of diary entry
 * @property tags List<DiaryTag>            list of tags associated with each diary entry
 * @property goodBadScore Int               the quantity of Good and Bad: if higher than 0 -> Good, negative -> bad, zero -> neutral
 * @property color Int                      the color integer
 * @property timeCreated Date               the timestamp on which the entry is created
 * @property timeModified Date              the timestamp on which the entry is modified
 * @property images List<DiaryImage>?       list of images that associated with the diary entry
 * @property location Pair<Double, Double>  pair of coordinate that consisted of Latitude and Longitude
 * @constructor                             Default constructor of the DiaryEntry class
 */
class DiaryEntry(
    var id: String,
    var creatorId: String,
    var title: String,
    var subtitle: String,
    var content: String,
    var tags: List<DiaryTag>,
    var ratings: Int,
    @ColorInt
        var color: Int = DEFAULT_COLOR,
    val timeCreated: Date,
    var timeModified: Date,
    var images: List<DiaryImage>?,

    lat: Double,
    long: Double
) {
    var location: Pair<Double, Double> = Pair(lat, long)

    fun toFirebaseData(): FirebaseDiaryEntry {
        val hashMap = if(!images.isNullOrEmpty()) {
            hashMapOf<String, FirebaseDiaryImage>().apply {
                var defaultIdCount = 0
                for(i in images!!.indices) {
                    val key = if(images!![i].id == DEFAULT_ID) {
                        defaultIdCount++.toString()
                    } else {
                        images!![i].id
                    }
                    this[key] = images!![i].toFirebaseData()
                }
            }
        } else {
            null
        }

        return FirebaseDiaryEntry(id, creatorId, title, content, subtitle, tags,
                                  ratings, color, timeCreated.time, timeModified.time, hashMap,
                                  hashMapOf("lat" to location.first, "long" to location.second))
    }

    override fun toString(): String {
        return "DiaryEntry(id='$id', " +
                "creatorId=$creatorId," +
                "timeCreated=$timeCreated, " +
                "timeModified=$timeModified, " +
                "ratings=$ratings, tags=$tags, " +
                "title='$title', " +
                "subtitle='$subtitle', " +
                "content='$content', " +
                "images=$images, " +
                "location=$location)"
    }

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(javaClass != other?.javaClass) return false

        other as DiaryEntry

        if(id != other.id) return false
        if(creatorId != other.creatorId) return false
        if(title != other.title) return false
        if(subtitle != other.subtitle) return false
        if(content != other.content) return false
        if(tags != other.tags) return false
        if(ratings != other.ratings) return false
        if(color != other.color) return false
        if(timeCreated != other.timeCreated) return false
        if(timeModified != other.timeModified) return false
        if(images != other.images) return false
        if(location != other.location) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + creatorId.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + subtitle.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + tags.hashCode()
        result = 31 * result + ratings
        result = 31 * result + color
        result = 31 * result + timeCreated.hashCode()
        result = 31 * result + timeModified.hashCode()
        result = 31 * result + (images?.hashCode() ?: 0)
        result = 31 * result + location.hashCode()
        return result
    }

}

/**
 * An extension function to determine if a color
 * @receiver DiaryEntry target diary entry
 * @return Boolean true if there is a color
 */
fun DiaryEntry.hasSpecifiedColor(): Boolean {
    return this.color != DEFAULT_COLOR || this.color == 0
}

/**
 * Determine foreground and background color based on entry's accent
 * @receiver DiaryEntry target diary entry
 * @param context Context context to read the color resource
 * @return Pair<Int, Int> pair of foreground (Black or White) and background (Diary Entry's color)
 */
fun DiaryEntry.getForegroundAndAccentColor(context: Context): Pair<Int, Int> {
    return if(hasSpecifiedColor()) {
        calculateForegroundColorToPair(this.color)
    } else {
        calculateForegroundColorToPair(context.getColor(R.color.colorPrimaryDark))
    }
}

/**
 * A Firebase version of DiaryEntry for an easier Serialization
 * @property id String                                          A unique diary entry ID string
 * @property title String                                       The title of the entry
 * @property content String                                     The content string of the entry
 * @property subtitle String                                    The subtitle of the entry
 * @property tags List<DiaryTag>                                The list of tags associated with the entry
 * @property goodBadScore Int                                   The quantitative value of Good-Bad score
 * @property color Int                                          The color associated with the diary entry
 * @property timeCreated Long                                   The timestamp showing the creation time of the entry
 * @property timeModified Long                                  The timestamp showing the modification time of the entry
 * @property images HashMap<String, FirebaseDiaryImage>?        The map that stores images of the Diary entry
 * @property location HashMap<String, Double>                   The map that stores coordinate specified for the Diary Entry
 * @constructor
 */
@IgnoreExtraProperties
data class FirebaseDiaryEntry(
    var id: String = " ",
    var creatorId: String= " ",
    var title: String = " ",
    var content: String = " ",
    var subtitle: String = " ",
    var tags: List<DiaryTag> = arrayListOf(),
    var ratings: Int = 0,
    @ColorInt
        var color: Int = DEFAULT_COLOR,
    val timeCreated: Long = 0L,
    var timeModified: Long = 0L,
    var images: HashMap<String, FirebaseDiaryImage>? = HashMap(),
    var location: HashMap<String, Double> = hashMapOf("lat" to 0.0, "long" to 0.0)
) {

    /**
     * Convert this object into DiaryEntry class which is mainly used by the rest of the app except Firebase
     *
     * @return DiaryEntry a Diary Entry object
     */
    fun toNormalData(): DiaryEntry {

        return DiaryEntry(id, creatorId, title, subtitle, content, tags, ratings,
                          color, Date(timeCreated),
                          Date(timeModified),
                          images?.values?.toList()?.map(FirebaseDiaryImage::toNormalData),
                          location["lat"]!!, location["long"]!!)
    }

//    fun serializeToMap(): Int {
//
//    }
}

data class DiaryImage(
    var id: String = DEFAULT_ID,
    val description: String = "",
    val uri: Uri = Uri.EMPTY,
    val timeCreated: Date = Calendar.getInstance().time,
    val entryId: String = "",
    val isUploaded: Boolean = false,
    val imageBitmap: Bitmap? = null,
    var markedForDeletion: Boolean = false
) {
    fun toFirebaseData(): FirebaseDiaryImage {
        return FirebaseDiaryImage(id, description, uri.toString(), timeCreated.time, entryId)
    }
}

class FirebaseDiaryImage(
        var id: String = "",
        val description: String = "",
        val uri: String = Uri.EMPTY.toString(),
        val timeCreated: Long = Calendar.getInstance().timeInMillis,
        var entryId: String = "",
        @get:Exclude var isUploaded: Boolean = false
) {

    @get:Exclude
    val markedForDeletion = false

    fun toNormalData(): DiaryImage {
        return DiaryImage(id, description, Uri.parse(uri), Date(timeCreated), entryId, true)
    }

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(javaClass != other?.javaClass) return false

        other as FirebaseDiaryImage

        if(id != other.id) return false
        if(description != other.description) return false
        if(uri != other.uri) return false
        if(timeCreated != other.timeCreated) return false
        if(entryId != other.entryId) return false
        if(markedForDeletion != other.markedForDeletion) return false
        if(isUploaded != other.isUploaded) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + uri.hashCode()
        result = 31 * result + timeCreated.hashCode()
        result = 31 * result + entryId.hashCode()
        result = 31 * result + markedForDeletion.hashCode()
        result = 31 * result + isUploaded.hashCode()
        return result
    }


    companion object {
        fun getFirebaseStoragePath(entryId: String): String {
            return "image/$entryId/"
        }
    }

}