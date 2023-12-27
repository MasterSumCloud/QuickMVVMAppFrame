package com.demo.appframe.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import androidx.core.content.FileProvider
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ScreenUtils
import com.demo.appframe.App
import com.demo.appframe.act.AllVIFileAct
import com.demo.appframe.base.BaseActivity
import com.demo.appframe.base.BaseFragment
import com.demo.appframe.base.BaseMessageEvent
import com.demo.appframe.beans.LoginResponBean
import com.demo.appframe.beans.MediaFile
import com.demo.appframe.core.Constent
import com.demo.appframe.ext.isNotEmptyOrNull
import com.demo.appframe.ext.toastShort
import com.demo.appframe.wxapi.Wx
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXFileObject
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.umeng.analytics.MobclickAgent
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.UUID


object GeneralUtil {

    fun getScreentPointOfApp(): Float {
        return ScreenUtils.getScreenWidth() / 360f
    }

    fun dpTPointOfApp(dp: Float): Float {
        return ScreenUtils.getScreenWidth() / 360f * dp
    }


    fun isActExist(act: Activity?): Boolean {
        if (act == null) {
            return false
        } else {
            return !(act.isFinishing || act.isDestroyed)
        }
    }

    fun loginSuccess(it: LoginResponBean?, LoginType: String) {
        App.token = it?.token
        App.isLogin = it?.token.isNotEmptyOrNull()
        App.myActInfo = it
        if (App.isLogin) {
            SPUtil.putLoginInfoBean(GsonUtils.toJson(it))
            SPUtil.putToken(it?.token)
        }
        App.app.mPhoneNumberAuthHelper?.quitLoginPage()
        App.app.mPhoneNumberAuthHelper?.setAuthListener(null)
        App.isVip = it?.vip == true
        SPUtil.putVip(it?.vip ?: false)
        EventBus.getDefault().post(BaseMessageEvent(Constent.LOGIN_SUCCESS, null))
        Tos.showToastShort("登录成功")
        onUMengAccountSignIn(LoginType, it?.user_id)
    }

    fun getFileSize(filsSize: Long): String {
        //最低1M
        val standSize = 1024 * 1024
        val format = DecimalFormat("0.##")
        //未保留小数的舍弃规则，RoundingMode.FLOOR表示直接舍弃。
        format.roundingMode = RoundingMode.FLOOR

        if (filsSize > standSize) {//1M
            val mbs = filsSize / standSize
            return format.format(mbs) + "M"
        } else if (filsSize > standSize * 1024) {
            val gbs = filsSize / (standSize * 1024)
            return format.format(gbs) + "GB"
        } else {
            val kbs = filsSize / 1024
            return format.format(kbs) + "K"
        }
    }

    /**
     * 友盟点击统计
     */
    fun onUMengClickEvent(context: Context, event: String) {
        MobclickAgent.onEvent(context.applicationContext, event)
    }

    /**
     * 友盟用户统计登入
     */
    fun onUMengAccountSignIn(provider: String, accountId: String?) {
        MobclickAgent.onProfileSignIn(provider, accountId)
    }

    fun str2Int(numberStr: String?): Int {
        if (numberStr == null) {
            return -1
        }
        try {
            return numberStr.toInt()
        } catch (e: java.lang.Exception) {
            return -1
        }
    }

    /**
     * 获取文件名字
     */
    fun getFileName(path: String?): String {
        if (path.isNotEmptyOrNull()) {
            val lastpoint = path!!.lastIndexOf("/")
            return path.substring(lastpoint + 1, path.length)
        } else {
            return ""
        }
    }

    /**
     * 获取文件类型
     */
    fun getFileType(path: String?): String {
        if (path == null) {
            return ""
        }
        val lastpoint = path.lastIndexOf(".")
        return path.substring(lastpoint + 1, path.length).lowercase()
    }


    fun updateAblumRefresh(context: Context, path: String) {
        MediaScannerConnection.scanFile(
            context, arrayOf(path), null, null
        )
    }


