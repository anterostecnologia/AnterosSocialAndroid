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

package br.com.anteros.social.google;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.lang.ref.WeakReference;

import br.com.anteros.social.core.AnterosSocialConfiguration;
import br.com.anteros.social.core.AnterosSocialSession;
import br.com.anteros.social.core.OnLoginListener;
import br.com.anteros.social.core.OnLogoutListener;
import br.com.anteros.social.core.OnProfileListener;
import br.com.anteros.social.google.entities.GoogleAgeRange;
import br.com.anteros.social.google.entities.GoogleProfile;
import br.com.anteros.social.core.image.ImageRequest;

/**
 * Created by edson on 25/03/16.
 */
public class AnterosGoogleSession implements GoogleApiClient.OnConnectionFailedListener, AnterosSocialSession {


    private final static String TAG = AnterosGoogleSession.class.getName();
    private AnterosSocialConfiguration configuration;

    private WeakReference<Activity> activity;
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient clientGoogle;
    private WeakReference<OnLoginListener> onLoginListener;
    private WeakReference<OnLogoutListener> onLogoutListener;
    private GoogleSignInAccount account;


    public AnterosGoogleSession(AnterosGoogleConfiguration configuration) {
        this.configuration = configuration;
        this.onLoginListener = new WeakReference<OnLoginListener>(configuration.getOnLoginListener());
        this.onLogoutListener = new WeakReference<OnLogoutListener>(configuration.getOnLogoutListener());
        setActivity(configuration.getActivity());
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            account = result.getSignInAccount();
            onLoginListener.get().onLogin();
        } else {
            account=null;
            onLogoutListener.get().onLogout();
        }
    }

    private void signIn() {
        clientGoogle.connect();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(clientGoogle);
        activity.get().startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        if (clientGoogle.isConnected()) {
            Auth.GoogleSignInApi.signOut(clientGoogle).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            account = null;
                            onLogoutListener.get().onLogout();
                        }
                    });
        } else {
            account = null;
            onLogoutListener.get().onLogout();
        }
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(clientGoogle).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        account = null;
                        onLogoutListener.get().onLogout();
                    }
                });
    }

    public void login() {
        signIn();
    }


    public void logout() {
        signOut();
    }


    public boolean isLogged() {
        return (account!=null);
    }

    public void setActivity(Activity activity) {
        this.activity = new WeakReference<Activity>(activity);
        if (clientGoogle==null) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                    .build();

            clientGoogle = new GoogleApiClient.Builder(this.activity.get())
                    .enableAutoManage((FragmentActivity) this.activity.get() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Plus.API)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
    }

    public void silentLogin() {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(clientGoogle);
        if (opr.isDone()) {
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    public Activity getActivity() {
        return activity.get();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    public void getProfile(OnProfileListener onProfileGoogleListener) {
        if (!isLogged()) {
            throw new AnterosGoogleException("Sessão Google Plus não conectada. Não é possível obter informações do perfil do usuário.");
        }
        final OnProfileListener callBack = onProfileGoogleListener;
        callBack.onThinking();
        Plus.PeopleApi.load(clientGoogle, account.getId()).setResultCallback(new ResultCallback<People.LoadPeopleResult>() {
            @Override

            public void onResult(@NonNull People.LoadPeopleResult loadPeopleResult) {
                if (loadPeopleResult.getPersonBuffer()!=null && loadPeopleResult.getPersonBuffer().singleRefIterator().hasNext()) {
                    Person person = loadPeopleResult.getPersonBuffer().get(0);
                    final GoogleProfile profile = new GoogleProfile(person.getName().getGivenName(),
                            person.getName().getMiddleName(),
                            person.getName().getFamilyName(),
                            (person.getGender() == 0 ? "male" : "female"),
                            person.getBirthday(),
                            account.getEmail(),
                            person.getImage().getUrl(),
                            person.getUrl(),
                            new GoogleAgeRange(String.valueOf(person.getAgeRange().getMin()), String.valueOf(person.getAgeRange().getMax())));

                    try {
                        ImageRequest request = new ImageRequest(profile.getImage(), new ImageRequest.ImageRequestCallback() {
                            @Override
                            public void onImageRequestStarted(ImageRequest request) {

                            }

                            @Override
                            public void onImageRequestFailed(ImageRequest request, Throwable throwable) {

                            }

                            @Override
                            public void onImageRequestEnded(ImageRequest request, Bitmap image) {
                                profile.setImageBitmap(image);
                                callBack.onComplete(profile);
                            }

                            @Override
                            public void onImageRequestCancelled(ImageRequest request) {

                            }
                        });
                        request.load(activity.get().getApplicationContext());
                    } catch (Exception e) {
                        callBack.onFail(e);
                    }
                }
            }
        });

    }

    public void revoke() {
        revokeAccess();
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
            throw new AnterosGoogleException("Listener OnLoginGoogleListener não foi definido.");
        }
        if (onLogoutListener ==null) {
            throw new AnterosGoogleException("Listener OnLogoutGoogleListener não foi definido.");
        }
    }

}
