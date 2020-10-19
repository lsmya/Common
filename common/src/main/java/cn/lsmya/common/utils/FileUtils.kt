package cn.lsmya.common.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import java.io.*


//将文件保存到公共的媒体文件夹
//这里的filepath不是绝对路径，而是某个媒体文件夹下的子路径，和沙盒子文件夹类似
//这里的filename单纯的指文件名，不包含路径
fun saveBitmap(fileName: String, bitmap: Bitmap, context: Context) {
    val outputStream = getBitmapOutputStream(fileName, context)
    bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
    outputStream?.let {
        it.flush()
        it.close()
    }

}

fun saveBase64(fileName: String, base64: String?, context: Context) {
    val outputStream = getBitmapOutputStream(fileName, context) ?: return
    val decode = Base64.decode(base64, Base64.NO_WRAP)
    try {
        outputStream.write(decode)
        outputStream.flush()
        outputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

fun getBitmapOutputStream(fileName: String, context: Context): OutputStream? {
    try {
        //设置保存参数到ContentValues中
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, fileName)
        //设置文件名
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        //兼容Android Q和以下版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //android Q中不再使用DATA字段，而用RELATIVE_PATH代替
            //RELATIVE_PATH是相对路径不是绝对路径
            //DCIM是系统文件夹，关于系统文件夹可以到系统自带的文件管理器中查看，不可以写没存在的名字
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            //contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Music/signImage");
        } else {
            val myCaptureFile = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + fileName
            )
            val bos = BufferedOutputStream(FileOutputStream(myCaptureFile))
            bos.flush()
            bos.close()
            contentValues.put(
                MediaStore.Images.Media.DATA,
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + fileName
            )
        }
        //设置文件类型
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        //执行insert操作，向系统文件夹中添加文件
        //EXTERNAL_CONTENT_URI代表外部存储器，该值不变
        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            //若生成了uri，则表示该文件添加成功
            //使用流将内容写入该uri中即可
            return resolver.openOutputStream(it)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}
