package com.example.prjct_movmnt

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PointF
import android.os.Bundle
import android.util.Size
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.material3.MaterialTheme
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.prjct_movmnt.data.MovementReading
import com.example.prjct_movmnt.ui.theme.HomeScreen
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class PoseDetectionActivity : ComponentActivity() {
    private lateinit var previewView: PreviewView
    private lateinit var overlay: PoseOverlayView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var countText: TextView
    private lateinit var sessionChart: SessionChartView

    private var isRecording = false
    private val currentSession = mutableListOf<MovementReading>()

    private val poseDetector by lazy {
        val options = AccuratePoseDetectorOptions.Builder()
            .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE)
            .build()
        PoseDetection.getClient(options)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        previewView = findViewById(R.id.previewView)
        overlay = findViewById(R.id.overlay)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)
        countText = findViewById(R.id.countText)
        sessionChart = findViewById(R.id.sessionChart) as SessionChartView


        startButton.setOnClickListener {
            currentSession.clear()
            isRecording = true
            updateCount()
        }

        stopButton.setOnClickListener {
            isRecording = false
            saveSessionToDatabase()
            showSessionChart()
        }

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA), 1001
            )
        }
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001 && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            finish()
            exitProcess(0)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()
                .also { it.setSurfaceProvider(previewView.surfaceProvider) }

            val analysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(480, 640))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(ContextCompat.getMainExecutor(this)) { imageProxy ->
                        processImageProxy(imageProxy)
                    }
                }

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this, CameraSelector.DEFAULT_FRONT_CAMERA, preview, analysis
            )
        }, ContextCompat.getMainExecutor(this))
    }

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: run {
            imageProxy.close()
            return
        }
        val rotation = imageProxy.imageInfo.rotationDegrees
        val inputImage = InputImage.fromMediaImage(mediaImage, rotation)

        poseDetector.process(inputImage)
            .addOnSuccessListener { pose ->
                overlay.setPose(pose)

                val rShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
                val rElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)
                val rWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)

                if (rShoulder != null && rElbow != null && rWrist != null) {
                    val angle = PoseUtils.calculateAngle(
                        PointF(rShoulder.position.x, rShoulder.position.y),
                        PointF(rElbow.position.x, rElbow.position.y),
                        PointF(rWrist.position.x, rWrist.position.y)
                    )
                    overlay.setAngle(angle)
                    recordAngle(angle)
                }
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

    private fun recordAngle(angle: Float) {
        if (!isRecording) return
        val reading = MovementReading(
            timestamp = System.currentTimeMillis(),
            angle = angle
        )
        currentSession += reading
        updateCount()
    }

    private fun updateCount() {
        countText.text = "Frames: ${currentSession.size}"
    }

    private fun saveSessionToDatabase() {
        val db = (application as PrjctMovmntApp).database
        val dao = db.readingDao()
        CoroutineScope(Dispatchers.IO).launch {
            currentSession.forEach { dao.insert(it) }
        }
    }

    private fun showSessionChart() {
        previewView.visibility = View.GONE
        overlay.visibility = View.GONE
        sessionChart.visibility = View.VISIBLE

        if (::sessionChart.isInitialized) {
            sessionChart.setReadings(currentSession)
        }
    }
}