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

package com.linkedin.platform.internals;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import br.com.anteros.social.linkedin.R;


public class AppStore {

    public static void goAppStore(final Activity activity, boolean showDialog) {
        if (!showDialog) {
            goToAppStore(activity);
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(R.string.update_linkedin_app_message)
                .setTitle(R.string.update_linkedin_app_title);
        builder.setPositiveButton(R.string.update_linkedin_app_download, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                goToAppStore(activity);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(R.string.update_linkedin_app_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    private static void goToAppStore(final Activity activity) {
        SupportedAppStore appStore = SupportedAppStore.fromDeviceManufacturer();
        String appStoreUri = appStore.getAppStoreUri();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(appStoreUri));
        try {
            activity.startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            //should not happen
        }
    }

    private static enum SupportedAppStore {
        amazonAppstore("amazon", "amzn://apps/android?p=com.linkedin.android"),
        googlePlay("google", "market://details?id=com.linkedin.android"),
        samsungApps("samsung", "samsungapps://ProductDetail/com.linkedin.android")
        ;

        private final String deviceManufacturer;
        private final String appStoreUri;

        private SupportedAppStore(String deviceManufacturer, String appStoreUri) {
            this.deviceManufacturer = deviceManufacturer;
            this.appStoreUri = appStoreUri;
        }

        public String getDeviceManufacturer() {
            return deviceManufacturer;
        }

        public String getAppStoreUri() {
            return appStoreUri;
        }

        public static SupportedAppStore fromDeviceManufacturer() {
            for (SupportedAppStore appStore : values()) {
                if(appStore.getDeviceManufacturer().equalsIgnoreCase(Build.MANUFACTURER)) {
                    return appStore;
                }
            }
            //return google play by default
            return googlePlay;
        }
    };

}
