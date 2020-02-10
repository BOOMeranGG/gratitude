package com.orange_infinity.gratitude

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.AtomicFile
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

const val TAG = "MAIN_TAG"
const val MAIN_FOLDER = "gratitude"

fun showFcmToken(context: Context) {
    FirebaseInstanceId.getInstance().instanceId
        .addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.i(TAG, "getInstanceId failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result?.token
            Log.i(TAG, token)
            Toast.makeText(context, token, Toast.LENGTH_SHORT).show()
        })
}

/**
 * Создаёт по названию картинки bitmap
 * @param fileName  - название изображения
 * @return          - итоговый bitmap, или null, если файла нет/не получилось создать
 */
fun readBitmapFromDisk(fileName: String): Bitmap? {
    val source = File(getGalleryPath() + fileName + ".jpg")
    var bitmap: Bitmap? = null

    try {
        val atomicFile = AtomicFile(source)
        bitmap = BitmapFactory.decodeFile(source.absolutePath)
        val fos = atomicFile.startWrite()
        val oos = BufferedOutputStream(fos)
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, oos)

        oos.flush()
        atomicFile.finishWrite(fos)

        return bitmap
    } catch (e: FileNotFoundException) {
        Log.e(TAG, "Can not find file, error: $e")
    } catch (e: Exception) {
        Log.e(TAG, "Error: $e")
    }

    return bitmap
}

fun saveImageToGallery(bmp: Bitmap, fileName: String) {
    try {
        val dest = File(getGalleryPath() + MAIN_FOLDER)
        val mkdir = dest.mkdirs()
        val mkdir2 = dest.mkdir()

        val fileImg = File("${getGalleryPath()}$fileName.jpg")
        val out = FileOutputStream(fileImg)
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, out)

        out.flush()
        out.close()
        Log.i(TAG, "Save image to: " + dest.path)
    } catch (e: Exception) {
        Log.i(TAG, "Ошибка при сохранении. Повторите попытку. Error: $e")
    }

}

fun getGalleryPath(): String {
    //return "${Environment.getExternalStorageDirectory()}/${Environment.DIRECTORY_DCIM}/"
    return "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath}/"
}