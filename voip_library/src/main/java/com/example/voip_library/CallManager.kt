package com.example.voip_messages

import android.Manifest
import android.annotation.TargetApi
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telecom.PhoneAccount
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.util.Log

class CallManager(context: Context) {

    val telecomManager: TelecomManager
    var phoneAccountHandle: PhoneAccountHandle
    var context:Context
    val title = "CARE AR"

    init {
        telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        this.context = context
        val componentName =  ComponentName(this.context, CallConnectionService::class.java)
        phoneAccountHandle = PhoneAccountHandle(componentName, "Admin")
        val phoneAccount = PhoneAccount.builder(phoneAccountHandle, "Admin").setCapabilities(PhoneAccount.CAPABILITY_SELF_MANAGED).build()


        telecomManager.registerPhoneAccount(phoneAccount)
        val intent = Intent()
        intent.component = ComponentName("com.android.server.telecom", "com.android.server.telecom.settings.EnableAccountPreferenceActivity")
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

    }

    @TargetApi(Build.VERSION_CODES.M)
    fun  startIncomingCall(){
        if (this.context.checkSelfPermission(Manifest.permission.MANAGE_OWN_CALLS) == PackageManager.PERMISSION_GRANTED) {
            val extras = Bundle()
            val uri = Uri.fromParts(PhoneAccount.SCHEME_TEL, title, null)
            extras.putParcelable(TelecomManager.EXTRA_INCOMING_CALL_ADDRESS, uri)
            extras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle)
            extras.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, true)
            val isCallPermitted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                telecomManager.isIncomingCallPermitted(phoneAccountHandle)
            } else {
                true
            }
            Log.i("CallManager", "is incoming call permited = $isCallPermitted")
            telecomManager.addNewIncomingCall(phoneAccountHandle, extras)
        }
    }
}