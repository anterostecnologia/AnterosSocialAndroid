package br.com.anteros.social.instagram;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import java.lang.ref.WeakReference;

import br.com.anteros.social.AnterosSocialConfiguration;
import br.com.anteros.social.AnterosSocialNetwork;
import br.com.anteros.social.AnterosSocialSession;
import br.com.anteros.social.OnLoginListener;
import br.com.anteros.social.OnLogoutListener;
import br.com.anteros.social.OnProfileListener;

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

