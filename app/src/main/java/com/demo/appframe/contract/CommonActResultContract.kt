package com.demo.appframe.contract

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.demo.appframe.core.Constent

class CommonActResultContract() : ActivityResultContract<Intent, Intent?>() {

    private var requestCode: Int = -1;
    override fun createIntent(context: Context, input: Intent): Intent {
        val resQ = input.getIntExtra(Constent.REQUEST_CODE, -1)
        requestCode = resQ
        return input
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Intent {
        val intentResult: Intent = intent ?: Intent()
        intentResult.putExtra(Constent.RESULT_CODE, resultCode)
        intentResult.putExtra(Constent.REQUEST_CODE, requestCode)
        return intentResult
    }
}