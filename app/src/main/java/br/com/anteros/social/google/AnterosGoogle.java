package br.com.anteros.social.google;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import br.com.anteros.social.google.listeners.OnLoginGoogleListener;
import br.com.anteros.social.google.listeners.OnLogoutGoogleListener;
import br.com.anteros.social.google.listeners.OnProfileGoogleListener;

/**
 * Created by edson on 25/03/16.
 */
public class AnterosGoogle {

    private static AnterosGoogle instance = null;
    private static AnterosGoogleConfiguration configuration = new AnterosGoogleConfiguration.Builder().build();

    private static AnterosGoogleSessionManager sessionManager = null;
    private OnLoginGoogleListener onLoginGoogleListener;
    private OnLogoutGoogleListener onLogoutGoogleListener;


    private AnterosGoogle() {
    }

    /**
     * Inicializa a biblioteca do Google passando uma {@link Activity}. Este tipo de inicialização
     * deve ser usada caso você tenha uma activity base e muitos fragmentos. Este método inicializa a biblioteca e retorna uma
     * instância da classe {@link AnterosGoogle#getInstance()} para ser utilizada.
     *
     * @param activity
     *            FragmentActivity
     */
    public static void initialize(FragmentActivity activity) {
        if (instance == null) {
            instance = new AnterosGoogle();
            sessionManager = new AnterosGoogleSessionManager(configuration);
        }
        sessionManager.setActivity(activity);
    }

    /**
     * Get the instance of {@link AnterosGoogle}. This method, not only returns
     * a singleton instance of {@link AnterosGoogle} but also updates the
     * current activity with the passed activity. <br>
     * If you have more than one <code>Activity</code> in your application. And
     * more than one activity do something with facebook. Then, call this method
     * in {@link Activity#onResume()} method
     *
     * <pre>
     * &#064;Override
     * protected void onResume() {
     * 	super.onResume();
     * 	anterosGoogle = AnterosGoogle.getInstance(this);
     * }
     * </pre>
     *
     * @param activity
     * @return {@link AnterosGoogle} instance
     */
    public static AnterosGoogle getInstance(FragmentActivity activity, OnLoginGoogleListener onLoginGoogleListener, OnLogoutGoogleListener onLogoutGoogleListener) {
        if (instance == null) {
            instance = new AnterosGoogle();
            sessionManager = new AnterosGoogleSessionManager(configuration);
            sessionManager.setOnLoginGoogleListener(onLoginGoogleListener);
            sessionManager.setOnLogoutGoogleListener(onLogoutGoogleListener);
        }
        instance.onLoginGoogleListener = onLoginGoogleListener;
        instance.onLogoutGoogleListener = onLogoutGoogleListener;
        sessionManager.setActivity(activity);
        return instance;
    }

    /**
     * Get the instance of {@link AnterosGoogle}. <br>
     * <br>
     * <b>Important:</b> Use this method only after you initialized this library
     * or by: {@link #initialize(android.support.v4.app.FragmentActivity)} or by {@link #getInstance(FragmentActivity, OnLoginGoogleListener, OnLogoutGoogleListener)}
     *
     * @return The {@link AnterosGoogle} instance
     */
    public static AnterosGoogle getInstance() {
        return instance;
    }


    /**
     * Set facebook configuration. <b>Make sure</b> to set a configuration
     * before first actual use of this library like (login, getProfile, etc..).
     *
     * @param configuration
     *            The configuration of this library
     */
    public static void setConfiguration(AnterosGoogleConfiguration configuration) {
        configuration = configuration;
        AnterosGoogleSessionManager.configuration = configuration;
    }

    /**
     * Get configuration
     *
     * @return
     */
    public static AnterosGoogleConfiguration getConfiguration() {
        return configuration;
    }


    public void silentLogin() {
        checkListeners();
        sessionManager.silentLogin();
    }

    private void checkListeners() {
        if (onLoginGoogleListener==null) {
            throw new AnterosGoogleException("Listener OnLoginGoogleListener não foi definido.");
        }
        if (onLogoutGoogleListener==null) {
            throw new AnterosGoogleException("Listener OnLogoutGoogleListener não foi definido.");
        }
    }

    /**
     * Login to Google
     *
     */
    public void login() {
        checkListeners();
        sessionManager.login();
    }

    /**
     * Logout from Google Plus
     */
    public void logout() {
        checkListeners();
        sessionManager.logout();
    }

    /**
     * Logout from Google Plus
     */
    public void revoke() {
        checkListeners();
        sessionManager.revoke();
    }

    /**
     * Are we logged in to Google Plus
     *
     * @return <code>True</code> if we have active and open session to Google Plus
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
     * @param onProfileGoogleListener
     *            The callback listener.
     *
     */
    public void getProfile(OnProfileGoogleListener onProfileGoogleListener)  {
       sessionManager.getProfile(onProfileGoogleListener);
    }

    public void setOnLoginGoogleListener(OnLoginGoogleListener onLoginGoogleListener) {
        this.onLoginGoogleListener = onLoginGoogleListener;
        sessionManager.setOnLoginGoogleListener(onLoginGoogleListener);
    }

    public void setOnLogoutGoogleListener(OnLogoutGoogleListener onLogoutGoogleListener) {
        this.onLogoutGoogleListener = onLogoutGoogleListener;
        sessionManager.setOnLogoutGoogleListener(onLogoutGoogleListener);
    }



}
