package br.com.anteros.social.linkedin;

import android.app.Activity;

import java.lang.ref.WeakReference;

import br.com.anteros.social.core.AnterosSocialConfiguration;
import br.com.anteros.social.core.OnLoginListener;
import br.com.anteros.social.core.OnLogoutListener;
import br.com.anteros.social.core.SocialNetworkType;

/**
 * Created by edson on 10/04/16.
 */
public class AnterosLinkedInConfiguration implements AnterosSocialConfiguration {

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

    private AnterosLinkedInConfiguration(Builder builder) {
        this.onLoginListener = new WeakReference<>(builder.onLoginListener);
        this.onLogoutListener = new WeakReference<>(builder.onLogoutListener);
        this.activity = new WeakReference<>(builder.activity);
    }

    @Override
    public SocialNetworkType getSocialNetworkType() {
        return SocialNetworkType.LINKEDIN;
    }


    public static class Builder {

        private OnLoginListener onLoginListener;
        private OnLogoutListener onLogoutListener;
        private Activity activity;


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


        public AnterosLinkedInConfiguration build() {
            return new AnterosLinkedInConfiguration(this);
        }
    }
}
