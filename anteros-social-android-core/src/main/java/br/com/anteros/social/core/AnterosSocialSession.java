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

package br.com.anteros.social.core;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by edson on 07/04/16.
 */
public interface AnterosSocialSession {

    public void onActivityResult(int requestCode, int resultCode, Intent data);

    public void login();

    public void logout();

    public boolean isLogged();

    public void setActivity(Activity activity);

    public void silentLogin();

    public Activity getActivity();

    public void getProfile(OnProfileListener onProfileListener);

    public void revoke();

    public void setOnLoginListener(OnLoginListener onLoginListener);

    public void setOnLogoutListener(OnLogoutListener onLogoutListener);

    public void checkListeners();
}
