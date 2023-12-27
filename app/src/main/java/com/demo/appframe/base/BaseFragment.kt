package com.demo.appframe.base

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.TimeUtils
import com.demo.appframe.ext.toastShort
import com.demo.appframe.App
import com.demo.appframe.contract.CommonActResultContract
import com.demo.appframe.core.Constent
import com.demo.appframe.core.UCS
import com.demo.appframe.dialog.LoadingDialog
import com.demo.appframe.dialog.TextDialog
import com.umeng.analytics.MobclickAgent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseFragment : Fragment(), View.OnClickListener {
    var baseActivity: BaseActivity? = null

    private var mLastClickView: View? = null
    private var mLastClickTime: Long = 0
    private var handlerBase: Handler? = null
    private var mLoadingDialog: LoadingDialog? = null
    private lateinit var mIntentActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var cameraDialog: TextDialog
    private lateinit var externalDialog: TextDialog

    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseActivity = context as BaseActivity
        EventBus.getDefault().register(this)
        cameraDialog = TextDialog(context)
        externalDialog = TextDialog(context)
        initActLauncher()
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLoadingDialog = baseActivity?.let { LoadingDialog(it) }
        initBaseConfig()
        initSelfConfig()
        initSelfViews()
        initSelfListener()
    }

    private fun initBaseConfig() {
        handlerBase = Handler(Looper.getMainLooper()) { msg ->
            handleMessageFormBase(msg)
            false
        }
    }

    open fun handleMessageFormBase(msg: Message?) {}

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(messageEvent: BaseMessageEvent) {
        if (TextUtils.equals(Constent.LOGIN_SUCCESS, messageEvent.tag)) {
            App.app.mPhoneNumberAuthHelper?.quitLoginPage()
            App.app.mPhoneNumberAuthHelper?.setAuthListener(null)
            onLoginSuccess()
        } else if (TextUtils.equals(messageEvent.tag, Constent.LOGIN_OUT)) {
            onLoginOut()
        } else {
            onSelfMessageEvent(messageEvent)
        }
    }

    open fun onLoginOut() {}
    open fun onLoginSuccess() {}
    open fun startActivity(cls: Class<*>?) {
        val intent = Intent(baseActivity, cls)
        startActivity(intent)
    }

    open fun startActivityJuageLogin(cls: Class<*>?) {
        if (App.isLogin) {
            startActivity(cls)
        } else {
            App.app.goLogin()
        }
    }

    open fun postMessageEvent(messageEvent: BaseMessageEvent?) {
        EventBus.getDefault().post(messageEvent)
    }

    //统一处理点击事件
    override fun onClick(v: View) {
        val nowMills = TimeUtils.getNowMills()
        if (mLastClickView === v) {
            if (nowMills - mLastClickTime > UCS.TIME_CLICK_INTERVAL) {
                singeClick(v)
            } else {
                normalClick(v)
            }
        } else {
            singeClick(v)
            normalClick(v)
        }
        mLastClickView = v
        mLastClickTime = nowMills
    }

    fun showLoading() {
        mLoadingDialog?.show()
    }

    fun disLoading() {
        mLoadingDialog?.dismiss()
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onPageStart(javaClass.simpleName)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPageEnd(javaClass.simpleName)
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
        val granted = PermissionUtils.isGranted(
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (!granted) {
            externalDialog.showTextDialog("您当前使用的功能需要存储权限，目的是方便您选择文件后能正常的进行编辑和处理，若拒绝该权限将无法正常使用哦。",
                object : TextDialog.onTextDialogBtnListener {
                    override fun onClickOk() {
                        PermissionUtils.permission(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ).callback(object : PermissionUtils.SimpleCallback {
                            override fun onGranted() {
                                onRWGranted()
                            }

                            override fun onDenied() {
                                "您拒绝了存储权限，无法为您提供识别服务".toastShort()
                            }
                        }).request()
                    }

                    override fun onClickCancel() {

                    }

                })
        } else {
            onRWGranted()
        }
    }

    open fun onCameraGranted() {}
    open fun onRWGranted() {}
    open fun onNewActResult(requestCode: Int, resultCode: Int, result: Intent?) {}

    //设置必要的配置
    protected abstract fun initSelfConfig()

    //返回Act的布局
    protected abstract fun initLayoutResId(): Int

    //初始化View和Listener
    protected abstract fun initSelfViews()
    protected abstract fun initSelfListener()

    abstract fun singeClick(v: View?)
    abstract fun normalClick(v: View?)
    protected abstract fun onSelfMessageEvent(messageEvent: BaseMessageEvent?)


    companion object {
        private const val STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN"
    }

    fun startActForResult(intent: Intent?) {
        mIntentActivityResultLauncher.launch(intent)
    }

    fun startActForResult(cls: Class<*>?) {
        val intent = Intent(baseActivity, cls)
        mIntentActivityResultLauncher.launch(intent)
    }

    fun startActForResult(requestCode: Int, intent: Intent) {
        intent.putExtra(Constent.REQUEST_CODE, requestCode)
        mIntentActivityResultLauncher.launch(intent)
    }
}