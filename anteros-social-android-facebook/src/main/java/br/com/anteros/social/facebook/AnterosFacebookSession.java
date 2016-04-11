package br.com.anteros.social.facebook;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.anteros.social.core.AnterosSocialSession;
import br.com.anteros.social.core.OnLoginListener;
import br.com.anteros.social.core.OnLogoutListener;
import br.com.anteros.social.core.OnProfileListener;
import br.com.anteros.social.facebook.actions.Permission;

/**
 * Created by edson on 23/03/16.
 */
public class AnterosFacebookSession implements AnterosSocialSession {


    private final static String TAG = AnterosFacebookSession.class.getName();
    private AnterosFacebookConfiguration configuration;

    private WeakReference<Activity> activity;
    private final LoginManager loginManager;
    private final LoginCallback loginCallback = new LoginCallback();
    private final CallbackManager callbackManager = CallbackManager.Factory.create();
    private WeakReference<OnLoginListener> onLoginListener;
    private WeakReference<OnLogoutListener> onLogoutListener;

    public AnterosFacebookSession(AnterosFacebookConfiguration configuration) {
        this.configuration = configuration;
        this.onLoginListener = new WeakReference<OnLoginListener>(configuration.getOnLoginListener());
        this.onLogoutListener = new WeakReference<OnLogoutListener>(configuration.getOnLogoutListener());
        setActivity(configuration.getActivity());

        loginManager = LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, loginCallback);
        loginManager.setDefaultAudience(configuration.getDefaultAudience());
        loginManager.setLoginBehavior(configuration.getLoginBehavior());
    }

    public AnterosFacebookConfiguration getConfiguration() {
        return configuration;
    }

    public void silentLogin() {
        login();
    }

    public void revoke() {
            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions", null, HttpMethod.DELETE, new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse response) {
                    boolean isSuccess = false;
                    try {
                        if (response.getJSONObject()!=null) {
                            isSuccess = response.getJSONObject().getBoolean("success");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (isSuccess && response.getError()==null){
                        onLogoutListener.get().onLogout();
                    }

                }
            }).executeAsync();
    }

    public class LoginCallback implements FacebookCallback<LoginResult> {

        public OnLoginListener loginListener;
        boolean doOnLogin = false;
        boolean askPublishPermissions = false;
        List<String> publishPermissions;

        @Override
        public void onSuccess(LoginResult loginResult) {
            if (loginListener != null) {

                if (doOnLogin) {
                    doOnLogin = false;
                    askPublishPermissions = false;
                    publishPermissions = null;
                    loginListener.onLogin();
                    return;
                }

                if (askPublishPermissions && publishPermissions != null) {
                    doOnLogin = true;
                    askPublishPermissions = false;
                    requestPublishPermissions(publishPermissions);
                } else {
                    loginListener.onLogin();
                }

            }
        }

        @Override
        public void onCancel() {
            loginListener.onFail(new AnterosFacebookException("FacebookUser canceled the permissions dialog"));
        }

        @Override
        public void onError(FacebookException e) {
            loginListener.onFail(e);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void login() {
        if (isLogged()) {
            Log.i(TAG, "You were already logged in before calling 'login()' method.");
            LoginResult loginResult = createLastLoginResult();
            String token = loginResult.getAccessToken().getToken();
            List<Permission> acceptedPermissions = Permission.convert(getAcceptedPermissions());
            List<Permission> declinedPermissions = new ArrayList<>();
            onLoginListener.get().onLogin();
            return;
        }

        if (hasPendingRequest()) {
            Log.w(TAG, "You are trying to login one more time, before finishing the previous login call");
            onLoginListener.get().onFail(new AnterosFacebookException("Already has pending login request"));
            return;
        }

        loginCallback.loginListener = onLoginListener.get();

        if (configuration.hasPublishPermissions() && configuration.isAllPermissionsAtOnce()) {
            loginCallback.askPublishPermissions = true;
            loginCallback.publishPermissions = configuration.getPublishPermissions();
        }
        requestReadPermissions(configuration.getReadPermissions());
    }

    public void requestReadPermissions(List<String> permissions) {
        loginManager.logInWithReadPermissions(activity.get(), permissions);
    }

    public void requestPublishPermissions(List<String> permissions) {
        loginManager.logInWithPublishPermissions(activity.get(), permissions);
    }

    private LoginResult createLastLoginResult() {
        return new LoginResult(getAccessToken(), getAccessToken().getPermissions(), getAccessToken().getDeclinedPermissions());
    }

    public void logout() {
        loginManager.logOut();
        onLogoutListener.get().onLogout();
    }


    public boolean isLogged() {
        AccessToken accessToken = getAccessToken();
        if (accessToken == null) {
            return false;
        }
        return !accessToken.isExpired();
    }


    public AccessToken getAccessToken() {
        return AccessToken.getCurrentAccessToken();
    }

    public Set<String> getAcceptedPermissions() {
        AccessToken accessToken = getAccessToken();
        if (accessToken == null) {
            return new HashSet<>();
        }
        return accessToken.getPermissions();
    }

    public Set<String> getNotAcceptedPermissions() {
        AccessToken accessToken = getAccessToken();
        if (accessToken == null) {
            return null;
        }
        return accessToken.getDeclinedPermissions();
    }

    public void setActivity(Activity activity) {
        this.activity = new WeakReference<Activity>(activity);
    }

    public Activity getActivity() {
        return activity.get();
    }

    @Override
    public void getProfile(OnProfileListener onProfileListener) {

    }

    public LoginCallback getLoginCallback() {
        return loginCallback;
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    public boolean hasAccepted(String permission) {
        if (getAcceptedPermissions().contains(permission)) {
            return true;
        }
        return false;
    }


    public boolean hasPendingRequest() {
        return false;
    }

    private List<String> getNotGrantedReadPermissions() {
        Set<String> grantedPermissions = getAcceptedPermissions();
        List<String> readPermissions = new ArrayList<>(configuration.getReadPermissions());
        readPermissions.removeAll(grantedPermissions);
        return readPermissions;
    }

    private List<String> getNotGrantedPublishPermissions() {
        Set<String> grantedPermissions = getAcceptedPermissions();
        List<String> publishPermissions = new ArrayList<String>(configuration.getPublishPermissions());
        publishPermissions.removeAll(grantedPermissions);
        return publishPermissions;
    }

    public boolean isAllPermissionsGranted() {
        if (getNotGrantedReadPermissions().size() > 0 || getNotGrantedPublishPermissions().size() > 0) {
            return false;
        }
        return true;
    }

    public void setOnLoginListener(OnLoginListener onLoginListener) {
        this.onLoginListener = new WeakReference<OnLoginListener>(onLoginListener);
    }

    public void setOnLogoutListener(OnLogoutListener onLogoutListener) {
        this.onLogoutListener = new WeakReference<OnLogoutListener>(onLogoutListener);
    }

    @Override
    public void checkListeners() {
        if (onLoginListener ==null) {
            throw new AnterosFacebookException("Listener OnLoginFacebookListener não foi definido.");
        }
        if (onLogoutListener ==null) {
            throw new AnterosFacebookException("Listener OnLogoutFacebookListener não foi definido.");
        }
    }


}
