package br.com.anteros.social.google;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import br.com.anteros.social.AnterosSocialConfiguration;
import br.com.anteros.social.AnterosSocialNetwork;
import br.com.anteros.social.AnterosSocialSession;
import br.com.anteros.social.OnLoginListener;
import br.com.anteros.social.OnLogoutListener;
import br.com.anteros.social.OnProfileListener;

/**
 * Created by edson on 25/03/16.
 */
public class AnterosGoogle implements AnterosSocialNetwork {

    private AnterosGoogleConfiguration configuration = null;
    private AnterosGoogleSession session = null;


    private AnterosGoogle() {
    }

    public static AnterosGoogle create(FragmentActivity activity, OnLoginListener onLoginListener, OnLogoutListener onLogoutListener) {
        return new AnterosGoogle(activity,onLoginListener,onLogoutListener);
    }

    public static AnterosGoogle create(AnterosSocialConfiguration configuration) {
        return new AnterosGoogle(configuration);
    }

    private AnterosGoogle(FragmentActivity activity, OnLoginListener onLoginListener, OnLogoutListener onLogoutListener) {
        this(new AnterosGoogleConfiguration.Builder().activity(activity).onLoginListener(onLoginListener).onLogoutListener(onLogoutListener).build());
    }

    private AnterosGoogle(AnterosSocialConfiguration configuration) {
        this.configuration = (AnterosGoogleConfiguration) configuration;
        session = new AnterosGoogleSession((AnterosGoogleConfiguration) configuration);
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
