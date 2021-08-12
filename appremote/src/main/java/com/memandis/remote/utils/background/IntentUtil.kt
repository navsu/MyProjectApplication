package com.memandis.remote.utils.background

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.memandis.remote.utils.app.AppConstants

//calling activity in another module

//intent.setClassName(context.getPackageName(), "ir.sibvas.testlibary1.HelloWorldActivity");

//Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.your.packagename");
//if (launchIntent != null) {
//    startActivity(launchIntent);//null pointer check in case package name was not found
//}

//fun invokeMainActivityIntent(context: Context) {
//    val intent = Intent();
//
//    intent.setClassName(context.getPackageName(), "ir.sibvas.testlibary1.HelloWorldActivity");
//
//    startActivity(intent);
//
//}

fun Fragment.invokeImageSelectionIntent() {

    //Create an Intent with action as ACTION_PICK
    // val intent = Intent(Intent.ACTION_PICK)
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)

    // Sets the type as image/*. This ensures only components of type image are selected
    intent.type = "image/*"

    //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
    val mimeTypes = arrayOf("image/jpeg", "image/png")
    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)

    // Launching the Intent
    startActivityForResult(intent, AppConstants.REQUEST_CODE_GALLERY)
//    startActivityForResult(intent, EntryEditFragment.REQUEST_CODE_GALLERY)

//    StartActivityForResult()
//    RequestMultiplePermissions()
//    RequestPermission()
//    TakePicturePreview()
//    TakePicture()
//    TakeVideo()
//    PickContact()
//    CreateDocument()
//    OpenDocumentTree()
//    OpenMultipleDocuments()
//    OpenDocument()
//    GetMultipleContents()
//    GetContent()

}

fun Fragment.invokeVideoSelectionIntent() {

    //Create an Intent with action as ACTION_PICK
    val galleryIntent = Intent( Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)

    // Sets the type as image/*. This ensures only components of type video are selected
    galleryIntent.type = "video/*"

    //We pass an extra array with the accepted mime types.
    // This will ensure only components with these MIME types as targeted.
    val mimeTypes = arrayOf("video/mp4", "video/avi")
    galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
    galleryIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    galleryIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)

    // Launching the Intent
     startActivityForResult(galleryIntent, AppConstants.REQUEST_PICK_FROM_FILE)

}

fun Fragment.invokePDFSelectionIntent() {

    //Create an Intent with action as ACTION_PICK
    val pdfIntent = Intent( Intent.ACTION_GET_CONTENT)

    // Sets the type as image/*. This ensures only components of type video are selected
    pdfIntent.type = "pdf/*"

    //We pass an extra array with the accepted mime types.
    // This will ensure only components with these MIME types as targeted.
//    val mimeTypes = arrayOf("video/mp4", "video/avi")
//    pdfIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
    pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    pdfIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)

    // Launching the Intent
    startActivityForResult( pdfIntent, AppConstants.REQUEST_PICK_FROM_FILE)

//    val intent = Intent()
//    intent.setType ("pdf/*")
//    intent.setAction(Intent.ACTION_GET_CONTENT)
//    startActivityForResult(Intent.createChooser(intent, "Select PDF"), PDF)

//    val intent = Intent()
//    intent.setType ("docx/*")
//    intent.setAction(Intent.ACTION_GET_CONTENT)
//    startActivityForResult(Intent.createChooser(intent, "Select DOCX"), DOCX)

//    val intent = Intent()
//    intent.setType ("audio/*")
//    intent.setAction(Intent.ACTION_GET_CONTENT)
//    startActivityForResult(Intent.createChooser(intent, "Select Audio"), AUDIO)
}