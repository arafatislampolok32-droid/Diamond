package com.example

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF080A0F))
            .statusBarsPadding()
            .navigationBarsPadding()
        ) {
          var isLoading by remember { mutableStateOf(true) }
          
          AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
              WebView(context).apply {
                configureWebViewSettings(settings)
                
                webViewClient = object : WebViewClient() {
                  override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    isLoading = true
                  }

                  override fun onPageFinished(view: WebView?, url: String?) {
                    isLoading = false
                  }
                }
                
                webChromeClient = object : WebChromeClient() {
                  override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                    android.util.Log.d("WebViewConsole", consoleMessage?.message() ?: "")
                    return true
                  }
                }
                
                loadUrl("file:///android_asset/index.html")
              }
            }
          )
          
          if (isLoading) {
            CircularProgressIndicator(
              modifier = Modifier.align(Alignment.Center),
              color = Color(0xFF3B82F6)
            )
          }
        }
      }
    }
  }

  @SuppressLint("SetJavaScriptEnabled")
  private fun configureWebViewSettings(settings: WebSettings) {
    settings.javaScriptEnabled = true
    settings.domStorageEnabled = true
    settings.allowFileAccess = true
    settings.allowContentAccess = true
    settings.databaseEnabled = true
    settings.useWideViewPort = true
    settings.loadWithOverviewMode = true
    settings.setSupportZoom(false)
    settings.builtInZoomControls = false
    settings.displayZoomControls = false
    settings.cacheMode = WebSettings.LOAD_DEFAULT
  }
}

