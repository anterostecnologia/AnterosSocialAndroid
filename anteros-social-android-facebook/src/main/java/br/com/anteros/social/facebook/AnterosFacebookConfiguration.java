package br.com.anteros.social.facebook;

import android.app.Activity;

import com.facebook.internal.ServerProtocol;
import com.facebook.login.DefaultAudience;
import com.facebook.login.LoginBehavior;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import br.com.anteros.social.core.AnterosSocialConfiguration;
import br.com.anteros.social.core.OnLoginListener;
import br.com.anteros.social.core.OnLogoutListener;
import br.com.anteros.social.core.SocialNetworkType;
import br.com.anteros.social.facebook.actions.Permission;

/**
 * Created by edson on 23/03/16.
 */
public class AnterosFacebookConfiguration implements AnterosSocialConfiguration {

    private String appId;
    private String namespace;
    private List<String> readPermissions = null;
    private List<String> publishPermissions = null;
    private DefaultAudience defaultAudience = null;
    private LoginBehavior loginBehavior = null;
    private boolean hasPublishPermissions = false;
    boolean allAtOnce = false;
    private boolean useAppsecretProof = false;
    private String appSecret = null;
    private String graphVersion = null;
    private WeakReference<OnLoginListener> onLoginListener;
    private WeakReference<OnLogoutListener> onLogoutListener;
    private WeakReference<Activity> activity;

    public OnLoginListener getOnLoginListener() {
        return onLoginListener.get();
    }

    public OnLogoutListener getOnLogoutListener() {
        return onLogoutListener.get();
    }

    public Activity getActivity() {
        return activity.get();
    }

    private AnterosFacebookConfiguration(Builder builder) {
        this.appId = builder.appId;
        this.namespace = builder.namespace;
        this.readPermissions = builder.readPermissions;
        this.publishPermissions = builder.publishPermissions;
        this.defaultAudience = builder.defaultAudience;
        this.loginBehavior = builder.loginBehavior;
        this.allAtOnce = builder.allAtOnce;
        this.useAppsecretProof = builder.useAppsecretProof;
        this.appSecret = builder.appSecret;
        this.graphVersion = builder.graphVersion;
        this.onLoginListener = new WeakReference<>(builder.onLoginListener);
        this.onLogoutListener = new WeakReference<>(builder.onLogoutListener);
        this.activity = new WeakReference<>(builder.activity);

        if (this.publishPermissions.size() > 0) {
            this.hasPublishPermissions = true;
        }
    }

    public String getAppId() {
        return appId;
    }

    public String getNamespace() {
        return namespace;
    }


    public List<String> getReadPermissions() {
        return readPermissions;
    }

    public List<String> getPublishPermissions() {
        return publishPermissions;
    }

    public String getGraphVersion() {
        return graphVersion;
    }

    boolean hasPublishPermissions() {
        return hasPublishPermissions;
    }

    LoginBehavior getLoginBehavior() {
        return loginBehavior;
    }

    DefaultAudience getDefaultAudience() {
        return defaultAudience;
    }

    public boolean useAppsecretProof() {
        return useAppsecretProof;
    }

    public String getAppSecret() {
        return appSecret;
    }

    boolean isAllPermissionsAtOnce() {
        return allAtOnce;
    }


    int getType(List<Permission> permissions) {
        int flag = 0;

        if (permissions == null || permissions.size() == 0) {
            return flag;
        }

        for (Permission permission : permissions) {
            switch (permission.getType()) {
                case READ:
                    flag |= 1;
                    break;
                case PUBLISH:
                    flag |= 2;
                    break;
                default:
                    break;
            }
        }

        return flag;
    }

    @Override
    public SocialNetworkType getSocialNetworkType() {
        return SocialNetworkType.FACEBOOK;
    }

    public static class Builder {
        private String appId = null;
        private String namespace = null;
        private List<String> readPermissions = new ArrayList<>();
        private List<String> publishPermissions = new ArrayList<String>();
        private DefaultAudience defaultAudience = DefaultAudience.FRIENDS;
        private LoginBehavior loginBehavior = LoginBehavior.SSO_WITH_FALLBACK;
        private boolean allAtOnce = false;
        private boolean useAppsecretProof = false;
        private String appSecret = null;
        private String graphVersion = ServerProtocol.getAPIVersion();
        private OnLoginListener onLoginListener;
        private OnLogoutListener onLogoutListener;
        private Activity activity;

        public Builder() {
        }

        public Builder appId(String appId) {
            this.appId = appId;
            return this;
        }

        public Builder namespace(String namespace) {
            this.namespace = namespace;
            return this;
        }

        public Builder permissions(Permission[] permissions) {
            for (Permission permission : permissions) {
                switch (permission.getType()) {
                    case READ:
                        readPermissions.add(permission.getValue());
                        break;
                    case PUBLISH:
                        publishPermissions.add(permission.getValue());
                        break;
                    default:
                        break;
                }
            }

            return this;
        }


        public Builder defaultAudience(DefaultAudience defaultAudience) {
            this.defaultAudience = defaultAudience;
            return this;
        }


        public Builder loginBehavior(LoginBehavior loginBehavior) {
            this.loginBehavior = loginBehavior;
            return this;
        }

        public Builder askForAllPermissionsAtOnce(boolean allAtOnce) {
            this.allAtOnce = allAtOnce;
            return this;
        }

        public Builder useAppsecretProof(boolean use) {
            useAppsecretProof = use;
            return this;
        }


        public Builder appSecret(String appSecret) {
            this.appSecret = appSecret;
            return this;
        }

        public Builder graphVersion(String graphVersion) {
            this.graphVersion = graphVersion;
            return this;
        }

        public Builder onLoginListener(OnLoginListener onLoginListener){
            this.onLoginListener = onLoginListener;
            return this;
        }

        public Builder onLogoutListener(OnLogoutListener onLogoutListener){
            this.onLogoutListener = onLogoutListener;
            return this;
        }

        public Builder activity(Activity activity){
            this.activity = activity;
            return this;
        }

        public AnterosFacebookConfiguration build() {
            return new AnterosFacebookConfiguration(this);
        }

    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[ ").append("appId:").append(appId).append(", ").append("namespace:").append(namespace).append(", ").append("defaultAudience:")
                .append(", ").append("loginBehavior:").append(", ").append("readPermissions:").append(readPermissions.toString()).append(", ")
                .append("publishPermissions:").append(publishPermissions.toString()).append(" ]");
        return stringBuilder.toString();
    }

}
