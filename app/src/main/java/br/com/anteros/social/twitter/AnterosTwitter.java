package br.com.anteros.social.twitter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import br.com.anteros.social.twitter.listeners.OnLoginTwitterListener;
import br.com.anteros.social.twitter.listeners.OnLogoutTwitterListener;
import br.com.anteros.social.twitter.listeners.OnProfileTwitterListener;
import io.fabric.sdk.android.Fabric;

/**
 * Created by edson on 28/03/16.
 */
public class AnterosTwitter {

    private static AnterosTwitter instance = null;
    private static AnterosTwitterConfiguration configuration = new AnterosTwitterConfiguration.Builder().build();

    private static AnterosTwitterSessionManager sessionManager = null;
    private OnLoginTwitterListener onLoginTwitterListener;
    private OnLogoutTwitterListener onLogoutTwitterListener;


    private AnterosTwitter() {
    }

    /**
     * Inicializa a biblioteca do Twitter passando uma {@link Activity}. Este tipo de inicialização
     * deve ser usada caso você tenha uma activity base e muitos fragmentos. Este método inicializa a biblioteca e retorna uma
     * instância da classe {@link AnterosTwitter#getInstance()} para ser utilizada.
     *
     * @param activity
     *            FragmentActivity
     */
    public static void initialize(FragmentActivity activity, String twitterkey, String twitterSecret) {
        if (instance == null) {
            TwitterAuthConfig authConfig = new TwitterAuthConfig(twitterkey, twitterSecret);
            Fabric.with(activity, new Twitter(authConfig));
            instance = new AnterosTwitter();
            sessionManager = new AnterosTwitterSessionManager(configuration);
        }
        sessionManager.setActivity(activity);
    }

    /**
     * Get the instance of {@link AnterosTwitter}. This method, not only returns
     * a singleton instance of {@link AnterosTwitter} but also updates the
     * current activity with the passed activity. <br>
     * If you have more than one <code>Activity</code> in your application. And
     * more than one activity do something with facebook. Then, call this method
     * in {@link Activity#onResume()} method
     *
     * <pre>
     * &#064;Override
     * protected void onResume() {
     * 	super.onResume();
     * 	anterosTwitter = AnterosTwitter.getInstance(this);
     * }
     * </pre>
     *
     * @param activity
     * @return {@link AnterosTwitter} instance
     */
    public static AnterosTwitter getInstance(FragmentActivity activity, String twitterkey, String twitterSecret, OnLoginTwitterListener onLoginTwitterListener, OnLogoutTwitterListener onLogoutTwitterListener) {
        if (instance == null) {
            TwitterAuthConfig authConfig = new TwitterAuthConfig(twitterkey, twitterSecret);
            Fabric.with(activity, new Twitter(authConfig));
            instance = new AnterosTwitter();
            sessionManager = new AnterosTwitterSessionManager(configuration);
            sessionManager.setOnLoginTwitterListener(onLoginTwitterListener);
            sessionManager.setOnLogoutTwitterListener(onLogoutTwitterListener);
        }
        instance.onLoginTwitterListener = onLoginTwitterListener;
        instance.onLogoutTwitterListener = onLogoutTwitterListener;
        sessionManager.setActivity(activity);
        return instance;
    }

    /**
     * Get the instance of {@link AnterosTwitter}. <br>
     * <br>
     * <b>Important:</b> Use this method only after you initialized this library
     * or by: {@link #initialize(android.support.v4.app.FragmentActivity, String,String)} or by {@link #getInstance(FragmentActivity, String, String, OnLoginTwitterListener, OnLogoutTwitterListener)}
     *
     * @return The {@link AnterosTwitter} instance
     */
    public static AnterosTwitter getInstance() {
        return instance;
    }


    /**
     * Set facebook configuration. <b>Make sure</b> to set a configuration
     * before first actual use of this library like (login, getProfile, etc..).
     *
     * @param configuration
     *            The configuration of this library
     */
    public static void setConfiguration(AnterosTwitterConfiguration configuration) {
        configuration = configuration;
        AnterosTwitterSessionManager.configuration = configuration;
    }

    /**
     * Get configuration
     *
     * @return
     */
    public static AnterosTwitterConfiguration getConfiguration() {
        return configuration;
    }


    public void silentLogin() {
        checkListeners();
        sessionManager.silentLogin();
    }

    private void checkListeners() {
        if (onLoginTwitterListener==null) {
            throw new AnterosTwitterException("Listener OnLoginTwitterListener não foi definido.");
        }
        if (onLogoutTwitterListener==null) {
            throw new AnterosTwitterException("Listener OnLogoutTwitterListener não foi definido.");
        }
    }

    /**
     * Login to Twitter
     *
     */
    public void login() {
        checkListeners();
        sessionManager.login();
    }

    /**
     * Logout from Twitter Plus
     */
    public void logout() {
        checkListeners();
        sessionManager.logout();
    }

    /**
     * Logout from Twitter Plus
     */
    public void revoke() {
        checkListeners();
        sessionManager.revoke();
    }

    /**
     * Are we logged in to Twitter Plus
     *
     * @return <code>True</code> if we have active and open session to Twitter Plus
     */
    public boolean isLogged() {
        return sessionManager.isLogged();
    }


    /**
     * Call this inside your activity in {@link Activity#onActivityResult}
     * method
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        sessionManager.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * Get profile . <br>
     *
     * @param onProfileTwitterListener
     *            The callback listener.
     *
     */
    public void getProfile(OnProfileTwitterListener onProfileTwitterListener)  {
        sessionManager.getProfile(onProfileTwitterListener);
    }

    public void setOnLoginTwitterListener(OnLoginTwitterListener onLoginTwitterListener) {
        this.onLoginTwitterListener = onLoginTwitterListener;
        sessionManager.setOnLoginTwitterListener(onLoginTwitterListener);
    }

    public void setOnLogoutTwitterListener(OnLogoutTwitterListener onLogoutTwitterListener) {
        this.onLogoutTwitterListener = onLogoutTwitterListener;
        sessionManager.setOnLogoutTwitterListener(onLogoutTwitterListener);
    }

}
