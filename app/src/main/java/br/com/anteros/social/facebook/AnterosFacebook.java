package br.com.anteros.social.facebook;

import android.app.Activity;
import android.content.Intent;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import java.util.Set;

import br.com.anteros.social.facebook.actions.Permission;
import br.com.anteros.social.facebook.actions.ProfileAction;
import br.com.anteros.social.facebook.entities.FacebookProfile;
import br.com.anteros.social.facebook.listeners.OnLoginFacebookListener;
import br.com.anteros.social.facebook.listeners.OnLogoutFacebookListener;
import br.com.anteros.social.facebook.listeners.OnNewFacebookPermissionsListener;
import br.com.anteros.social.facebook.listeners.OnProfileFacebookListener;
import br.com.anteros.social.facebook.utils.Attributes;
import br.com.anteros.social.facebook.utils.PictureAttributes;

/**
 * Created by edson on 23/03/16.
 */
public class AnterosFacebook {

    private static AnterosFacebook instance = null;
    private static AnterosFacebookConfiguration configuration = new AnterosFacebookConfiguration.Builder().build();

    private static AnterosFacebookSessionManager sessionManager = null;
    private OnLoginFacebookListener onLoginFacebookListener;
    private OnLogoutFacebookListener onLogoutFacebookListener;

    private AnterosFacebook() {
    }

    /**
     * Inicializa a biblioteca do Facebook passando uma {@link Activity}. Este tipo de inicialização
     * deve ser usada caso você tenha uma activity base e muitos fragmentos. Este método inicializa a biblioteca e retorna uma
     * instância da classe {@link br.com.anteros.social.facebook.AnterosFacebook#getInstance()} para ser utilizada.
     *
     * @param activity
     *            Activity
     */
    public static void initialize(Activity activity) {
        FacebookSdk.sdkInitialize(activity.getApplicationContext());
    }

    /**
     * Get the instance of {@link br.com.anteros.social.facebook.AnterosFacebook}. This method, not only returns
     * a singleton instance of {@link br.com.anteros.social.facebook.AnterosFacebook} but also updates the
     * current activity with the passed activity. <br>
     * If you have more than one <code>Activity</code> in your application. And
     * more than one activity do something with facebook. Then, call this method
     * in {@link Activity#onResume()} method
     *
     * <pre>
     * &#064;Override
     * protected void onResume() {
     * 	super.onResume();
     * 	anterosFacebook = AnterosFacebook.getInstance(this);
     * }
     * </pre>
     *
     * @param activity
     * @return {@link br.com.anteros.social.facebook.AnterosFacebook} instance
     */
    public static AnterosFacebook getInstance(Activity activity, OnLoginFacebookListener onLoginFacebookListener, OnLogoutFacebookListener onLogoutFacebookListener) {
        if (instance == null) {
            FacebookSdk.sdkInitialize(activity.getApplicationContext());
            instance = new AnterosFacebook();
            sessionManager = new AnterosFacebookSessionManager(configuration);
            sessionManager.setOnLoginFacebookListener(onLoginFacebookListener);
            sessionManager.setOnLogoutFacebookListener(onLogoutFacebookListener);
        }
        instance.onLoginFacebookListener = onLoginFacebookListener;
        instance.onLogoutFacebookListener = onLogoutFacebookListener;
        sessionManager.setActivity(activity);
        return instance;
    }

    /**
     * Get the instance of {@link br.com.anteros.social.facebook.AnterosFacebook}. <br>
     * <br>
     * <b>Important:</b> Use this method only after you initialized this library
     * or by: {@link #initialize(Activity)} or by {@link #getInstance(Activity, OnLoginFacebookListener, OnLogoutFacebookListener)}
     *
     * @return The {@link br.com.anteros.social.facebook.AnterosFacebook} instance
     */
    public static AnterosFacebook getInstance() {
        return instance;
    }


    /**
     * Set facebook configuration. <b>Make sure</b> to set a configuration
     * before first actual use of this library like (login, getProfile, etc..).
     *
     * @param configuration
     *            The configuration of this library
     */
    public static void setConfiguration(AnterosFacebookConfiguration configuration) {
        configuration = configuration;
        AnterosFacebookSessionManager.configuration = configuration;
    }

