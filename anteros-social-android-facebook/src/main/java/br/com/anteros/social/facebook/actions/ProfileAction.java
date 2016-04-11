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

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.facebook.GraphResponse;

import br.com.anteros.social.core.SocialProfile;
import br.com.anteros.social.facebook.AnterosFacebookSession;
import br.com.anteros.social.facebook.entities.FacebookProfile;
import br.com.anteros.social.facebook.listeners.OnActionFacebookListener;
import br.com.anteros.social.facebook.utils.JsonUtils;
import br.com.anteros.social.core.image.ImageRequest;

/**
 * Created by edson on 23/03/16.
 */
public class ProfileAction extends FacebookAction<SocialProfile> {

    private FacebookProfile.Properties mProperties;

    public ProfileAction(AnterosFacebookSession sessionManager) {
        super(sessionManager);
    }

    public void setProperties(FacebookProfile.Properties properties) {
        mProperties = properties;
    }

    @Override
    protected String getGraphPath() {
        return getTarget();
    }

    @Override
    protected Bundle getBundle() {
        if (mProperties != null) {
            return mProperties.getBundle();
        }
        return null;
    }

    @Override
    protected void processAndReturnResponse(OnActionFacebookListener<SocialProfile> actionListener, GraphResponse response) {
        final OnActionFacebookListener<SocialProfile> listener = actionListener;
        Log.i(ProfileAction.class.getName(), response.getRawResponse());
        final FacebookProfile profile = JsonUtils.fromJson(response.getRawResponse(), FacebookProfile.class);
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
            request.load(sessionManager.getActivity().getApplicationContext());
        } catch (Exception e) {
            listener.onFail(e);
        }
    }

}