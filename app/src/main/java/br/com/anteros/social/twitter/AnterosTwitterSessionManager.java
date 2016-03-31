package br.com.anteros.social.twitter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

import java.lang.ref.WeakReference;
import java.util.StringTokenizer;

import br.com.anteros.social.image.ImageRequest;
import br.com.anteros.social.twitter.entities.TwitterAgeRange;
import br.com.anteros.social.twitter.entities.TwitterProfile;
import br.com.anteros.social.twitter.listeners.OnLoginTwitterListener;
import br.com.anteros.social.twitter.listeners.OnLogoutTwitterListener;
import br.com.anteros.social.twitter.listeners.OnProfileTwitterListener;

/**
 * Created by edson on 28/03/16.
 */
public class AnterosTwitterSessionManager {


    private final static String TAG = AnterosTwitterSessionManager.class.getName();
    private static final int RC_SIGN_IN = 9001;
    private static final int PROFILE_PIC_SIZE = 400;
    static AnterosTwitterConfiguration configuration;
    private WeakReference<FragmentActivity> activity;
    private TwitterAuthClient authTwitter;
    private WeakReference<OnLoginTwitterListener> onLoginListener;
    private WeakReference<OnLogoutTwitterListener> onLogoutListener;
    private TwitterSession twitterSession;


    public AnterosTwitterSessionManager(AnterosTwitterConfiguration configuration) {
        AnterosTwitterSessionManager.configuration = configuration;
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
                //If failure occurs while login handle it here
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

    /**
     * Login to Twitter Plus
     */
    public void login() {
        if (!isLogged()) {
            signIn();
        } else {
            onLoginListener.get().onLogin();
        }
    }


    /**
     * Logout from Twitter Plus
     */
    public void logout() {
        signOut();
    }

    /**
     * Indicate if you are logged in or not.
     *
     * @return <code>True</code> if you is logged in, otherwise return
     * <code>False</code>
     */
    public boolean isLogged() {
        return (twitterSession != null);
    }

    public void silentLogin() {
        login();
    }

    public Activity getActivity() {
        return activity.get();
    }

    void setActivity(FragmentActivity activity) {
        this.activity = new WeakReference<FragmentActivity>(activity);

    }

    public void getProfile(OnProfileTwitterListener onProfileTwitterListener) {
        if (!isLogged()) {
            throw new AnterosTwitterException("Sessão Twitter não conectada. Não é possível obter informações do perfil do usuário.");
        }
        final OnProfileTwitterListener callBack = onProfileTwitterListener;
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

    public void setOnLoginTwitterListener(OnLoginTwitterListener onLoginTwitterListener) {
        this.onLoginListener = new WeakReference<OnLoginTwitterListener>(onLoginTwitterListener);
    }

    public void setOnLogoutTwitterListener(OnLogoutTwitterListener onLogoutTwitterListener) {
        this.onLogoutListener = new WeakReference<OnLogoutTwitterListener>(onLogoutTwitterListener);
    }
}
