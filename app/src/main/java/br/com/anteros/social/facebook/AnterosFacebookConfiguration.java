package br.com.anteros.social.facebook;

import com.facebook.internal.ServerProtocol;
import com.facebook.login.DefaultAudience;
import com.facebook.login.LoginBehavior;

import java.util.ArrayList;
import java.util.List;

import br.com.anteros.social.facebook.actions.Permission;

/**
 * Created by edson on 23/03/16.
 */
public class AnterosFacebookConfiguration {

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

        if (this.publishPermissions.size() > 0) {
            this.hasPublishPermissions = true;
        }
    }

    /**
     * Get facebook application id
     */
    public String getAppId() {
        return appId;
    }

    /**
     * Get application namespace
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Get read permissions
     */
    public List<String> getReadPermissions() {
        return readPermissions;
    }

    /**
     * Get publish permissions
     */
    public List<String> getPublishPermissions() {
        return publishPermissions;
    }

    /**
     * Get graph version
     */
    public String getGraphVersion() {
        return graphVersion;
    }

    /**
     * Return <code>True</code> if 'PUBLISH' permissions are defined
     */
    boolean hasPublishPermissions() {
        return hasPublishPermissions;
    }

    /**
     * Get session login behavior
     *
     * @return
     */
    LoginBehavior getLoginBehavior() {
        return loginBehavior;
    }

    /**
     * Get session default audience
     *
     * @return
     */
    DefaultAudience getDefaultAudience() {
        return defaultAudience;
    }

    /**
     * Return <code>True</code> if appsecret_proof should be passed with graph
     * api calls, otherwise return <code>False</code>
     *
     * @return The app secret proof
     * // @see https://developers.facebook.com/docs/graph-api/securing-requests
     */
    public boolean useAppsecretProof() {
        return useAppsecretProof;
    }

    /**
     * Get the app secret
     *
     * @return The app secret
     */
    public String getAppSecret() {
        return appSecret;
    }

    /**
     * Return <code>True</code> if all permissions - read and publish should be
     * asked one after another in the same time after logging in.
     */
    boolean isAllPermissionsAtOnce() {
        return allAtOnce;
    }

    /**
     * @param permissions
     * @return 0 - no new permissions, 1 - added only read, 2 - added only write, 3 - added both read and write
     * */
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

        public Builder() {
        }

        /**
         * Set facebook App Id. <br>
         * The application id is located in the dashboard of the app in admin
         * panel of facebook
         *
         * @param appId
         */
        public Builder setAppId(String appId) {
            this.appId = appId;
            return this;
        }

        /**
         * Set application namespace
         *
         * @param namespace
         * @return
         */
        public Builder setNamespace(String namespace) {
            this.namespace = namespace;
            return this;
        }

        /**
         * Set the array of permissions you want to use in your application
         *
         * @param permissions
         */
        public Builder setPermissions(Permission[] permissions) {
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

        /**
         * @param defaultAudience
         *            The defaultAudience to set.
         * @see DefaultAudience
         */
        public Builder setDefaultAudience(DefaultAudience defaultAudience) {
            this.defaultAudience = defaultAudience;
            return this;
        }

        /**
         * @param loginBehavior
         *            The loginBehavior to set.
         * @see LoginBehavior
         */
        public Builder setLoginBehavior(LoginBehavior loginBehavior) {
            this.loginBehavior = loginBehavior;
            return this;
        }

        /**
         * If your app has both: read and publish permissions, then this
         * configuration can be very useful. When you first time login, the popup
         * with read permissions that the user should accept appears. After
         * this you can decide, if you want the dialog of publish permissions to
         * appear or not. <br>
         * <br>
         * <b>Note:</b>Facebook requests not to ask the user for read and then
         * publish permissions at once, thus the default value will be
         * <code>false</code> for this flag.
         *
         * @param allAtOnce
         * @return {@link br.com.anteros.social.image.facebook.AnterosFacebookConfiguration.Builder}
         */
        public Builder setAskForAllPermissionsAtOnce(boolean allAtOnce) {
            this.allAtOnce = allAtOnce;
            return this;
        }

        /**
         * Set <code>True</code> if appsecret_proof should be passed with graph
         * api calls, otherwise set <code>False</code>. <b>Set app secret</b>
         * {@link #setAppSecret(String)} to be able to use this feature.<br>
         * <br>
         * The default value is <code>False</code>
         */
        public Builder useAppsecretProof(boolean use) {
            useAppsecretProof = use;
            return this;
        }

        /**
         * Set the app secret string. The app secret is shown in your app
         * dashboard settings. <br>
         * <b>It is highly suggested not to save this string hard coded in your
         * app</b>
         *
         * @param appSecret
         */
        public Builder setAppSecret(String appSecret) {
            this.appSecret = appSecret;
            return this;
        }

        /**
         * Set graph version if you want to use older versions.
         * The format should be v{X.X} for example: v2.3, v2.4
         */
        public Builder setGraphVersion(String graphVersion) {
            this.graphVersion = graphVersion;
            return this;
        }

        /**
         * Build the configuration for storage tool.
         *
         * @return
         */
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
