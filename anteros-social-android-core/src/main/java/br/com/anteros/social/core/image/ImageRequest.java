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

package br.com.anteros.social.core.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.concurrent.Future;


public class ImageRequest {


    public static interface ImageRequestCallback {
        void onImageRequestStarted(ImageRequest request);

        void onImageRequestFailed(ImageRequest request, Throwable throwable);

        void onImageRequestEnded(ImageRequest request, Bitmap image);

        void onImageRequestCancelled(ImageRequest request);
    }

    private static ImageLoader imageLoader;

    private Future<?> future;
    private String url;
    private ImageRequestCallback callback;
    private ImageProcessor bitmapProcessor;
    private BitmapFactory.Options options;

    public ImageRequest(String url, ImageRequestCallback callback) {
        this(url, callback, null);
    }

    public ImageRequest(String url, ImageRequestCallback callback, ImageProcessor bitmapProcessor) {
        this(url, callback, bitmapProcessor, null);
    }

    public ImageRequest(String url, ImageRequestCallback callback, ImageProcessor bitmapProcessor, BitmapFactory.Options options) {
        this.url = url;
        this.callback = callback;
        this.bitmapProcessor = bitmapProcessor;
        this.options = options;
    }

    public void setImageRequestCallback(ImageRequestCallback callback) {
        this.callback = callback;
    }

    public String getUrl() {
        return url;
    }

    public void load(Context context) {
        if (future == null) {
            if (imageLoader == null) {
                imageLoader = new ImageLoader(context);
            }
            future = imageLoader.loadImage(url, new InnerCallback(), bitmapProcessor, options);
        }
    }

    public void cancel() {
        if (!isCancelled()) {
            future.cancel(false);
            if (callback != null) {
                callback.onImageRequestCancelled(this);
            }
        }
    }

    public final boolean isCancelled() {
        return future.isCancelled();
    }

    private class InnerCallback implements ImageLoader.ImageLoaderCallback {

        public void onImageLoadingStarted(ImageLoader loader) {
            if (callback != null) {
                callback.onImageRequestStarted(ImageRequest.this);
            }
        }

        public void onImageLoadingEnded(ImageLoader loader, Bitmap bitmap) {
            if (callback != null && !isCancelled()) {
                callback.onImageRequestEnded(ImageRequest.this, bitmap);
            }
            future = null;
        }

        public void onImageLoadingFailed(ImageLoader loader, Throwable exception) {
            if (callback != null && !isCancelled()) {
                callback.onImageRequestFailed(ImageRequest.this, exception);
            }
            future = null;
        }
    }

}
