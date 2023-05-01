package com.neotica.storyapp.util

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import kotlin.math.roundToInt

private const val FILENAME_FORMAT = "dd-MMM-yyyy"

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun createTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun rotateImage(img: Bitmap, degree: Int): Bitmap {
    val matrix = Matrix().apply { postRotate(degree.toFloat()) }
    val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
    img.recycle()
    return rotatedImg
}

@Throws(IOException::class)
fun handleSamplingAndRotationBitmap(context: Context, selectedImage: Uri): Bitmap {
    val MAX_HEIGHT = 1024
    val MAX_WIDTH = 1024
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    }
    context.contentResolver.openInputStream(selectedImage)?.use { imageStream ->
        BitmapFactory.decodeStream(imageStream, null, options)
    }

// Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT)

// Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false
    var img = context.contentResolver.openInputStream(selectedImage)?.use { imageStream ->
        BitmapFactory.decodeStream(imageStream, null, options)
    }

    img = rotateImageIfRequired(img!!, selectedImage)
    return img
}

private fun calculateInSampleSize(
    options: BitmapFactory.Options,
    reqWidth: Int, reqHeight: Int
): Int {
    // Raw height and width of image
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        // Calculate ratios of height and width to requested height and width
        val heightRatio = (height.toFloat() / reqHeight.toFloat()).roundToInt()
        val widthRatio = (width.toFloat() / reqWidth.toFloat()).roundToInt()

        // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
        // with both dimensions larger than or equal to the requested height and width.
        inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio

        val totalPixels = (width * height).toFloat()
        // Anything more than 2x the requested pixels we'll sample down further
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }
    }
    return inSampleSize
}

@Throws(IOException::class)
fun rotateImageIfRequired(img: Bitmap, selectedImage: Uri): Bitmap {
    val ei = ExifInterface(selectedImage.path!!)

    return when (ei.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    )) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270)
        else -> img
    }
}

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}

fun reduceFileImage(file: File): File {
    val bitmap = BitmapFactory.decodeFile(file.path)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > 1000000)
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}

fun formatDateTime(isoDateString: String): String {
    val dateTime = ZonedDateTime.parse(isoDateString)
    val dateFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
    return dateFormatter.format(dateTime)
}