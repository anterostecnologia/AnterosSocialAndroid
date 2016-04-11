package br.com.anteros.social.twitter;

import android.app.Activity;

import java.lang.ref.WeakReference;

import br.com.anteros.social.AnterosSocialConfiguration;
import br.com.anteros.social.OnLoginListener;
import br.com.anteros.social.OnLogoutListener;
import br.com.anteros.social.SocialNetworkType;

/**
 * Created by edson on 25/03/16.
 */
public class AnterosTwitterConfiguration implements AnterosSocialConfiguration {


    private WeakReference<OnLoginListener> onLoginListener;
    private WeakReference<OnLogoutListener> onLogoutListener;
    private WeakReference<Activity> activity;
    private String twitterkey;

    public String getTwitterkey() {
        return twitterkey;
    }

    public String getTwitterSecret() {
        return twitterSecret;
    }

    private String twitterSecret;


    public OnLoginListener getOnLoginListener() {
        return onLoginListener.get();
    }

    public OnLogoutListener getOnLogoutListener() {
        return onLogoutListener.get();
    }

    public Activity getActivity() {
        return activity.get();
    }

    private AnterosTwitterConfiguration(Builder builder) {
        this.onLoginListener = new WeakReference<>(builder.onLoginListener);
        this.onLogoutListener = new WeakReference<>(builder.onLogoutListener);
        this.activity = new WeakReference<>(builder.activity);
        this.twitterkey = builder.twitterkey;
        this.twitterSecret = builder.twitterSecret;
    }

    @Override
    public SocialNetworkType getSocialNetworkType() {
        return SocialNetworkType.TWITTER;
    }


    public static class Builder {

        private OnLoginListener onLoginListener;
        private OnLogoutListener onLogoutListener;
        private Activity activity;
        private String twitterkey;
        private String twitterSecret;


        public Builder() {
        }

        public Builder onLoginListener(OnLoginListener onLoginListener) {
            this.onLoginListener = onLoginListener;
            return this;
        }

        public Builder onLogoutListener(OnLogoutListener onLogoutListener) {
            this.onLogoutListener = onLogoutListener;
            return this;
        }

        public Builder activity(Activity activity) {
            this.activity = activity;
            return this;
        }

        public Builder twitterkey(String twitterkey) {
            this.twitterkey = twitterkey;
            return this;
        }

        public Builder twitterSecret(String twitterSecret) {
            this.twitterSecret = twitterSecret;
            return this;
        }


        public AnterosTwitterConfiguration build() {
            return new AnterosTwitterConfiguration(this);
        }
    }
}