    fun shareFileToWx(filePath: String, context: Context) {
        try {
            val wxFileObj = WXFileObject()
            wxFileObj.filePath = getFileUri(context, File(copyFile2ShareDir(filePath, context)))
            val msg = WXMediaMessage(wxFileObj)
            msg.title = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length)
            //发送请求
            val req = SendMessageToWX.Req()
            //创建唯一标识
            req.transaction = System.currentTimeMillis().toString()
            req.message = msg
            req.scene = SendMessageToWX.Req.WXSceneSession
            val wxapi = WXAPIFactory.createWXAPI(context, Wx.APP_ID)
            wxapi.sendReq(req)
        } catch (e: Exception) {
            Tos.showToastShort("分享出错")
        }
    }

    private fun copyFile2ShareDir(srcPath: String, context: Context): String {
        val shareDir = getSharePath(context)
        File(shareDir).mkdirs()
        val shareFilePath = "$shareDir/${getFileName(srcPath)}"
        com.blankj.utilcode.util.FileUtils.copy(srcPath, shareFilePath)
        return shareFilePath
    }

    fun getSharePath(context: Context): String {
        return "${context.getExternalFilesDir(null)}/shareData/"
    }

    fun getFileUri(context: Context, file: File?): String? {
        if (file == null || !file.exists()) {
            return null
        }
        val contentUri: Uri = FileProvider.getUriForFile(
            context, "包名",  //TODO 要与`AndroidManifest.xml`里配置的`authorities`一致，假设你的应用包名为com.example.app
            file
        )

        // 授权给微信访问路径
        context.grantUriPermission(
            "com.tencent.mm",  // 这里填微信包名
            contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
        return contentUri.toString() // contentUri.toString() 即是以"content://"开头的用于共享的路径
    }



    fun getUUID(): String {
        var uid = SPUtils.getInstance().getString(Constent.UUID)
        if (TextUtils.isEmpty(uid)) {
            val aid = ""
            if (TextUtils.isEmpty(aid)) {
                val randomUUID = UUID.randomUUID().toString()
                SPUtil.putUUID(randomUUID)
                uid = randomUUID
            } else {
                val randomUUID = UUID.nameUUIDFromBytes(aid.toByteArray()).toString()
                SPUtil.putUUID(randomUUID)
                uid = randomUUID
            }
//            LogU.d("uid===$uid")
        }
        return uid
    }

    /**
     * 去除文件名的类型
     */
    fun removeFileType(name: String?): String {
        if (name == null) {
            return ""
        }
        val lastIndexOf = name.lastIndexOf(".")
        if (lastIndexOf == -1) {
            return name
        } else {
            return name.substring(0, lastIndexOf)
        }
    }


    @SuppressLint("IntentReset")
    fun openFileManager(baseAct: BaseActivity?, reqCode: Int, type: Int = 1) {
        baseAct?.startActForResult(
            reqCode, Intent(baseAct, AllVIFileAct::class.java).putExtra(
                Constent.TITLE_TYPE, type
            )
        )
    }

    @SuppressLint("IntentReset")
    fun openFileManager(baseFm: BaseFragment?, reqCode: Int = 2011, type: Int = 1) {
        baseFm?.startActForResult(
            reqCode, Intent(baseFm.baseActivity, AllVIFileAct::class.java).putExtra(
                Constent.TITLE_TYPE, type
            )
        )
    }

    fun deleteWhenExist(path: String?) {
        if (path.isNotEmptyOrNull()) {
            File(path).run {
                if (isFile) {
                    if (exists()) {
                        delete()
                    }
                } else if (isDirectory) {
                    deleteRecursively()
                }
            }
        }
    }

    fun saveViewToImage(view: View?, filePath: String, scale: Float) {
        if (view == null) {
            return
        }
        try {
            deleteWhenExist(filePath)
            val iwidth = view.width
            val iheight = view.height
            val bitmap = Bitmap.createBitmap(iwidth, iheight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            val save = ImageUtils.save(bitmap, filePath, Bitmap.CompressFormat.PNG)
            if (!save) {
                "V2I失败".toastShort()
            } else {
                val scaleV = ImageUtils.scale(BitmapFactory.decodeFile(filePath), iwidth.times(scale).toInt(), iheight.times(scale).toInt())
                val save2 = ImageUtils.save(scaleV, filePath, Bitmap.CompressFormat.PNG)
                if (!save2) {
                    "V2I失败".toastShort()
                }
            }
        } catch (_: Exception) {
            "V2I失败".toastShort()
        }
    }

    fun randomrandomAlphanumeric(size: Int): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..size).map { kotlin.random.Random.nextInt(0, charPool.size) }.map(charPool::get).joinToString("")

    }

    @SuppressLint("Range")
    fun getAllVIFiles(context: Context, type: Int): MutableList<MediaFile> {
        try {
            val mediaFiles = mutableListOf<MediaFile>()
            val contentResolver = context.contentResolver
            var uri: Uri
            if (type == 0) {
                uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else {
                uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            }
            val projection = arrayOf(
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DATE_ADDED,
                MediaStore.Audio.Media.DURATION
            )
//        val selection = MediaStore.Video.Medi
            val cursor = contentResolver.query(uri, projection, null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        val name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                        val path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                        val size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE))
                        val timeModify = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED))
                        val duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                        if (!TextUtils.isEmpty(name)) {
                            mediaFiles.add(
                                MediaFile(
                                    name, path, size, timeModify, duration, "", type, false
                                )
                            )
                        }
                    } while (cursor.moveToNext())
                }
                cursor.close()
            }
            mediaFiles.sortBy { -it.addTime }
            return mediaFiles
        } catch (e: Exception) {
            if (type == 0) {
                "获取图片文件列表错误".toastShort()
            } else {
                "获取视频文件列表错误".toastShort()
            }
            return mutableListOf()
        }
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}