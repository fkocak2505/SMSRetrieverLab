package com.marketyo.smsretriever

object Constants {

    internal fun getVerificationCodeFromSms(sms: String, smsCodeLength: Int): String =
        sms.filter { it.isDigit() }
            .substring(0 until smsCodeLength)

}