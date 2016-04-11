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

package br.com.anteros.social.facebook.actions;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Locale;

import br.com.anteros.social.facebook.AnterosFacebookException;
import br.com.anteros.social.facebook.AnterosFacebookSession;
import br.com.anteros.social.facebook.listeners.OnActionFacebookListener;
import br.com.anteros.social.facebook.utils.FacebookUtils;

/**
 * Created by edson on 23/03/16.
 */
public class FacebookAction<T> extends AbstractAction{

    private String target = "me";
    private String edge = null;
    private OnActionFacebookListener<T> onActionFacebookListener = null;
    private ActionCursor<T> cursor = null;

    private GraphRequest.Callback mCallback = new GraphRequest.Callback() {
        @Override
        public void onCompleted(GraphResponse response) {
            final OnActionFacebookListener<T> actionListener = getActionFacebookListener();
            FacebookRequestError error = response.getError();
            if (error != null) {
                Log.e(FacebookAction.class.getName(), "Failed to get what you have requested", error.getException());
                if (actionListener != null) {
                    actionListener.onFail(error.getException());
                }
            } else {
                if (response.getRawResponse() == null) {
                    Log.e(FacebookAction.class.getName(), "The response GraphObject has null value. Response=" + response.toString(), null);
                } else {
                    if (actionListener != null) {
                        T result = null;
                        try {
                            updateCursor(response);
                            processAndReturnResponse(actionListener, response);
                        } catch (Exception e) {
                            actionListener.onFail(e);
                            return;
                        }
                    }
                }
            }
        }
    };

    public FacebookAction(AnterosFacebookSession sessionManager) {
        super(sessionManager);
    }

    public void setEdge(String edge) {
        this.edge = edge;
    }

    public void setTarget(String target) {
        if (target != null) {
            this.target = target;
        }
    }

    public void setActionListener(OnActionFacebookListener<T> actionListener) {
        onActionFacebookListener = actionListener;
    }

    @Override
    protected void executeImpl() {
        OnActionFacebookListener<T> actionListener = getActionFacebookListener();
        if (sessionManager.isLogged()) {
            AccessToken accessToken = sessionManager.getAccessToken();
            Bundle bundle = updateAppSecretProof(getBundle());
            GraphRequest request = new GraphRequest(accessToken, getGraphPath(), bundle, HttpMethod.GET);
            request.setVersion(configuration.getGraphVersion());
            runRequest(request);
        } else {
            String reason = FacebookAction.getError(ErrorMsg.LOGIN);
            Log.e(getClass().getName(), reason, null);
            if (actionListener != null) {
                actionListener.onFail(new AnterosFacebookException(reason));
            }
        }
    }

    protected String getTarget() {
        return target;
    }

    protected String getGraphPath() {
        if (edge != null) {
            return target + "/" + edge;
        }
        return target;
    }

    protected Bundle getBundle() {
        return null;
    }

    protected OnActionFacebookListener<T> getActionFacebookListener() {
        return onActionFacebookListener;
    }

    protected void processAndReturnResponse(OnActionFacebookListener<T> actionListener, GraphResponse response) {
        Type type = onActionFacebookListener.getGenericType();
        if (type instanceof ParameterizedType) {
            Object arrayJson;
            try {
                arrayJson = response.getJSONObject().get("data");
            } catch (JSONException e) {
                return;
            }
            Log.i(FacebookAction.class.getName(),String.valueOf(arrayJson));
            T data = FacebookUtils.convert(String.valueOf(arrayJson), type);
            actionListener.onComplete(data);
            return;
        }
        T result = FacebookUtils.convert(response, type);
        actionListener.onComplete(result);
    }

    void runRequest(GraphRequest request) {
        OnActionFacebookListener<T> actionFacebookListener = getActionFacebookListener();
        request.setCallback(mCallback);
        GraphRequestAsyncTask task = new GraphRequestAsyncTask(request);
        task.execute();
        if (actionFacebookListener != null) {
            actionFacebookListener.onThinking();
        }
    }

    private void updateCursor(GraphResponse response) {
        if (onActionFacebookListener == null) {
            return;
        }

        if (cursor == null) {
            cursor = new ActionCursor<T>(FacebookAction.this);
        }

        GraphRequest requestNextPage = response.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
        if (requestNextPage != null) {
            requestNextPage.setCallback(mCallback);
        }
        cursor.setNextPage(requestNextPage);

        GraphRequest requestPrevPage = response.getRequestForPagedResults(GraphResponse.PagingDirection.PREVIOUS);
        if (requestPrevPage != null) {
            requestPrevPage.setCallback(mCallback);
        }
        cursor.setPrevPage(requestPrevPage);
        onActionFacebookListener.setCursor(cursor);
    }

    private Bundle updateAppSecretProof(Bundle bundle) {
        if (configuration.useAppsecretProof()) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putString("appsecret_proof", FacebookUtils.encode(configuration.getAppSecret(), sessionManager.getAccessToken().getToken()));
        }
        return bundle;
    }


    public static enum ErrorMsg {
        LOGIN("You are not logged in"),
        CANCEL_WEB_LOGIN("FacebookUser canceled the login web dialog"),
        PERMISSIONS_PUBLISH("Publish permission: '%s' wasn't set by SimpleFacebookConfiguration"),
        CANCEL_PERMISSIONS_PUBLISH("Publish permissions: '%s' weren't accepted by user");

        private String mMsg;

        private ErrorMsg(String msg) {
            mMsg = msg;
        }

        public String message() {
            return mMsg;
        }
    }

    public static String getError(ErrorMsg errorMsg, Object... params) {
        if (params == null) {
            return errorMsg.message();
        }
        else {
            return String.format(Locale.US, errorMsg.message(), params);
        }
    }

    public static String getError(ErrorMsg errorMsg) {
        return errorMsg.message();
    }
}
