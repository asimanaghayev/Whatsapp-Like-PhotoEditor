package com.droidninja.photoeditengine

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.droidninja.imageeditengine.ImageEditor
import com.tbruyelle.rxpermissions2.RxPermissions
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import kotlinx.android.synthetic.main.activity_main.edited_image
import kotlinx.android.synthetic.main.activity_main.select_image_btn


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rxPermissions = RxPermissions(this)
        select_image_btn.setOnClickListener {

            rxPermissions
                .request(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .subscribe { granted ->
                    if (granted) { // Always true pre-M
                        // I can control the camera now
                        FilePickerBuilder.getInstance().setMaxCount(1)
                            .setActivityTheme(droidninja.filepicker.R.style.LibAppTheme)
                            .pickPhoto(this)
                    } else {
                        // Oups permission denied
                        Toast.makeText(this, "Not given permission", Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            FilePickerConst.REQUEST_CODE_PHOTO ->
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val photoPaths = ArrayList<String>()
                    data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA)
                        ?.let { photoPaths.addAll(it) }

                    if (photoPaths.isNotEmpty()) {
                        ImageEditor.Builder(this, photoPaths[0])
                            .setStickerAssets("stickers")
                            .open()
                    } else {
                        Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
                    }
                }

            ImageEditor.RC_IMAGE_EDITOR ->
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.getStringExtra(ImageEditor.EXTRA_EDITED_PATH)?.let {
                        edited_image.setImageBitmap(BitmapFactory.decodeFile(it))
                    }
                }
        }
    }
}
