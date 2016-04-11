/*
 * ******************************************************************************
 *  * Copyright 2016 Anteros Tecnologia
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.linkedin.platform;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.linkedin.platform.errors.LIAppErrorCode;
import com.linkedin.platform.errors.LIDeepLinkError;
import com.linkedin.platform.internals.AppStore;
import com.linkedin.platform.internals.LIAppVersion;
import com.linkedin.platform.listeners.DeepLinkListener;

/**
 * DeepLinkHelper enables linking to pages within the LinkedIn application
 */
public class DeepLinkHelper {

    public static final int LI_SDK_CROSSLINK_REQUEST_CODE = 13287;
    private static final String TAG = DeepLinkHelper.class.getName();
    private static final String CURRENTLY_LOGGED_IN_MEMBER = "you";
    private static final String DEEPLINK_ERROR_CODE_EXTRA_NAME = "com.linkedin.thirdparty.deeplink.EXTRA_ERROR_CODE";
    private static final String DEEPLINK_ERROR_MESSAGE_EXTRA_NAME = "com.linkedin.thirdparty.deeplink.EXTRA_ERROR_MESSAGE";
    private static DeepLinkHelper deepLinkHelper;
    private DeepLinkListener deepLinkListener;

    public static DeepLinkHelper getInstance() {
        if (deepLinkHelper == null) {
            deepLinkHelper = new DeepLinkHelper();
        }
        return deepLinkHelper;
    }

    /**
     * opens up a view which shows the profile of the user that is currently logged in to the
     * LinkedIn app.
     * @param activity
     * @param callback
     */
    public void openCurrentProfile(@NonNull Activity activity, DeepLinkListener callback) {
        openOtherProfile(activity, CURRENTLY_LOGGED_IN_MEMBER, callback);
    }

    /**
     * opens a view which shows the profile of the given member
     * @param activity
     * @param memberId obtained through an api call
     * @param callback
     */
    public void openOtherProfile(@NonNull Activity activity, String memberId, DeepLinkListener callback) {
        this.deepLinkListener = callback;

        LISession session = LISessionManager.getInstance(activity.getApplicationContext()).getSession();
        if (!session.isValid()) {
            callback.onDeepLinkError(new LIDeepLinkError(LIAppErrorCode.NOT_AUTHENTICATED, "there is no access token"));
            return;
        }
        try {
            if (!LIAppVersion.isLIAppCurrent(activity)) {
                AppStore.goAppStore(activity, true);
                return;
            }
            deepLinkToProfile(activity, memberId, session.getAccessToken());
        } catch (ActivityNotFoundException e) {
            callback.onDeepLinkError(new LIDeepLinkError(LIAppErrorCode.LINKEDIN_APP_NOT_FOUND,
                    "LinkedIn app needs to be either installed or` updated"));
            deepLinkListener = null;
        }
    }

    private void deepLinkToProfile(@NonNull Activity activity, String memberId, @NonNull AccessToken accessToken) {
        Intent i = new Intent("android.intent.action.VIEW");
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("linkedin");
        if (CURRENTLY_LOGGED_IN_MEMBER.equals(memberId)) {
            uriBuilder.authority(CURRENTLY_LOGGED_IN_MEMBER);
        } else {
            uriBuilder.authority("profile").appendPath(memberId);
        }
        uriBuilder.appendQueryParameter("accessToken", accessToken.getValue());
        uriBuilder.appendQueryParameter("src", "sdk");
        i.setData(uriBuilder.build());
        activity.startActivityForResult(i, LI_SDK_CROSSLINK_REQUEST_CODE);
    }

    /**
     * call this method in your activity's onActivityResult method.
     * Handles any response code from LinkedIn and calls the DeepLinkListener callback
     * @param activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == LI_SDK_CROSSLINK_REQUEST_CODE && deepLinkListener != null) {
            if (resultCode == Activity.RESULT_OK) {
                deepLinkListener.onDeepLinkSuccess();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                if (data == null || data.getExtras() == null) {
                    deepLinkListener.onDeepLinkError(new LIDeepLinkError(LIAppErrorCode.USER_CANCELLED,""));
                }
                else {
                    String errorMessage = data.getExtras().getString(DEEPLINK_ERROR_MESSAGE_EXTRA_NAME);
                    String errorCode = data.getExtras().getString(DEEPLINK_ERROR_CODE_EXTRA_NAME);
                    deepLinkListener.onDeepLinkError(new LIDeepLinkError(errorCode, errorMessage));
                }
            }
        }
    }

}
