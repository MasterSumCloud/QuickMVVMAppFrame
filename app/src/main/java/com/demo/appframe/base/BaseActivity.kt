package com.demo.appframe.base

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.PermissionUtils
import com.demo.appframe.App
import com.demo.appframe.R
import com.demo.appframe.contract.CommonActResultContract
import com.demo.appframe.core.AppManager
import com.demo.appframe.core.Constent
import com.demo.appframe.dialog.LoadingDialog
import com.demo.appframe.dialog.TextDialog
import com.demo.appframe.ext.logd
import com.demo.appframe.ext.toastShort
import com.lky.toucheffectsmodule.factory.TouchEffectsFactory
import com.umeng.analytics.MobclickAgent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


abstract class BaseActivity : AppCompatActivity() {

    private val loginToken: String? = null

    open lateinit var handlerBase: Handler
    private lateinit var mLoadingDialog: LoadingDialog
    var mIsDestoried = false
    private lateinit var mIntentActivityResultLauncher: ActivityResultLauncher<Intent>
    open var laterUnRegisetEventBus = false
    private lateinit var cameraDialog: TextDialog
    private lateinit var externalDialog: TextDialog


    private fun initActLauncher() {
        mIntentActivityResultLauncher = registerForActivityResult<Intent, Intent>(
            CommonActResultContract()
        ) { result ->
            var resultCode = -1
            var requestCode = -1
            if (result != null) {
                requestCode = result.getIntExtra(Constent.REQUEST_CODE, Activity.RESULT_OK)
                resultCode = result.getIntExtra(Constent.RESULT_CODE, Activity.RESULT_OK)
            }
            onNewActResult(requestCode, resultCode, result)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        TouchEffectsFactory.initTouchEffects(this)
        super.onCreate(savedInstanceState)
        BarUtils.setNavBarColor(this, getColor(R.color.black_17171f))
        initBaseConfig()
        initActLauncher()
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    private fun initBaseConfig() {
        handlerBase = Handler(Looper.getMainLooper()) { msg ->
            handleMessageFormBase(msg)
            false
        }
        AppManager.Companion.appManager?.addActivity(this)
        mLoadingDialog = LoadingDialog(this)
        cameraDialog = TextDialog(this)
        externalDialog = TextDialog(this)
    }

    open fun handleMessageFormBase(msg: Message?) {}


    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(messageEvent: BaseMessageEvent) {
        GsonUtils.toJson(messageEvent).logd("messageEvent")
        if (TextUtils.equals(Constent.LOGIN_EVENT, messageEvent.tag)) {
            if (this.localClassName == "MainActivity") {
                onLogin(messageEvent)
            }
        } else if (TextUtils.equals(Constent.LOGIN_SUCCESS, messageEvent.tag)) {
            App.app.mPhoneNumberAuthHelper?.quitLoginPage()
            App.app.mPhoneNumberAuthHelper?.setAuthListener(null)
            onLoginSuccess()
        } else if (TextUtils.equals(messageEvent.tag, Constent.LOGIN_OUT)) {
            onLoginOut()
        } else {
            onSelfMessageEvent(messageEvent)
        }
    }

    open fun onNewActResult(requestCode: Int, resultCode: Int, result: Intent?) {}

    override fun onResume() {
        super.onResume()
        MobclickAgent.onPageStart(javaClass.simpleName)
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        KeyboardUtils.hideSoftInput(this)
        MobclickAgent.onPageEnd(javaClass.simpleName)
        MobclickAgent.onPause(this)
    }

    override fun onStop() {
        super.onStop()
        if (!laterUnRegisetEventBus) {
            EventBus.getDefault().unregister(this)
        }
    }


    fun postMessageEvent(messageEvent: BaseMessageEvent?) {
        EventBus.getDefault().post(messageEvent)
    }


    override fun onDestroy() {
        super.onDestroy()
        if (laterUnRegisetEventBus) {
            EventBus.getDefault().unregister(this)
        }
        AppManager.Companion.appManager?.finishActivity(this)
        handlerBase.removeCallbacksAndMessages(null)
        mLoadingDialog.dismiss()
        mIsDestoried = true
        mIntentActivityResultLauncher.unregister()
    }


    fun startActivity(cls: Class<*>?) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }

    fun startActivityJuageLogin(cls: Class<*>?) {
        if (App.isLogin) {
            val intent = Intent(this, cls)
            startActivity(intent)
        } else {
            App.app.goLogin()
        }
    }

    fun startActForResult(intent: Intent?) {
        mIntentActivityResultLauncher.launch(intent)
    }

    fun startActForResult(cls: Class<*>?) {
        val intent = Intent(this, cls)
        mIntentActivityResultLauncher.launch(intent)
    }

    fun startActForResult(requestCode: Int, intent: Intent) {
        intent.putExtra(Constent.REQUEST_CODE, requestCode)
        mIntentActivityResultLauncher.launch(intent)
    }

    fun showLoading() {
        mLoadingDialog.show()
    }

    fun disLoading() {
        mLoadingDialog.dismiss()
    }

    open fun reqCameraPermission() {
        val granted = PermissionUtils.isGranted(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        if (!granted) {
            cameraDialog.showTextDialog("您当前使用的功能需要相机和存储权限，目的是方便您拍摄照片后的存储和编辑，若拒绝该权限将无法正常使用哦。",
                object : TextDialog.onTextDialogBtnListener {
                    override fun onClickOk() {
                        PermissionUtils.permission(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                        ).callback(object : PermissionUtils.SimpleCallback {
                            override fun onGranted() {
                                onCameraGranted()
                            }

                            override fun onDenied() {
                                "您拒绝了相机权限，无法打开相机".toastShort()
                            }
                        }).request()
                    }

                    override fun onClickCancel() {

                    }
                })
        } else {
            onCameraGranted()
        }
    }

    open fun reqRWPermission() {
        val granted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionUtils.isGranted(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        } else {
            PermissionUtils.isGranted(
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
        if (!granted) {
            externalDialog.showTextDialog("您当前使用的功能需要存储权限，目的是方便您选择文件后能正常的进行编辑和处理，若拒绝该权限将无法正常使用哦。",
                object : TextDialog.onTextDialogBtnListener {
                    override fun onClickOk() {
                        val callback = object : PermissionUtils.SimpleCallback {
                            override fun onGranted() {
                                onRWGranted()
                            }

                            override fun onDenied() {
                                "您拒绝了存储权限，无法为您提供识别服务".toastShort()
                            }
                        }
                        /*val callback = object :PermissionUtils.FullCallback{
                            override fun onGranted(granted: MutableList<String>) {
                                onRWGranted()
                            }

                            override fun onDenied(deniedForever: MutableList<String>, denied: MutableList<String>) {
                                "您拒绝了存储权限，无法为您提供识别服务".toastShort()
                            }
                        }*/
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            PermissionUtils.permission(
                                Manifest.permission.READ_MEDIA_IMAGES,
                                Manifest.permission.READ_MEDIA_VIDEO,
                                Manifest.permission.READ_MEDIA_AUDIO,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ).callback(callback).request()
                        } else {
                            PermissionUtils.permission(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ).callback(callback).request()
                        }
                    }

                    override fun onClickCancel() {

                    }

                })
        } else {
            onRWGranted()
        }
    }

    open fun onRWGranted() {}
    open fun onCameraGranted() {}

    open fun onLoginSuccess() {}
    open fun onLoginOut() {}
    open fun onLogin(messageEvent: BaseMessageEvent) {}

    protected abstract fun onSelfMessageEvent(messageEvent: BaseMessageEvent?)
}