package com.tokbox.sample.basicvideocapturercamera2;

import android.text.TextUtils;
import androidx.annotation.NonNull;

public class OpenTokConfig {
    /*
    Fill the following variables using your own Project info from the OpenTok dashboard
    https://dashboard.tokbox.com/projects
    */

    // Replace with a API key
    public static final String API_KEY = "47369691";

    // Replace with a generated Session ID
    public static final String SESSION_ID = "1_MX40NzM2OTY5MX5-MTYzNTkwMjQ0OTgxNX5xbUNMNGdXelU3TGNWdmRMSklNR3M1OGl-fg";

    // Replace with a generated token (from the dashboard or using an OpenTok server SDK)
    public static final String TOKEN = "T1==cGFydG5lcl9pZD00NzM2OTY5MSZzaWc9NzI4MjE5ODgxMGFkYjhjODVhMDY2MTMwZjhmMDU2ZTgyY2EwZjk1OTpzZXNzaW9uX2lkPTFfTVg0ME56TTJPVFk1TVg1LU1UWXpOVGt3TWpRME9UZ3hOWDV4YlVOTU5HZFhlbFUzVEdOV2RtUk1Ta2xOUjNNMU9HbC1mZyZjcmVhdGVfdGltZT0xNjM1OTAyNDY1Jm5vbmNlPTAuMzA4NDM4MjgwMjYzNjYxNiZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNjM2NTA3MjY0JmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";

    public static boolean isValid() {
        if (TextUtils.isEmpty(OpenTokConfig.API_KEY)
                || TextUtils.isEmpty(OpenTokConfig.SESSION_ID)
                || TextUtils.isEmpty(OpenTokConfig.TOKEN)) {
            return false;
        }

        return true;
    }

    @NonNull
    public static String getDescription() {
        return "OpenTokConfig:" + "\n"
                + "API_KEY: " + OpenTokConfig.API_KEY + "\n"
                + "SESSION_ID: " + OpenTokConfig.SESSION_ID + "\n"
                + "TOKEN: " + OpenTokConfig.TOKEN + "\n";
    }
}
