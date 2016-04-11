package br.com.anteros.social.facebook.actions;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.facebook.GraphResponse;

import br.com.anteros.social.SocialProfile;
import br.com.anteros.social.facebook.AnterosFacebookSession;
import br.com.anteros.social.facebook.entities.FacebookProfile;
import br.com.anteros.social.facebook.listeners.OnActionFacebookListener;
import br.com.anteros.social.facebook.utils.JsonUtils;
import br.com.anteros.social.image.ImageRequest;

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