    public void setOnLoginFacebookListener(OnLoginFacebookListener onLoginFacebookListener) {
        this.onLoginFacebookListener = onLoginFacebookListener;
        sessionManager.setOnLoginFacebookListener(onLoginFacebookListener);
    }

    public void setOnLogoutFacebookListener(OnLogoutFacebookListener onLogoutFacebookListener) {
        this.onLogoutFacebookListener = onLogoutFacebookListener;
        sessionManager.setOnLogoutFacebookListener(onLogoutFacebookListener);
    }

    /**
     * Get configuration
     *
     * @return
     */
    public static AnterosFacebookConfiguration getConfiguration() {
        return configuration;
    }

    public void silentLogin() {
        checkListeners();
        sessionManager.silentLogin();
    }

    private void checkListeners() {
        if (onLoginFacebookListener==null) {
            throw new AnterosFacebookException("Listener OnLoginFacebookListener não foi definido.");
        }
        if (onLogoutFacebookListener==null) {
            throw new AnterosFacebookException("Listener OnLogoutFacebookListener não foi definido.");
        }
    }

    /**
     * Login to Facebook
     *
     */
    public void login() {
        checkListeners();

        sessionManager.login();
    }

    /**
     * Logout from Facebook
     */
    public void logout() {
        checkListeners();
        sessionManager.logout();
    }

    public void revoke(){
        checkListeners();
        sessionManager.revoke();
    }

    /**
     * Are we logged in to facebook
     *
     * @return <code>True</code> if we have active and open session to facebook
     */
    public boolean isLogged() {
        return sessionManager.isLogged();
    }

    /**
     * Get the current access token
     */
    public AccessToken getAccessToken() {
        return sessionManager.getAccessToken();
    }

