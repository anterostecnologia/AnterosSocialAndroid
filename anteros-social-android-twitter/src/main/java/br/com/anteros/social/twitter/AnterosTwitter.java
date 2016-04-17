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

package br.com.anteros.social.twitter;

import android.app.Activity;
import android.content.Intent;

import br.com.anteros.social.core.AnterosSocialConfiguration;
import br.com.anteros.social.core.AnterosSocialNetwork;
import br.com.anteros.social.core.AnterosSocialSession;
import br.com.anteros.social.core.OnLoginListener;
import br.com.anteros.social.core.OnLogoutListener;
import br.com.anteros.social.core.OnProfileListener;

/**
 * Created by edson on 28/03/16.
 */
public class AnterosTwitter implements AnterosSocialNetwork {

    private AnterosTwitterConfiguration configuration;
    private AnterosTwitterSession session = null;
    private OnLoginListener onLoginListener;
    private OnLogoutListener onLogoutListener;

    private AnterosTwitter() {
    }

    public static AnterosTwitter create(Activity activity, OnLoginListener onLoginListener, OnLogoutListener onLogoutListener, String twitterkey, String twitterSecret) {
        return new AnterosTwitter(activity,onLoginListener,onLogoutListener,twitterkey, twitterSecret);
    }

    public static AnterosTwitter create(AnterosSocialConfiguration configuration) {
        return new AnterosTwitter(configuration);
    }

    private AnterosTwitter(Activity activity, OnLoginListener onLoginListener, OnLogoutListener onLogoutListener, String twitterkey, String twitterSecret) {
        this(new AnterosTwitterConfiguration.Builder().activity(activity).onLoginListener(onLoginListener).onLogoutListener(onLogoutListener).twitterkey(twitterkey).twitterSecret(twitterSecret).build());
    }

    private AnterosTwitter(AnterosSocialConfiguration configuration) {
        this.configuration = (AnterosTwitterConfiguration) configuration;
        session = new AnterosTwitterSession((AnterosTwitterConfiguration) configuration);
    }

    public void setConfiguration(AnterosSocialConfiguration configuration) {
        this.configuration = (AnterosTwitterConfiguration) configuration;
    }

    public AnterosSocialConfiguration getConfiguration() {
        return configuration;
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
        this.onLoginListener = onLoginListener;
        session.setOnLoginListener(onLoginListener);
    }

    public void setOnLogoutListener(OnLogoutListener onLogoutListener) {
        this.onLogoutListener = onLogoutListener;
        session.setOnLogoutListener(onLogoutListener);
    }

}
