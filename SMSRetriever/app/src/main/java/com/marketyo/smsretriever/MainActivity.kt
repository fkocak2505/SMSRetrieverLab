package com.marketyo.smsretriever

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.marketyo.smsretriever.ui.theme.SMSRetrieverTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SMSRetrieverTheme {
                FeatureThatRequiresCameraPermission()
                SmsRetrieverUserConsentBroadcast { _, code ->
                    print(code)
                }
            }
        }
    }
}