    /**
     * Get string access token
     */
    public String getToken() {
        return sessionManager.getAccessToken().getToken();
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
     *
     * Requests any new permission in a runtime. <br>
     * <br>
     * Useful when you just want to request the action and won't be publishing
     * at the time, but still need the updated <b>access token</b> with the
     * permissions (possibly to pass back to your backend).
     *
     * <br>
     * <b>Must be logged to use.</b>
     *
     * @param permissions
     *            New permissions you want to have. This array can include READ
     *            and PUBLISH permissions in the same time. Just ask what you
     *            need.<br>
     * <br>
     * @param onNewFacebookPermissionsListener
     *            The listener for the requesting new permission action.
     */
    public void requestNewPermissions(Permission[] permissions, OnNewFacebookPermissionsListener onNewFacebookPermissionsListener) {
        sessionManager.requestNewPermissions(permissions, onNewFacebookPermissionsListener);
    }

    /**
     * Get the list of all granted permissions. <br>
     * Use {@link Permission#fromValue(String)} to get the {@link Permission}
     * object from string in this list.
     *
     * @return List of granted permissions
     */
    public Set<String> getGrantedPermissions() {
        return sessionManager.getAcceptedPermissions();
    }

    /**
     * @return <code>True</code> if all permissions were granted by the user,
     *         otherwise return <code>False</code>
     */
    public boolean isAllPermissionsGranted() {
        return sessionManager.isAllPermissionsGranted();
    }


    /**
     * Get my profile from facebook.<br>
     * This method will return profile with next default properties depends on
     * permissions you have:<br>
     * <em>id, name, first_name, middle_name, last_name, gender, locale, languages, link, username, timezone, updated_time, verified, bio, birthday, education, email,
     * hometown, location, political, favorite_athletes, favorite_teams, quotes, relationship_status, religion, website, work</em>
     *
     * @param onProfileFacebookListener
     *            The callback listener.
     */
    public void getProfile(OnProfileFacebookListener onProfileFacebookListener) {
        PictureAttributes pictureAttributes = Attributes.createPictureAttributes();
        pictureAttributes.setHeight(100);
        pictureAttributes.setWidth(100);
        pictureAttributes.setType(PictureAttributes.PictureType.SQUARE);
        FacebookProfile.Properties properties = new FacebookProfile.Properties.Builder()
                .add(FacebookProfile.Properties.PICTURE, pictureAttributes)
                .add(FacebookProfile.Properties.FIRST_NAME)
                .add(FacebookProfile.Properties.LAST_NAME)
                .add(FacebookProfile.Properties.AGE_RANGE)
                .add(FacebookProfile.Properties.BIRTHDAY)
                .add(FacebookProfile.Properties.EMAIL)
                .add(FacebookProfile.Properties.GENDER)
                .add(FacebookProfile.Properties.LINK)
                .add(FacebookProfile.Properties.MIDDLE_NAME)
                .build();
        getProfile(null, properties, onProfileFacebookListener);
    }

    /**
     * Get profile by profile id. <br>
     * The default values only will be returned. For more options, see
     * {@link #getProfile(String, FacebookProfile.Properties, OnProfileFacebookListener)}
     *
     * @param profileId
     *            The id of the profile we want to get
     * @param onProfileFacebookListener
     *            The callback listener.
     */
    public void getProfile(String profileId, OnProfileFacebookListener onProfileFacebookListener) {
        PictureAttributes pictureAttributes = Attributes.createPictureAttributes();
        pictureAttributes.setHeight(100);
        pictureAttributes.setWidth(100);
        pictureAttributes.setType(PictureAttributes.PictureType.SQUARE);
        FacebookProfile.Properties properties = new FacebookProfile.Properties.Builder()
                .add(FacebookProfile.Properties.PICTURE, pictureAttributes)
                .add(FacebookProfile.Properties.FIRST_NAME)
                .add(FacebookProfile.Properties.LAST_NAME)
                .add(FacebookProfile.Properties.AGE_RANGE)
                .add(FacebookProfile.Properties.BIRTHDAY)
                .add(FacebookProfile.Properties.EMAIL)
                .add(FacebookProfile.Properties.GENDER)
                .add(FacebookProfile.Properties.LINK)
                .add(FacebookProfile.Properties.MIDDLE_NAME)
                .build();
        getProfile(profileId, properties, onProfileFacebookListener);
    }

    /**
     * Get my profile from facebook by mentioning specific parameters. <br>
     * For example, if you need: <em>square picture 500x500 pixels</em>
     *
     * @param onProfileFacebookListener
     *            The callback listener.
     * @param properties
     *            The {@link FacebookProfile.Properties}. <br>
     *            To create {@link FacebookProfile.Properties} instance use:
     *
     *            <pre>
     * // define the profile picture we want to get
     * PictureAttributes pictureAttributes = Attributes.createPictureAttributes();
     * pictureAttributes.setType(PictureType.SQUARE);
     * pictureAttributes.setHeight(500);
     * pictureAttributes.setWidth(500);
     *
     * // create properties
     * Properties properties = new Properties.Builder().add(Properties.ID).add(Properties.FIRST_NAME).add(Properties.PICTURE, attributes).build();
     * </pre>
     */
    public void getProfile(FacebookProfile.Properties properties, OnProfileFacebookListener onProfileFacebookListener) {
        getProfile(null, properties, onProfileFacebookListener);
    }

    /**
     * Get profile by profile id and mentioning specific parameters. <br>
     *
     * @param profileId
     *            The id of the profile we want to get
     * @param onProfileFacebookListener
     *            The callback listener.
     * @param properties
     *            The {@link FacebookProfile.Properties}. <br>
     *            To create {@link FacebookProfile.Properties} instance use:
     *
     *            <pre>
     * // define the profile picture we want to get
     * PictureAttributes pictureAttributes = Attributes.createPictureAttributes();
     * pictureAttributes.setType(PictureType.SQUARE);
     * pictureAttributes.setHeight(500);
     * pictureAttributes.setWidth(500);
     *
     * // create properties
     * Properties properties = new Properties.Builder().add(Properties.ID).add(Properties.FIRST_NAME).add(Properties.PICTURE, attributes).build();
     * </pre>
     */
    public void getProfile(String profileId, FacebookProfile.Properties properties, OnProfileFacebookListener onProfileFacebookListener) {
        ProfileAction profileAction = new ProfileAction(sessionManager);
        profileAction.setProperties(properties);
        profileAction.setTarget(profileId);
        profileAction.setActionListener(onProfileFacebookListener);
        profileAction.execute();
    }
}
