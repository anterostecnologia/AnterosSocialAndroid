package br.com.anteros.social.twitter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import br.com.anteros.social.AnterosSocialConfiguration;
import br.com.anteros.social.AnterosSocialNetwork;
import br.com.anteros.social.AnterosSocialSession;
import br.com.anteros.social.OnLoginListener;
import br.com.anteros.social.OnLogoutListener;
import br.com.anteros.social.OnProfileListener;
import br.com.anteros.social.instagram.AnterosInstagramConfiguration;
import br.com.anteros.social.instagram.AnterosInstagramSession;
import io.fabric.sdk.android.Fabric;

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
