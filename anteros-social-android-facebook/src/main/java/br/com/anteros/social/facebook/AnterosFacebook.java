package br.com.anteros.social.facebook;

import android.app.Activity;
import android.content.Intent;

import com.facebook.FacebookSdk;

import br.com.anteros.social.core.AnterosSocialConfiguration;
import br.com.anteros.social.core.AnterosSocialNetwork;
import br.com.anteros.social.core.AnterosSocialSession;
import br.com.anteros.social.core.OnLoginListener;
import br.com.anteros.social.core.OnLogoutListener;
import br.com.anteros.social.core.OnProfileListener;
import br.com.anteros.social.core.SocialProfile;
import br.com.anteros.social.facebook.actions.ProfileAction;
import br.com.anteros.social.facebook.entities.FacebookProfile;
import br.com.anteros.social.facebook.listeners.OnProfileFacebookListener;
import br.com.anteros.social.facebook.utils.Attributes;
import br.com.anteros.social.facebook.utils.PictureAttributes;

/**
 * Created by edson on 23/03/16.
 */
public class AnterosFacebook implements AnterosSocialNetwork {

    private AnterosFacebookConfiguration configuration;
    private AnterosSocialSession session = null;

    private AnterosFacebook() {
    }


    public static void initialize(Activity activity) {
        FacebookSdk.sdkInitialize(activity.getApplicationContext());
    }

    public static AnterosFacebook create(Activity activity, OnLoginListener onLoginListener, OnLogoutListener onLogoutListener) {
        return new AnterosFacebook(activity, onLoginListener, onLogoutListener);
    }

    public static AnterosFacebook create(AnterosSocialConfiguration configuration) {
        return new AnterosFacebook(configuration);
    }

    private AnterosFacebook(Activity activity, OnLoginListener onLoginListener, OnLogoutListener onLogoutListener) {
        this(new AnterosFacebookConfiguration.Builder().onLoginListener(onLoginListener).onLogoutListener(onLogoutListener).activity(activity).build());
    }

    private AnterosFacebook(AnterosSocialConfiguration configuration) {
        this.configuration = (AnterosFacebookConfiguration) configuration;
        FacebookSdk.sdkInitialize(this.configuration.getActivity().getApplicationContext());
        session = new AnterosFacebookSession((AnterosFacebookConfiguration) configuration);
    }


    public void setOnLoginListener(OnLoginListener onLoginListener) {
        session.setOnLoginListener(onLoginListener);
    }

    public void setOnLogoutListener(OnLogoutListener onLogoutListener) {
        session.setOnLogoutListener(onLogoutListener);
    }


    public void silentLogin() {
        checkListeners();
        session.silentLogin();
    }

    private void checkListeners() {
        session.checkListeners();
    }


    public void login() {
        checkListeners();

        session.login();
    }


    public void logout() {
        checkListeners();
        session.logout();
    }

    public void revoke() {
        checkListeners();
        session.revoke();
    }

    public boolean isLogged() {
        return session.isLogged();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        session.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public AnterosSocialSession getSession() {
        return session;
    }

    @Override
    public void getProfile(OnProfileListener onProfileListener) {
        final OnProfileListener listener = onProfileListener;
        getProfile(new OnProfileFacebookListener() {
            @Override
            public void onComplete(SocialProfile response) {
                listener.onComplete(response);
            }

            @Override
            public void onFail(Throwable throwable) {
                listener.onFail(throwable);
            }

            @Override
            public void onThinking() {
                listener.onThinking();
            }
        });
    }


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


    public void getProfile(FacebookProfile.Properties properties, OnProfileFacebookListener onProfileFacebookListener) {
        getProfile(null, properties, onProfileFacebookListener);
    }

    public void getProfile(String profileId, FacebookProfile.Properties properties, OnProfileFacebookListener onProfileFacebookListener) {
        ProfileAction profileAction = new ProfileAction((AnterosFacebookSession) session);
        profileAction.setProperties(properties);
        profileAction.setTarget(profileId);
        profileAction.setActionListener(onProfileFacebookListener);
        profileAction.execute();
    }


}
