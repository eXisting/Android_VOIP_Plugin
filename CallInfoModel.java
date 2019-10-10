package io.CareAR.connect;

import android.os.Bundle;

public class CallInfoModel {
    private Bundle info;

    public CallInfoModel(Bundle bundle) {
        info = bundle;
    }

    public Boolean IsHasCallInfo() {
        return info != null;
    }

    public String BuildInfoString() {
        String roomKey = VoIPMessagingService.SESSION_ID_KEY;
        String callerKey = VoIPMessagingService.CALLER_KEY;
        String roomID = info.getString(roomKey);
        String caller = info.getString(callerKey);

        StringBuilder builder = new StringBuilder();
        builder.append(roomKey);
        builder.append("=");
        builder.append(roomID);
        builder.append("|");
        builder.append(callerKey);
        builder.append("=");
        builder.append(caller);

        return builder.toString();
    }

    public Bundle GetInfoAsBundle() {
        return info;
    }

    public void Reset() {
        info = null;
    }
}
