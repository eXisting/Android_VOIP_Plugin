package com.example.voip_messages

import android.os.Build
import android.telecom.*
import android.util.Log

class CallConnectionService : ConnectionService() {
    override fun onCreateIncomingConnection(connectionManagerPhoneAccount: PhoneAccountHandle?, request: ConnectionRequest?): Connection {
        Log.i("CallConnectionService", "onCreateIncomingConnection")
        val conn = CallConnection(applicationContext)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            conn.connectionProperties = Connection.PROPERTY_SELF_MANAGED
        }
        conn.setCallerDisplayName("test call", TelecomManager.PRESENTATION_ALLOWED)
//        conn.setAddress(request!!.address, PRESENTATION_ALLOWED)
        conn.setInitializing()
//        conn.videoProvider = MyVideoProvider()
        conn.setActive()

        return conn
    }

    override fun onCreateIncomingConnectionFailed(connectionManagerPhoneAccount: PhoneAccountHandle?, request: ConnectionRequest?) {
        super.onCreateIncomingConnectionFailed(connectionManagerPhoneAccount, request)
        Log.i("CallConnectionService", "create outgoing call failed ")
    }
}