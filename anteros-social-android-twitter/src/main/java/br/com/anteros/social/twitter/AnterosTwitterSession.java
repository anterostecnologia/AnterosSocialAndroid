package br.com.anteros.social.twitter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

import java.lang.ref.WeakReference;
import java.util.StringTokenizer;

import br.com.anteros.social.core.AnterosSocialSession;
import br.com.anteros.social.core.OnLoginListener;
import br.com.anteros.social.core.OnLogoutListener;
import br.com.anteros.social.core.OnProfileListener;
import br.com.anteros.social.core.image.ImageRequest;
import br.com.anteros.social.twitter.entities.TwitterAgeRange;
import br.com.anteros.social.twitter.entities.TwitterProfile;
import io.fabric.sdk.android.Fabric;

/**
 * Created by edson on 28/03/16.
 */
public class AnterosTwitterSession implements AnterosSocialSession{


    private final static String TAG = AnterosTwitterSession.class.getName();
    private static final int RC_SIGN_IN = 9005;
    private static final int PROFILE_PIC_SIZE = 400;
    private AnterosTwitterConfiguration configuration;
    private WeakReference<Activity> activity;
    private TwitterAuthClient authTwitter;
    private WeakReference<OnLoginListener> onLoginListener;
    private WeakReference<OnLogoutListener> onLogoutListener;
    private TwitterSession twitterSession;


    public AnterosTwitterSession(AnterosTwitterConfiguration configuration) {
        this.configuration = configuration;
        this.onLoginListener = new WeakReference<OnLoginListener>(configuration.getOnLoginListener());
        this.onLogoutListener = new WeakReference<OnLogoutListener>(configuration.getOnLogoutListener());
        setActivity(configuration.getActivity());
        TwitterAuthConfig authConfig = new TwitterAuthConfig(configuration.getTwitterkey(), configuration.getTwitterSecret());
        Fabric.with(configuration.getActivity(), new Twitter(authConfig));
        authTwitter = new TwitterAuthClient();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == authTwitter.getRequestCode()) {
            authTwitter.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void signIn() {
        authTwitter.authorize(getActivity(), new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                twitterSession = result.data;
                onLoginListener.get().onLogin();
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
    }

    private void signOut() {
        Twitter.getSessionManager().clearActiveSession();
        Twitter.logOut();
        twitterSession = null;
        onLogoutListener.get().onLogout();
    }

    private void revokeAccess() {
        signOut();
    }

    public void login() {
        if (!isLogged()) {
            signIn();
        } else {
            onLoginListener.get().onLogin();
        }
    }


    public void logout() {
        signOut();
    }

    public boolean isLogged() {
        return (twitterSession != null);
    }

    public void silentLogin() {
        login();
    }

    public Activity getActivity() {
        return activity.get();
    }

    public void setActivity(Activity activity) {
        this.activity = new WeakReference<>(activity);

    }

    public void getProfile(OnProfileListener onProfileListener) {
        if (!isLogged()) {
            throw new AnterosTwitterException("Sessão Twitter não conectada. Não é possível obter informações do perfil do usuário.");
        }
        final OnProfileListener callBack = onProfileListener;
        callBack.onThinking();


        Twitter.getApiClient(twitterSession).getAccountService()
                .verifyCredentials(true, false, new Callback<User>() {
                    @Override
                    public void success(Result<User> userResult) {

                        User user = userResult.data;
                        StringTokenizer stok = new StringTokenizer(user.name);
                        String firstName = stok.nextToken();

                        StringBuilder middleName = new StringBuilder();
                        String lastName = stok.nextToken();
                        while (stok.hasMoreTokens()) {
                            middleName.append(lastName + " ");
                            lastName = stok.nextToken();
                        }


                        final TwitterProfile profile = new TwitterProfile(firstName,
                                middleName.toString(),
                                lastName,
                                "",
                                "",
                                user.email,
                                user.profileImageUrl,
                                "",
                                new TwitterAgeRange(null, null));


                        try {
                            ImageRequest request = new ImageRequest(user.profileImageUrl, new ImageRequest.ImageRequestCallback() {
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

                    @Override
                    public void failure(TwitterException e) {
                    }

                });

    }

    public void revoke() {
        revokeAccess();
    }

    public void setOnLoginListener(OnLoginListener onLoginTwitterListener) {
        this.onLoginListener = new WeakReference<OnLoginListener>(onLoginTwitterListener);
    }

    public void setOnLogoutListener(OnLogoutListener onLogoutTwitterListener) {
        this.onLogoutListener = new WeakReference<OnLogoutListener>(onLogoutTwitterListener);
    }

    @Override
    public void checkListeners() {
        if (onLoginListener ==null) {
            throw new AnterosTwitterException("Listener OnLoginTwitterListener não foi definido.");
        }
        if (onLogoutListener ==null) {
            throw new AnterosTwitterException("Listener OnLogoutTwitterListener não foi definido.");
        }
    }
}
