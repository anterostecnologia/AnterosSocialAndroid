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

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

public class LIAppVersion {

    public static final String LI_APP_PACKAGE_NAME = "com.linkedin.android";

    public static boolean isLIAppCurrent(@NonNull Context ctx) {

        return isLIAppCurrent(ctx, LI_APP_PACKAGE_NAME);
    }

    private static boolean isLIAppCurrent(@NonNull Context   ctx, @NonNull String packageName) {
        PackageManager packageManager = ctx.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return packageInfo.versionCode >= BuildConfig.LI_APP_SUPPORTED_VER_CODE;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return false;
    }

}
