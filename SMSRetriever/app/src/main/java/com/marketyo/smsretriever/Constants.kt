package com.marketyo.smsretriever

import androidx.compose.runtime.mutableStateOf

object Constants {

    internal fun getVerificationCodeFromSms(sms: String, smsCodeLength: Int): String =
        sms.filter { it.isDigit() }
            .substring(0 until smsCodeLength)

}

var shouldRegisterReceiver = mutableStateOf(false)
var isRestartReciever = mutableStateOf(false)
var sCode = mutableStateOf("")