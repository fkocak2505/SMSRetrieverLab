package com.marketyo.smsretriever

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import com.marketyo.smsretriever.ui.theme.SMSRetrieverTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SMSRetrieverTheme {
                Column {
//                    FeatureThatRequiresCameraPermission()
                    SmsRetrieverUserConsentBroadcast { _, code ->
                        print(code)
                        sCode.value = code
                        isRestartReciever.value = !isRestartReciever.value
                    }

                    Text(
                        text = sCode.value
                    )
                }
            }
        }
    }
}