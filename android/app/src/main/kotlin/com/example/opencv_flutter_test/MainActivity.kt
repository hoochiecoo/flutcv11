package com.example.opencv_flutter_test

import android.graphics.Bitmap
import android.graphics.Color
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import java.io.File
import java.io.FileOutputStream

class MainActivity: FlutterActivity() {
    private val CHANNEL = "opencv_channel"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            if (call.method == "processImage") {
                result.success(processImage())
            } else {
                result.notImplemented()
            }
        }
    }

    private fun processImage(): String {
        OpenCVLoader.initDebug()
        val bmp = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bmp)
        canvas.drawColor(Color.LTGRAY)
        val p = android.graphics.Paint().apply { color = Color.MAGENTA; strokeWidth = 10f; style = android.graphics.Paint.Style.STROKE }
        canvas.drawCircle(250f, 250f, 200f, p)

        val src = Mat()
        Utils.bitmapToMat(bmp, src)
        Imgproc.cvtColor(src, src, Imgproc.COLOR_RGB2GRAY)
        Imgproc.Canny(src, src, 50.0, 150.0)
        
        val out = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, out)

        val file = File(cacheDir, "result_v6.png")
        FileOutputStream(file).use { out.compress(Bitmap.CompressFormat.PNG, 100, it) }
        return file.absolutePath
    }
}