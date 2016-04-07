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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.anteros.social.facebook.actions.Permission;
import br.com.anteros.social.facebook.listeners.OnLoginFacebookListener;
import br.com.anteros.social.facebook.listeners.OnLogoutFacebookListener;
import br.com.anteros.social.facebook.listeners.OnNewFacebookPermissionsListener;

/**
 * Created by edson on 23/03/16.
 */
public class AnterosFacebookSessionManager {


    private final static String TAG = AnterosFacebookSessionManager.class.getName();
    static AnterosFacebookConfiguration configuration;

    private WeakReference<Activity> activity;
    private final LoginManager loginManager;
    private final LoginCallback loginCallback = new LoginCallback();
    private final CallbackManager callbackManager = CallbackManager.Factory.create();
    private OnLoginFacebookListener onLoginFacebookListener;
    private OnLogoutFacebookListener onLogoutFacebookListener;

    public AnterosFacebookSessionManager(AnterosFacebookConfiguration configuration) {
        AnterosFacebookSessionManager.configuration = configuration;
        loginManager = LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, loginCallback);
        loginManager.setDefaultAudience(configuration.getDefaultAudience());
        loginManager.setLoginBehavior(configuration.getLoginBehavior());
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
                        onLogoutFacebookListener.onLogout();
                    }

                }
            }).executeAsync();
    }

    public class LoginCallback implements FacebookCallback<LoginResult> {

        public OnLoginFacebookListener loginListener;
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
                    loginListener.onLogin(loginResult.getAccessToken().getToken(), Permission.convert(getAcceptedPermissions()), Permission.convert(loginResult.getRecentlyDeniedPermissions()));
                    return;
                }

                if (askPublishPermissions && publishPermissions != null) {
                    doOnLogin = true;
                    askPublishPermissions = false;
                    requestPublishPermissions(publishPermissions);
                } else {
                    loginListener.onLogin(loginResult.getAccessToken().getToken(), Permission.convert(getAcceptedPermissions()), Permission.convert(loginResult.getRecentlyDeniedPermissions()));
                }

            }
        }

        @Override
        public void onCancel() {
            loginListener.onFail("FacebookUser canceled the permissions dialog");
        }

        @Override
        public void onError(FacebookException e) {
            loginListener.onException(e);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Login to Facebook
     *
     */
    public void login() {
        if (isLogged()) {
            Log.i(TAG, "You were already logged in before calling 'login()' method.");
            LoginResult loginResult = createLastLoginResult();
            String token = loginResult.getAccessToken().getToken();
            List<Permission> acceptedPermissions = Permission.convert(getAcceptedPermissions());
            List<Permission> declinedPermissions = new ArrayList<>();
            onLoginFacebookListener.onLogin(token, acceptedPermissions,declinedPermissions);
            return;
        }

        if (hasPendingRequest()) {
            Log.w(TAG, "You are trying to login one more time, before finishing the previous login call");
            onLoginFacebookListener.onFail("Already has pending login request");
            return;
        }

        // user hasn't the access token with all read acceptedPermissions we need, thus we ask him to login
        loginCallback.loginListener = onLoginFacebookListener;

        // in case of marking in configuration the option of getting publish permission, just after read permissions
        if (configuration.hasPublishPermissions() && configuration.isAllPermissionsAtOnce()) {
            loginCallback.askPublishPermissions = true;
            loginCallback.publishPermissions = configuration.getPublishPermissions();
        }

        // login please, with all read permissions
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

    /**
     * Logout from Facebook
     */
    public void logout() {

        loginManager.logOut();
        onLogoutFacebookListener.onLogout();
    }

    /**
     * Indicate if you are logged in or not.
     *
     * @return <code>True</code> if you is logged in, otherwise return
     *         <code>False</code>
     */
    public boolean isLogged() {
        AccessToken accessToken = getAccessToken();
        if (accessToken == null) {
            return false;
        }
        return !accessToken.isExpired();
    }

    /**
     * Get access token of open session
     *
     */
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

    void setActivity(Activity activity) {
        this.activity = new WeakReference<Activity>(activity);
    }

    public Activity getActivity() {
        return activity.get();
    }

    public LoginCallback getLoginCallback() {
        return loginCallback;
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    /**
     * This checks if user had accepted all publish permissions for being able to use PublishAction
     * @return
     */
    public boolean hasAccepted(String permission) {
        if (getAcceptedPermissions().contains(permission)) {
            return true;
        }
        return false;
    }

    /**
     * Requests any new permission in a runtime. <br>
     * <br>
     *
     * Useful when you just want to request the action and won't be publishing
     * at the time, but still need the updated <b>access token</b> with the
     * permissions (possibly to pass back to your backend).
     *
     * <br>
     * <b>Must be logged in to use.</b>
     *
     * @param perms
     *            New permissions you want to have. This array can include READ
     *            and PUBLISH permissions in the same time. Just ask what you
     *            need.<br>
     * @param onNewPermissionListener
     *            The callback listener for the requesting new permission
     *            action.
     */
    public void requestNewPermissions(final Permission[] perms, final OnNewFacebookPermissionsListener onNewPermissionListener) {

        if (onNewPermissionListener == null) {
            Log.w(TAG, "Must pass listener");
            return;
        }

        List<Permission> permissions = Arrays.asList(perms);

        if (permissions == null || permissions.size() == 0) {
            onNewPermissionListener.onFail("Empty permissions in request");
            return;
        }

        int flag = configuration.getType(permissions);
        if (flag == 0) {
            onNewPermissionListener.onFail("There is no new permissions in your request");
            return;
        }

        loginCallback.loginListener = new OnLoginFacebookListener() {

            @Override
            public void onLogin(String accessToken, List<Permission> acceptedPermissions, List<Permission> declinedPermissions) {
                onNewPermissionListener.onSuccess(accessToken, acceptedPermissions, declinedPermissions);
            }

            @Override
            public void onCancel() {
                onNewPermissionListener.onCancel();
            }

            @Override
            public void onFail(String reason) {
                onNewPermissionListener.onFail(reason);
            }

            @Override
            public void onException(Throwable throwable) {
                onNewPermissionListener.onException(throwable);
            }

        };

        switch (flag) {
            case 1:
                requestReadPermissions(Permission.convert(permissions));
                break;
            case 3:
                // in case of marking in configuration the option of getting publish permission, just after read permissions
                if (configuration.isAllPermissionsAtOnce()) {
                    loginCallback.askPublishPermissions = true;
                    loginCallback.publishPermissions = Permission.fetchPermissions(permissions, Permission.Type.PUBLISH);
                }
                requestReadPermissions(Permission.fetchPermissions(permissions, Permission.Type.READ));
                break;
            case 2:
                requestPublishPermissions(Permission.convert(permissions));
                break;
        }

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

    public void setOnLoginFacebookListener(OnLoginFacebookListener onLoginFacebookListener) {
        this.onLoginFacebookListener = onLoginFacebookListener;
    }

    public void setOnLogoutFacebookListener(OnLogoutFacebookListener onLogoutFacebookListener) {
        this.onLogoutFacebookListener = onLogoutFacebookListener;
    }


}
