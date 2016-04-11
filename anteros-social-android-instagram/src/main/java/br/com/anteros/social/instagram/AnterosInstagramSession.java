package br.com.anteros.social.instagram;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import br.com.anteros.social.core.AnterosSocialConfiguration;
import br.com.anteros.social.core.AnterosSocialSession;
import br.com.anteros.social.core.OnLoginListener;
import br.com.anteros.social.core.OnLogoutListener;
import br.com.anteros.social.core.OnProfileListener;
import br.com.anteros.social.core.image.ImageRequest;
import br.com.anteros.social.instagram.entities.InstagramProfile;

/**
 * Created by edson on 05/04/16.
 */
public class AnterosInstagramSession implements AnterosSocialSession {

    public static final int ID = 7;
    private static final String SAVE_STATE_KEY_OAUTH_TOKEN = "InstagramSocialNetwork.SAVE_STATE_KEY_OAUTH_TOKEN";
    private static final String SAVE_STATE_KEY_OAUTH_REQUEST_TOKEN = "InstagramSocialNetwork.SAVE_STATE_KEY_OAUTH_SECRET";
    private static final int REQUEST_AUTH = UUID.randomUUID().hashCode() & 0xFFFF;
    private static final String INSTAGRAM_TOKENURL = "https://api.instagram.com/oauth/access_token";
    private static final String INSTAGRAM_APIURL = "https://api.instagram.com/v1";
    private static final String ERROR_CODE = "InstagramSocialNetwork.ERROR_CODE";
    public static final String PROFILE_PICTURE = "profile_picture";
    public static final String USERNAME = "username";
    public static final String FULL_NAME = "full_name";
    public static final String USER_ID = "id";
    public static final String SELF = "self";
    public static final String ERROR_TYPE = "error_type";
    public static final String CODE = "code";
    public static final String ERROR_MESSAGE = "error_message";

    private String authURLString;
    private String tokenURLString;
    private String clientId;
    private String clientSecret;
    private String redirectURL;
    private Bundle requestBundle;
    protected SharedPreferences sharedPreferences;
    private static final String SHARED_PREFERENCES_NAME = "anteros_social_android";
    public static final String REQUEST_LOGIN = "InstagramSocialNetwork.REQUEST_LOGIN";
    public static final String REQUEST_GET_PROFILE = "InstagramSocialNetwork.REQUEST_GET_PROFILE";
    protected Map<String, AnterosInstagramAsyncTask> mRequests = new HashMap<String, AnterosInstagramAsyncTask>();

    private WeakReference<OnLoginListener> onLoginListener;
    private WeakReference<OnLogoutListener> onLogoutListener;
    private final static String TAG = AnterosInstagramSession.class.getName();
    private AnterosSocialConfiguration configuration;
    private WeakReference<Activity> activity;
    private boolean restart = false;


    public AnterosInstagramSession(AnterosInstagramConfiguration configuration) {

        setActivity(configuration.getActivity());
        this.onLoginListener = new WeakReference<OnLoginListener>(configuration.getOnLoginListener());
        this.onLogoutListener = new WeakReference<OnLogoutListener>(configuration.getOnLogoutListener());
        this.clientId = configuration.getClientId();
        this.clientSecret = configuration.getClientSecret();
        this.redirectURL = configuration.getRedirectURL();
        this.activity = new WeakReference<Activity>(configuration.getActivity());
        sharedPreferences = activity.get().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);


        if (TextUtils.isEmpty(clientId) || TextUtils.isEmpty(clientSecret)) {
            throw new IllegalArgumentException("clientId and clientSecret are invalid");
        }
        String scope = configuration.getScope();
        if (scope == null) {
            scope = "basic";
        }
        String INSTAGRAM_AUTHURL = "https://api.instagram.com/oauth/authorize/";
        authURLString = INSTAGRAM_AUTHURL + "?client_id=" + clientId + "&redirect_uri="
                + redirectURL + "&response_type=code&display=touch&scope=" + scope;

        tokenURLString = INSTAGRAM_TOKENURL + "?client_id=" + clientId + "&client_secret="
                + clientSecret + "&redirect_uri=" + redirectURL + "&grant_type=authorization_code";
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int sanitizedRequestCode = requestCode & 0xFFFF;
        if (sanitizedRequestCode != REQUEST_AUTH) return;

        Uri uri = data != null ? data.getData() : null;

