package br.com.anteros.social.instagram;

import android.app.Activity;

import java.lang.ref.WeakReference;

import br.com.anteros.social.AnterosSocialConfiguration;
import br.com.anteros.social.OnLoginListener;
import br.com.anteros.social.OnLogoutListener;
import br.com.anteros.social.SocialNetworkType;

/**
 * Created by edson on 05/04/16.
 */
public class AnterosInstagramConfiguration implements AnterosSocialConfiguration {

    private WeakReference<OnLoginListener> onLoginListener;
    private WeakReference<OnLogoutListener> onLogoutListener;
    private WeakReference<Activity> activity;
    private String clientId;
    private String clientSecret;
    private String redirectURL;
    private String scope;

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public String getScope() {
        return scope;
    }

    public OnLoginListener getOnLoginListener() {
        return onLoginListener.get();
    }

    public OnLogoutListener getOnLogoutListener() {
        return onLogoutListener.get();
    }

    public Activity getActivity() {
        return activity.get();
    }

    private AnterosInstagramConfiguration(Builder builder) {
        this.onLoginListener = new WeakReference<>(builder.onLoginListener);
        this.onLogoutListener = new WeakReference<>(builder.onLogoutListener);
        this.activity = new WeakReference<>(builder.activity);
        this.clientId = builder.clientId;
        this.clientSecret = builder.clientSecret;
        this.scope = builder.scope;
        this.redirectURL = builder.redirectURL;
    }

    @Override
    public SocialNetworkType getSocialNetworkType() {
        return SocialNetworkType.INSTAGRAM;
    }


    public static class Builder {

        private OnLoginListener onLoginListener;
        private OnLogoutListener onLogoutListener;
        private Activity activity;
        private String clientId;
        private String clientSecret;
        private String redirectURL;
        private String scope;


        public Builder() {
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
        public Builder clientId(String clientId){
            this.clientId = clientId;
            return this;
        }

        public Builder clientSecret(String clientSecret){
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder redirectURL(String redirectURL){
            this.redirectURL = redirectURL;
            return this;
        }

        public Builder scope(String scope){
            this.scope = scope;
            return this;
        }


        public AnterosInstagramConfiguration build() {
            return new AnterosInstagramConfiguration(this);
        }

    }
}
