package com.demo.appframe.widget.camera

import androidx.annotation.IntDef
import com.demo.appframe.widget.camera.CameraRatioType.Companion.RATIO_1_1
import com.demo.appframe.widget.camera.CameraRatioType.Companion.RATIO_3_4
import com.demo.appframe.widget.camera.CameraRatioType.Companion.RATIO_9_16
import com.demo.appframe.widget.camera.CameraRatioType.Companion.RATIO_FULL

@IntDef(flag = true, value = [RATIO_3_4, RATIO_9_16, RATIO_1_1, RATIO_FULL])
annotation class CameraRatioType {
    companion object {
        const val RATIO_3_4: Int = 0
        const val RATIO_9_16: Int = 1
        const val RATIO_1_1: Int = 2
        const val RATIO_FULL: Int = 3
    }
}