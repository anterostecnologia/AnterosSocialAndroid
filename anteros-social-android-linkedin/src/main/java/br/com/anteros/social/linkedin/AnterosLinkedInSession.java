/*
 * ******************************************************************************
 *  * Copyright 2016 Anteros Tecnologia
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package br.com.anteros.social.linkedin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISession;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import br.com.anteros.social.core.AnterosSocialSession;
import br.com.anteros.social.core.OnLoginListener;
import br.com.anteros.social.core.OnLogoutListener;
import br.com.anteros.social.core.OnProfileListener;
import br.com.anteros.social.core.image.ImageRequest;
import br.com.anteros.social.linkedin.entities.LinkedInProfile;


/**
 * Created by edson on 10/04/16.
 */
public class AnterosLinkedInSession implements AnterosSocialSession {

    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String MAIDEN_NAME ="maiden-name";
    public static final String PROFILE_URL = "public-profile-url";
    public static final String PICTURE_URL = "picture-url";
    public static final String EMAIL_ADDRESS = "email-address";

    private WeakReference<OnLoginListener> onLoginListener;
    private WeakReference<OnLogoutListener> onLogoutListener;
    private final static String TAG = AnterosLinkedInSession.class.getName();
    private WeakReference<Activity> activity;

    private static final String host = "api.linkedin.com";
    private static final String topCardUrl = "https://" + host + "/v1/people/~:(first-name,last-name,email-address,public-profile-url,picture-url,industry,maiden-name)";
    private static final String shareUrl = "https://" + host + "/v1/people/~/shares";


    public AnterosLinkedInSession(AnterosLinkedInConfiguration configuration) {
        setActivity(configuration.getActivity());
        this.onLoginListener = new WeakReference<>(configuration.getOnLoginListener());
        this.onLogoutListener = new WeakReference<>(configuration.getOnLogoutListener());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LISessionManager.getInstance(getActivity().getApplicationContext()).onActivityResult(getActivity(), requestCode, resultCode, data);
    }

    @Override
    public void login() {
        LISessionManager.getInstance(getActivity().getApplicationContext()).init(getActivity(), buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                onLoginListener.get().onLogin();
            }
            @Override
            public void onAuthError(LIAuthError error) {
                onLoginListener.get().onFail(new AnterosLinkedInException(error.toString()));
            }
        }, true);
    }

    private Scope buildScope() {
        return Scope.build(Scope.W_SHARE, Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }


    @Override
    public void logout() {
        LISessionManager.getInstance(getActivity().getApplicationContext()).clearSession();
        onLogoutListener.get().onLogout();
    }

    @Override
    public boolean isLogged() {
        LISession session = LISessionManager.getInstance(getActivity().getApplicationContext()).getSession();
        return session.isValid();
    }

    @Override
    public void setActivity(Activity activity) {
        this.activity = new WeakReference<Activity>(activity);
    }

    @Override
    public void silentLogin() {
          login();
    }

    @Override
    public Activity getActivity() {
        return activity.get();
    }

    @Override
    public void getProfile(final OnProfileListener onProfileListener) {
        APIHelper apiHelper = APIHelper.getInstance(getActivity().getApplicationContext());
        apiHelper.getRequest(getActivity(), topCardUrl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse response) {
                Log.d(AnterosLinkedInSession.class.getName(),response.toString());
                JSONObject jsonResponse = response.getResponseDataAsJson();
                final OnProfileListener listener = onProfileListener;
                try {
                    final LinkedInProfile profile = parseResponse(jsonResponse);
                    try {
                        ImageRequest request = new ImageRequest(profile.getImageUrl(), new ImageRequest.ImageRequestCallback() {
                            @Override
                            public void onImageRequestStarted(ImageRequest request) {

                            }

                            @Override
                            public void onImageRequestFailed(ImageRequest request, Throwable throwable) {

                            }

                            @Override
                            public void onImageRequestEnded(ImageRequest request, Bitmap image) {
                                profile.setImageBitmap(image);
                                listener.onComplete(profile);
                            }

                            @Override
                            public void onImageRequestCancelled(ImageRequest request) {

                            }
                        });
                        request.load(activity.get().getApplicationContext());
                    } catch (Exception e) {
                        listener.onFail(e);
                    }
                } catch (JSONException e) {
                    onProfileListener.onFail(e);
                }
            }

            @Override
            public void onApiError(LIApiError error) {
               onProfileListener.onFail(new AnterosLinkedInException(error.toString()));
            }
        });
    }

    private LinkedInProfile parseResponse(JSONObject jsonResponse) throws JSONException {
        LinkedInProfile profile = new LinkedInProfile();
        if(jsonResponse.has(FIRST_NAME)) {
            profile.setFirstName(jsonResponse.getString(FIRST_NAME));
        }
        if(jsonResponse.has(MAIDEN_NAME)) {
            profile.setMiddleName(jsonResponse.getString(MAIDEN_NAME));
        }
        if(jsonResponse.has(LAST_NAME)) {
            profile.setLastName(jsonResponse.getString(LAST_NAME));
        }
        if(jsonResponse.has(PICTURE_URL)) {
            profile.setImage(jsonResponse.getString(PICTURE_URL));
        }
        if(jsonResponse.has(PROFILE_URL)) {
            profile.setLink(jsonResponse.getString(PROFILE_URL));
        }
        if(jsonResponse.has(EMAIL_ADDRESS)) {
            profile.setEmail(jsonResponse.getString(EMAIL_ADDRESS));
        }

        return profile;
    }

    @Override
    public void revoke() {
        logout();
    }

    @Override
    public void setOnLoginListener(OnLoginListener onLoginListener) {
        this.onLoginListener = new WeakReference<>(onLoginListener);
    }

    @Override
    public void setOnLogoutListener(OnLogoutListener onLogoutListener) {
        this.onLogoutListener = new WeakReference<>(onLogoutListener);
    }

    @Override
    public void checkListeners() {
        if (onLoginListener ==null) {
            throw new AnterosLinkedInException("Listener OnLoginLinkedInListener não foi definido.");
        }
        if (onLogoutListener ==null) {
            throw new AnterosLinkedInException("Listener OnLogoutLinkedInListener não foi definido.");
        }
    }
}
