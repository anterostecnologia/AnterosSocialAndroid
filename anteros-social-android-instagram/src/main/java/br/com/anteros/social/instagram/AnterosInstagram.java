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

package br.com.anteros.social.instagram;

import android.app.Activity;
import android.content.Intent;

import br.com.anteros.social.core.AnterosSocialConfiguration;
import br.com.anteros.social.core.AnterosSocialNetwork;
import br.com.anteros.social.core.AnterosSocialSession;
import br.com.anteros.social.core.OnLoginListener;
import br.com.anteros.social.core.OnLogoutListener;
import br.com.anteros.social.core.OnProfileListener;

/**
 * Created by edson on 05/04/16.
 */
public class AnterosInstagram implements AnterosSocialNetwork {

    private AnterosInstagramConfiguration configuration;
    private AnterosSocialSession session = null;

    private AnterosInstagram() {
    }

    public static AnterosInstagram create(Activity activity, OnLoginListener onLoginListener, OnLogoutListener onLogoutListener, String clientId, String clientSecret, String redirectURL, String scope) {
        return new AnterosInstagram(activity,onLoginListener,onLogoutListener,clientId,clientSecret,redirectURL,scope);
    }

    public static AnterosInstagram create(AnterosSocialConfiguration configuration) {
        return new AnterosInstagram(configuration);
    }

    private AnterosInstagram(Activity activity, OnLoginListener onLoginListener, OnLogoutListener onLogoutListener, String clientId, String clientSecret, String redirectURL, String scope) {
        this(new AnterosInstagramConfiguration.Builder().activity(activity).onLoginListener(onLoginListener).onLogoutListener(onLogoutListener).clientId(clientId).clientSecret(clientSecret).redirectURL(redirectURL).scope(scope).build());
    }

    private AnterosInstagram(AnterosSocialConfiguration configuration) {
        this.configuration = (AnterosInstagramConfiguration) configuration;
        session = new AnterosInstagramSession((AnterosInstagramConfiguration) configuration);
    }

    public void silentLogin() {
        checkListeners();
        session.silentLogin();
    }

    private void checkListeners() {
       session.checkListeners();
    }

    public void login() {
        checkListeners();
        session.login();
    }


    public void logout() {
        checkListeners();
        session.logout();
    }


    public void revoke() {
        checkListeners();
        session.revoke();
    }


    public boolean isLogged() {
        return session.isLogged();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        session.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public AnterosSocialSession getSession() {
        return session;
    }


    public void getProfile(OnProfileListener onProfileListener)  {
        session.getProfile(onProfileListener);
    }

    public void setOnLoginListener(OnLoginListener onLoginListener) {
        session.setOnLoginListener(onLoginListener);
    }

    public void setOnLogoutListener(OnLogoutListener onLogoutListener) {
        session.setOnLogoutListener(onLogoutListener);
    }
}

