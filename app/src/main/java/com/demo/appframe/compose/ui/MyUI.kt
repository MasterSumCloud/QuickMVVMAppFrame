package com.demo.appframe.compose.ui

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.demo.appframe.R
import com.demo.appframe.act.ContactServiceAct
import com.demo.appframe.act.SettingAct
import com.demo.appframe.act.TestAct
import com.demo.appframe.act.WebUrlAct
import com.demo.appframe.core.Constent
import com.demo.appframe.core.UCS
import com.demo.appframe.ext.startActivity
import com.demo.appframe.ext.startActivityJuageLogin
import com.demo.appframe.ext.startActivityWithIntent

@Composable
fun MyUI(modifierUI: Modifier) {
    val ctx = LocalContext.current
    ConstraintLayout(
        modifier = modifierUI
    ) {
        val (headerImg, userName, userId, vipbg, huangguan, viptx1, viptx2, openVip, mylist) = createRefs()
        Image(painter = painterResource(id = R.mipmap.header_ofmy_default),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(66.dp)
                .clip(
                    CircleShape
                )
                .constrainAs(headerImg) {
                    top.linkTo(parent.top, margin = 23.dp)
                    start.linkTo(parent.start, margin = 18.dp)
                })

        Text(text = "登录/注册", fontSize = 18.sp, color = colorResource(id = R.color.black_2), modifier = Modifier.constrainAs(userName) {
            top.linkTo(headerImg.top)
            bottom.linkTo(userId.top)
            start.linkTo(headerImg.end, 9.dp)
        })

        Text(text = "欢迎使用", fontSize = 13.sp, color = colorResource(id = R.color.gray_a1), modifier = Modifier.constrainAs(userId) {
            start.linkTo(userName.start)
            top.linkTo(userName.bottom)
            bottom.linkTo(headerImg.bottom)
        })

        Image(painter = painterResource(id = R.mipmap.food4),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp)
                .fillMaxWidth()
                .height(61.dp)
                .clip(RoundedCornerShape(10.dp))
                .border(BorderStroke(1.dp, colorResource(id = R.color.purple_200)))
                .constrainAs(vipbg) {
                    top.linkTo(headerImg.bottom, 27.dp)
                })

        Image(painter = painterResource(id = R.mipmap.vip_right_gou),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .constrainAs(huangguan) {
                    start.linkTo(vipbg.start, 24.dp)
                    top.linkTo(vipbg.top)
                    bottom.linkTo(vipbg.bottom)
                })

        Text(text = "开通会员  畅享全部特权",
            fontSize = 14.sp,
            color = colorResource(id = R.color.brown_7B5014),
            modifier = Modifier.constrainAs(viptx1) {
                top.linkTo(vipbg.top)
                start.linkTo(huangguan.end, 14.dp)
                bottom.linkTo(viptx2.top)
            })

        Text(text = "开通会员查看联系方式",
            fontSize = 12.sp,
            color = colorResource(id = R.color.gold_D09720),
            modifier = Modifier.constrainAs(viptx2) {
                start.linkTo(viptx1.start)
                top.linkTo(viptx1.bottom)
                bottom.linkTo(vipbg.bottom)
            })


        Text(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color(0xFFF9BD37), Color(0xFFE89530)
                        )
                    ), shape = RoundedCornerShape(12.dp)
                )
                .size(70.dp, 24.dp)
                .clickable {
                    Toast.makeText(ctx, "开通vip", Toast.LENGTH_SHORT)
                }
//                .padding(start = 11.dp, end = 11.dp, top = 3.dp, bottom = 3.dp)
                .constrainAs(openVip) {
                    end.linkTo(parent.end, 25.dp)
                    top.linkTo(vipbg.top)
                    bottom.linkTo(vipbg.bottom)
                },
            text = "立即开通", color = Color.White, fontSize = 12.sp,
            textAlign = TextAlign.Center, lineHeight = 2.em
        )

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 17.dp)
            .constrainAs(mylist) {
                top.linkTo(vipbg.bottom)
            }) {
            myfmlist.forEach {
                item { singleItem(ctx, item = it) }
            }
        }

    }
}

@Composable
fun singleItem(ctx: Context, item: MyItem) {
    val checkedState = remember {
        mutableStateOf(item.checked)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .clickable {
                when (item.funName) {
                    "联系客服" -> {
                        ctx.startActivityJuageLogin(ContactServiceAct::class.java)
                    }

                    "用户协议" -> {
                        ctx.startActivityWithIntent(Intent(ctx, WebUrlAct::class.java).apply {
                            putExtra(Constent.WEB_URL, UCS.YHXY_URL)
                            putExtra(Constent.TITLE_TEXT, "用户协议")
                        })
                    }

                    "隐私协议" -> {
                        ctx.startActivityWithIntent(Intent(ctx, WebUrlAct::class.java).apply {
                            putExtra(Constent.WEB_URL, UCS.YSZC_URL)
                            putExtra(Constent.TITLE_TEXT, "隐私协议")
                        })
                    }

                    "设置" -> {
                        ctx.startActivity(SettingAct::class.java)
                    }

                    "测试" -> {
                        ctx.startActivity(TestAct::class.java)
                    }
                }
            }) {
            Image(painter = painterResource(id = item.icon), contentDescription = null, modifier = Modifier.padding(start = 20.dp))
            Text(text = item.funName, fontSize = 15.sp, modifier = Modifier.padding(start = 14.dp))
        }

        if (item.showArr) {
            Image(
                painter = painterResource(id = R.mipmap.arr_right_gray),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 18.dp)
                    .size(20.dp)
                    .align(Alignment.CenterEnd),
            )
        } else if (item.showCheckBox) {
            Switch(
                checked = checkedState.value,
                modifier = Modifier
                    .padding(end = 18.dp)
                    .align(Alignment.CenterEnd),
                onCheckedChange = {
                    item.checked = it
                    checkedState.value = it
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFFF9BD37),
                    uncheckedThumbColor = Color(0xFFF2F2F2),
                    checkedTrackColor = Color(0xFFF0000),
                    uncheckedTrackColor = Color.White
                )
            )
        }
    }
}

@Preview
@Composable
fun itemShow() {
    val ctx = LocalContext.current
    singleItem(ctx, MyItem(R.mipmap.myicon_notify, "方法名称", false, true))
}

private val myfmlist = mutableListOf<MyItem>().apply {
    add(MyItem(R.mipmap.myicon_service, "联系客服", true))
    add(MyItem(R.mipmap.myicon_yhxy, "用户协议", true))
    add(MyItem(R.mipmap.myicon_yszc, "隐私协议", true))
    add(MyItem(R.mipmap.myicon_notify, "个性化推荐", false, true))
    add(MyItem(R.mipmap.myicon_setting, "设置", false))
    add(MyItem(R.mipmap.myicon_setting, "测试", false))
}

data class MyItem(
    val icon: Int,
    val funName: String,
    val showArr: Boolean = false,
    val showCheckBox: Boolean = false,
    var checked: Boolean = false
)