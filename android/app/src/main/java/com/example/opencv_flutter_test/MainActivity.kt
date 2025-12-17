package com.example.opencv_flutter_test

import android.graphics.Bitmap
import android.graphics.Color
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import java.io.File
import java.io.FileOutputStream

class MainActivity: FlutterActivity() {
    private val CHANNEL = "opencv_channel"

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            if (call.method == "processImage") {
                val path = processImage()
                result.success(path)
            } else {
                result.notImplemented()
            }
        }
    }

    private fun processImage(): String {
        OpenCVLoader.initDebug()

        val bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        canvas.drawColor(Color.BLUE)
        val paint = android.graphics.Paint().apply { color = Color.YELLOW }
        canvas.drawRect(100f, 100f, 400f, 400f, paint)

        val src = Mat()
        Utils.bitmapToMat(bitmap, src)
        val gray = Mat()
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_RGB2GRAY)
        Imgproc.GaussianBlur(gray, gray, Size(5.0, 5.0), 0.0)
        val edges = Mat()
        Imgproc.Canny(gray, edges, 80.0, 100.0)

        val resultBitmap = Bitmap.createBitmap(edges.cols(), edges.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(edges, resultBitmap)

        val file = File(cacheDir, "opencv_result.png")
        FileOutputStream(file).use { out ->
            resultBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        src.release()
        gray.release()
        edges.release()

        return file.absolutePath
    }
}