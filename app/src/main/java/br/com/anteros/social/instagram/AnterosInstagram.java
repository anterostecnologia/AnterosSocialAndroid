package br.com.anteros.social.instagram;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import br.com.anteros.social.instagram.listeners.OnLoginInstagramListener;
import br.com.anteros.social.instagram.listeners.OnLogoutInstagramListener;
import br.com.anteros.social.instagram.listeners.OnProfileInstagramListener;

/**
 * Created by edson on 05/04/16.
 */
public class AnterosInstagram {

    private static AnterosInstagram instance = null;
    private static AnterosInstagramConfiguration configuration = new AnterosInstagramConfiguration.Builder().build();

    private static AnterosInstagramSessionManager sessionManager = null;
    private OnLoginInstagramListener onLoginInstagramListener;
    private OnLogoutInstagramListener onLogoutInstagramListener;


    private AnterosInstagram() {
    }

    /**
     * Inicializa a biblioteca do Instagram passando uma {@link FragmentActivity}. Este tipo de inicialização
     * deve ser usada caso você tenha uma activity base e muitos fragmentos. Este método inicializa a biblioteca e retorna uma
     * instância da classe {@link AnterosInstagram#getInstance()} para ser utilizada.
     *
     * @param activity
     *            FragmentActivity
     */
    public static void initialize(FragmentActivity activity, String clientId, String clientSecret, String redirectURL, String scope) {
        if (instance == null) {
            instance = new AnterosInstagram();
            sessionManager = new AnterosInstagramSessionManager(activity,clientId,clientSecret,redirectURL,scope);
        }
        sessionManager.setActivity(activity);
    }

    /**
     * Get the instance of {@link AnterosInstagram}. This method, not only returns
     * a singleton instance of {@link AnterosInstagram} but also updates the
     * current activity with the passed activity. <br>
     * If you have more than one <code>Activity</code> in your application. And
     * more than one activity do something with facebook. Then, call this method
     * in {@link Activity#onResume()} method
     *
     * <pre>
     * &#064;Override
     * protected void onResume() {
     * 	super.onResume();
     * 	anterosInstagram = AnterosInstagram.getInstance(this);
     * }
     * </pre>
     *
     * @param activity
     * @return {@link AnterosInstagram} instance
     */
    public static AnterosInstagram getInstance(FragmentActivity activity, OnLoginInstagramListener onLoginInstagramListener, OnLogoutInstagramListener onLogoutInstagramListener, String clientId, String clientSecret, String redirectURL, String scope) {
        if (instance == null) {
            instance = new AnterosInstagram();
            sessionManager = new AnterosInstagramSessionManager(activity, clientId, clientSecret, redirectURL, scope);
            sessionManager.setOnLoginInstagramListener(onLoginInstagramListener);
            sessionManager.setOnLogoutInstagramListener(onLogoutInstagramListener);
        }
        instance.onLoginInstagramListener = onLoginInstagramListener;
        instance.onLogoutInstagramListener = onLogoutInstagramListener;
        sessionManager.setActivity(activity);
        return instance;
    }

    /**
     * Get the instance of {@link AnterosInstagram}. <br>
     * <br>
     * <b>Important:</b> Use this method only after you initialized this library
     * or by: {@link #initialize(android.support.v4.app.FragmentActivity)} or by {@link #getInstance(FragmentActivity, OnLoginInstagramListener, OnLogoutInstagramListener)}
     *
     * @return The {@link AnterosInstagram} instance
     */
    public static AnterosInstagram getInstance() {
        return instance;
    }


    /**
     * Set facebook configuration. <b>Make sure</b> to set a configuration
     * before first actual use of this library like (login, getProfile, etc..).
     *
     * @param configuration
     *            The configuration of this library
     */
    public static void setConfiguration(AnterosInstagramConfiguration configuration) {
        configuration = configuration;
        AnterosInstagramSessionManager.configuration = configuration;
    }

    /**
     * Get configuration
     *
     * @return
     */
    public static AnterosInstagramConfiguration getConfiguration() {
        return configuration;
    }


    public void silentLogin() {
        checkListeners();
        sessionManager.silentLogin();
    }

    private void checkListeners() {
        if (onLoginInstagramListener==null) {
            throw new AnterosInstagramException("Listener OnLoginInstagramListener não foi definido.");
        }
        if (onLogoutInstagramListener==null) {
            throw new AnterosInstagramException("Listener OnLogoutInstagramListener não foi definido.");
        }
    }

    /**
     * Login to Instagram
     *
     */
    public void login() {
        checkListeners();
        sessionManager.login();
    }

    /**
     * Logout from Instagram Plus
     */
    public void logout() {
        checkListeners();
        sessionManager.logout();
    }

    /**
     * Logout from Instagram Plus
     */
    public void revoke() {
        checkListeners();
        sessionManager.revoke();
    }

    /**
     * Are we logged in to Instagram Plus
     *
     * @return <code>True</code> if we have active and open session to Instagram Plus
     */
    public boolean isLogged() {
        return sessionManager.isLogged();
    }


    /**
     * Call this inside your activity in {@link android.app.Activity#onActivityResult}
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
     * @param onProfileInstagramListener
     *            The callback listener.
     *
     */
    public void getProfile(OnProfileInstagramListener onProfileInstagramListener)  {
        sessionManager.getProfile(onProfileInstagramListener);
    }

    public void setOnLoginInstagramListener(OnLoginInstagramListener onLoginInstagramListener) {
        this.onLoginInstagramListener = onLoginInstagramListener;
        sessionManager.setOnLoginInstagramListener(onLoginInstagramListener);
    }

    public void setOnLogoutInstagramListener(OnLogoutInstagramListener onLogoutInstagramListener) {
        this.onLogoutInstagramListener = onLogoutInstagramListener;
        sessionManager.setOnLogoutInstagramListener(onLogoutInstagramListener);
    }
}

