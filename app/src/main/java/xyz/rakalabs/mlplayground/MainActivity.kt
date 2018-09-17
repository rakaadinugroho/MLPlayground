package xyz.rakalabs.mlplayground

import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.wonderkiln.camerakit.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), CameraKitEventListener {
    override fun onVideo(p0: CameraKitVideo?) {

    }

    override fun onEvent(p0: CameraKitEvent?) {

    }

    override fun onImage(cameraKit: CameraKitImage) {
        var bitmap = cameraKit.bitmap
        bitmap = Bitmap.createScaledBitmap(bitmap, camera.width, camera.height, false)
        camera.stop()
        recognizeID(bitmap)
    }

    private fun recognizeID(bitmap: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val detect = FirebaseVision.getInstance()
                .onDeviceTextRecognizer
                .processImage(image)
                .addOnSuccessListener {
                    texts -> processSuccess(texts as FirebaseVisionText)
                }
                .addOnFailureListener {
                    _ -> Toast.makeText(this, "gagal", Toast.LENGTH_LONG).show()
                }
    }

    private fun processSuccess(texts: FirebaseVisionText) {
        val blocks = texts.textBlocks
        Toast.makeText(this, "total block ${blocks.size}", Toast.LENGTH_LONG).show()
        blocks.forEach { t ->
            t.lines.forEach {
                w ->
                w.elements.forEach {
                    z ->
                    Log.e("recognize", z.text )
                }
            }
        }
    }

    override fun onError(p0: CameraKitError?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recognize.setOnClickListener {
            camera.start()
            camera.captureImage()
        }

        camera.addCameraKitListener(this)
    }

    override fun onResume() {
        super.onResume()
        camera.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        camera.stop()
    }

    override fun onPause() {
        super.onPause()
        camera.stop()
    }
}
