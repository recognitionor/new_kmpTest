package com.kmp.newtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.engine.okhttp.OkHttp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Napier.base(DebugAntilog()) // 로그 설정

        Napier.d(tag = "jhlee", message = "~~~~~~~~~")
        setContent {
            App(remember {
                OkHttp.create()
            })
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App(remember {
        OkHttp.create()
    })
}