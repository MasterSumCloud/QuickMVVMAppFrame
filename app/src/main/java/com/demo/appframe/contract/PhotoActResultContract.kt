package com.demo.appframe.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.demo.appframe.core.Constent

class PhotoActResultContract() : ActivityResultContract<Int, Intent>() {

    override fun parseResult(resultCode: Int, intent: Intent?): Intent {
        if (intent != null && resultCode == Activity.RESULT_OK) {
            return intent
        } else {
            return Intent()
        }
    }

    override fun createIntent(context: Context, requestCode: Int): Intent {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Constent.START_CANERA2_REQUEST_CODE, requestCode)
        return intent
    }
}