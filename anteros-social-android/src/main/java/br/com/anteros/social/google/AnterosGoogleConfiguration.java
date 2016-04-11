package br.com.anteros.social.google;

import android.app.Activity;

import java.lang.ref.WeakReference;

import br.com.anteros.social.AnterosSocialConfiguration;
import br.com.anteros.social.OnLoginListener;
import br.com.anteros.social.OnLogoutListener;
import br.com.anteros.social.SocialNetworkType;

/**
 * Created by edson on 25/03/16.
 */
public class AnterosGoogleConfiguration implements AnterosSocialConfiguration {

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

    private AnterosGoogleConfiguration(Builder builder) {
        this.onLoginListener = new WeakReference<>(builder.onLoginListener);
        this.onLogoutListener = new WeakReference<>(builder.onLogoutListener);
        this.activity = new WeakReference<>(builder.activity);
    }

    @Override
    public SocialNetworkType getSocialNetworkType() {
        return SocialNetworkType.GOOGLE;
    }


    public static class Builder {

        private OnLoginListener onLoginListener;
        private OnLogoutListener onLogoutListener;
        private Activity activity;


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


        public AnterosGoogleConfiguration build() {
            return new AnterosGoogleConfiguration(this);
        }

    }


}
