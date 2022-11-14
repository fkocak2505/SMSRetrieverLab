package com.marketyo.smsretriever

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.marketyo.smsretriever.Constants.getVerificationCodeFromSms
import com.orhanobut.logger.Logger

@Composable
fun SmsRetrieverUserConsentBroadcast(
    smsCodeLength: Int = 6,
    onSmsReceived: (message: String, code: String) -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(isRestartReciever.value) {
        Logger.d("Initializing Sms Retriever client")
        SmsRetriever.getClient(context)
            .startSmsUserConsent(null)
            .addOnSuccessListener {
                Logger.d("SmsRetriever started successfully")
                shouldRegisterReceiver.value = true
            }
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                val message: String? = it.data!!.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                message?.let {
                    Logger.d("Sms received: $message")
                    val verificationCode = getVerificationCodeFromSms(message, smsCodeLength)
                    Logger.d("Verification code parsed: $verificationCode")

                    onSmsReceived(message, verificationCode)
                }
            } else {
                Logger.d("Consent denied. User can type OTC manually.")
            }
        }

    if (shouldRegisterReceiver.value) {
        SystemBroadcastReceiver(
            systemAction = SmsRetriever.SMS_RETRIEVED_ACTION
        ) { intent ->
            if (intent != null && SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
                val extras = intent.extras

                val smsRetrieverStatus = extras?.get(SmsRetriever.EXTRA_STATUS) as Status
                when (smsRetrieverStatus.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        // Get consent intent
                        val consentIntent =
                            extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                        try {
                            // Start activity to show consent dialog to user, activity must be started in
                            // 5 minutes, otherwise you'll receive another TIMEOUT intent
                            launcher.launch(consentIntent)
                        } catch (e: ActivityNotFoundException) {
                            Logger.d("Activity Not found for SMS consent API")
                        }
                    }

                    CommonStatusCodes.TIMEOUT -> Logger.d("Timeout in sms verification receiver")
                }
            }
        }
    }
}