        if (uri != null && uri.toString().startsWith(redirectURL)) {
            String parts[] = uri.toString().split("=");
            String verifier = parts[1];
            RequestLoginAsyncTask requestLogin2AsyncTask = new RequestLoginAsyncTask();
            mRequests.put(REQUEST_LOGIN, requestLogin2AsyncTask);
            Bundle args = new Bundle();
            args.putString(RequestLoginAsyncTask.PARAM_VERIFIER, verifier);
            requestLogin2AsyncTask.execute(args);
        } else {
            onLoginListener.get().onFail(new AnterosInstagramException("Incorrect URI returned " + uri));
        }
    }


    public void login() {
        Intent intent = new Intent(activity.get(), AnterosOAuthActivity.class)
                .putExtra(AnterosOAuthActivity.PARAM_CALLBACK, redirectURL)
                .putExtra(AnterosOAuthActivity.PARAM_URL_TO_LOAD, authURLString);
        activity.get().startActivityForResult(intent, REQUEST_AUTH);
    }


    public void logout() {
        sharedPreferences.edit()
                .remove(SAVE_STATE_KEY_OAUTH_TOKEN)
                .remove(SAVE_STATE_KEY_OAUTH_REQUEST_TOKEN)
                .apply();
    }


    public boolean isLogged() {
        String accessToken = sharedPreferences.getString(SAVE_STATE_KEY_OAUTH_TOKEN, null);
        String requestToken = sharedPreferences.getString(SAVE_STATE_KEY_OAUTH_REQUEST_TOKEN, null);
        return accessToken != null && requestToken != null;
    }

    public void setActivity(Activity activity) {
        this.activity = new WeakReference<>(activity);
    }

    public Activity getActivity(){
        return this.activity.get();
    }

    public void silentLogin() {
         this.login();
    }

    public void revoke() {
        logout();
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
            throw new AnterosInstagramException("Listener OnLoginInstagramListener não foi definido.");
        }
        if (onLogoutListener ==null) {
            throw new AnterosInstagramException("Listener OnLogoutInstagramListener não foi definido.");
        }
    }

    public AccessToken getAccessToken() {
        return new AccessToken(
                sharedPreferences.getString(SAVE_STATE_KEY_OAUTH_TOKEN, null),
                null
        );
    }

    public void getProfile(OnProfileListener onProfileListener) {
        Bundle args = new Bundle();
        args.putString(RequestProfileAsyncTask.PARAM_USER_ID, SELF);
        new RequestProfileAsyncTask(onProfileListener).execute(args);
    }

    private InstagramProfile parseResponse(JSONObject jsonResponse) throws JSONException {
        InstagramProfile profile = new InstagramProfile();
        if(jsonResponse.has(USER_ID)) {
            profile.setId(jsonResponse.getString(USER_ID));
        }
        if(jsonResponse.has(USERNAME)) {
            profile.setUserName(jsonResponse.getString(USERNAME));
            profile.setLink("http://www.instagram.com/" + jsonResponse.getString(USERNAME));
        }
        if(jsonResponse.has(PROFILE_PICTURE)) {
            profile.setImage(jsonResponse.getString(PROFILE_PICTURE));
        }

        if(jsonResponse.has(FULL_NAME)) {
            StringTokenizer stok = new StringTokenizer(jsonResponse.getString(FULL_NAME));
            String firstName = stok.nextToken();

            StringBuilder middleName = new StringBuilder();
            String lastName = stok.nextToken();
            while (stok.hasMoreTokens()) {
                middleName.append(lastName + " ");
                lastName = stok.nextToken();
            }
            profile.setFirstName(firstName);
            profile.setMiddleName(middleName.toString());
            profile.setLastName(lastName);
        }

        return profile;
    }


    private String streamToString(InputStream is) {
        try {
            StringBuilder outString = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String read = reader.readLine();
            while (read != null) {
                outString.append(read);
                read = reader.readLine();
            }
            return outString.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String checkInputStream(HttpURLConnection connection) {
        String errorType = null, code = null, errorMessage = null;
        InputStream inputStream = connection.getErrorStream();
        String response = streamToString(inputStream);
        try {
            JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
            JSONObject jsonResponse = jsonObject.getJSONObject("meta");
            if (jsonResponse.has(ERROR_TYPE)) {
                errorType = jsonResponse.getString(ERROR_TYPE);
            }
            if (jsonResponse.has(CODE)) {
                code = jsonResponse.getString(CODE);
            }
            if (jsonResponse.has(ERROR_MESSAGE)) {
                errorMessage = jsonResponse.getString(ERROR_MESSAGE);
            }
            return "ERROR TYPE: " + errorType + " ERROR CODE: " + code + " ERROR MESSAGE: " + errorMessage;
        } catch (JSONException e) {
            return e.getMessage();
        }
    }

    private void checkConnectionErrors(HttpURLConnection connection) throws Exception {
        if (connection.getResponseCode() >= 400) {
            throw new Exception(checkInputStream(connection));
        }
    }

    private boolean checkTokenError(Bundle result) {
        if (result != null && result.containsKey(ERROR_CODE) && result.getString(ERROR_CODE).contains("400") && result.getString(ERROR_CODE).contains("OAuth")) {
            restart = true;
            requestBundle = result;
            requestBundle.remove(ERROR_CODE);
            requestBundle.remove(AnterosInstagramAsyncTask.RESULT_ERROR);
            login();
            return true;
        }
        return false;
    }

    private boolean checkRequests() {
        boolean queryRequests = false;
        for (String request : mRequests.keySet()) {
            if (request.equals(REQUEST_LOGIN)) {
                break;
            }
            queryRequests = true;
        }
        return queryRequests;
    }

    private void checkException(Exception e, Bundle result) {
        if (e.getMessage().contains("ERROR CODE") && e.getMessage().contains("OAuth")) {
            result.putString(ERROR_CODE, e.getMessage());
        } else {
            result.putString(AnterosInstagramAsyncTask.RESULT_ERROR, e.getMessage());
        }
    }

    private class RequestLoginAsyncTask extends AnterosInstagramAsyncTask {
        public static final String PARAM_VERIFIER = "LoginAsyncTask.PARAM_VERIFIER";

        private static final String RESULT_ACCESS_TOKEN = "LoginAsyncTask.RESULT_TOKEN";
        private static final String RESULT_REQUEST_TOKEN = "LoginAsyncTask.RESULT_SECRET";
        private static final String RESULT_USER_ID = "LoginAsyncTask.RESULT_USER_ID";
        public static final String POST = "POST";

        @Override
        protected Bundle doInBackground(Bundle... params) {
            String verifier = params[0].getString(PARAM_VERIFIER);

            Bundle result = new Bundle();
            try {
                URL url = new URL(tokenURLString);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod(POST);
                httpsURLConnection.setDoInput(true);
                httpsURLConnection.setDoOutput(true);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpsURLConnection.getOutputStream());
                outputStreamWriter.write("client_id=" + clientId +
                        "&client_secret=" + clientSecret +
                        "&grant_type=authorization_code" +
                        "&redirect_uri=" + redirectURL +
                        "&code=" + verifier);
                outputStreamWriter.flush();
                String response = streamToString(httpsURLConnection.getInputStream());
                JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();

                String accessToken = jsonObject.getString("access_token");
                String id = jsonObject.getJSONObject("user").getString("id");
                result.putString(RESULT_ACCESS_TOKEN, accessToken);
                result.putString(RESULT_REQUEST_TOKEN, verifier);
                result.putString(RESULT_USER_ID, id);
            } catch (Exception e) {
                result.putString(RESULT_ERROR, e.getMessage());
            }

            return result;
        }

        @Override
        protected void onPostExecute(Bundle result) {
            sharedPreferences.edit()
                    .putString(SAVE_STATE_KEY_OAUTH_TOKEN, result.getString(RESULT_ACCESS_TOKEN))
                    .putString(SAVE_STATE_KEY_OAUTH_REQUEST_TOKEN, result.getString(RESULT_REQUEST_TOKEN))
                    .apply();
            onLoginListener.get().onLogin();
        }
    }


    private class RequestProfileAsyncTask extends AnterosInstagramAsyncTask {
        public static final String PARAM_USER_ID = "RequestGetPersonAsyncTask.PARAM_USER_ID";
        private final OnProfileListener listener;

        public RequestProfileAsyncTask(OnProfileListener listener) {
            this.listener = listener;
        }

        @Override
        protected Bundle doInBackground(Bundle... params) {
            Bundle args = params[0];
            Bundle result = new Bundle(args);
            String userID = args.getString(PARAM_USER_ID);
            String token = sharedPreferences.getString(SAVE_STATE_KEY_OAUTH_TOKEN, null);

            String urlString = INSTAGRAM_APIURL + "/users/"+ userID +"/?access_token=" + token;

            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                checkConnectionErrors(connection);
                InputStream inputStream = connection.getInputStream();
                String response = streamToString(inputStream);
                JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
                JSONObject jsonResponse = jsonObject.getJSONObject("data");

                InstagramProfile instagramProfile = parseResponse(jsonResponse);
                result.putParcelable(REQUEST_GET_PROFILE, instagramProfile);
            } catch (Exception e) {
                checkException(e, result);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Bundle result) {
            if (checkTokenError(result)){return;}
            final InstagramProfile profile = result.getParcelable(REQUEST_GET_PROFILE);

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
        }
    }


}
