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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;


public class MaskImageProcessor implements ImageProcessor {

    private static final int CUSTOM = 1;
    private static final int RECTANGLE = 2;

    private int mShape;
    private float mRadius;
    private float[] mRadiiArray;

    private Bitmap mMaskBitmap;

    private final Paint mMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path mPath = new Path();
    private final RectF mRect = new RectF();


    public MaskImageProcessor(float radius) {
        init();
        mShape = RECTANGLE;
        if (radius < 0) {
            radius = 0;
        }
        mRadius = radius;
    }


    public MaskImageProcessor(float[] radii) {
        init();
        mShape = RECTANGLE;
        mRadiiArray = radii;
        if (radii == null) {
            mRadius = 0;
        }
    }

    public MaskImageProcessor(Bitmap maskBitmap) {
        init();
        mShape = CUSTOM;
        mMaskBitmap = maskBitmap;
    }

    private void init() {
        mFillPaint.setColor(Color.RED);
        mMaskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    }

    public Bitmap processImage(Bitmap bitmap) {

        if (bitmap == null) {
            return null;
        }

        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();

        mRect.set(0, 0, width, height);

        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);

        switch (mShape) {
            case CUSTOM:
                canvas.drawBitmap(mMaskBitmap, 0, 0, mFillPaint);
                break;

            case RECTANGLE:
            default:
                if (mRadiiArray != null) {
                    mPath.reset();
                    mPath.addRoundRect(mRect, mRadiiArray, Path.Direction.CW);
                    canvas.drawPath(mPath, mFillPaint);
                } else {
                    float rad = mRadius;
                    float r = Math.min(width, height) * 0.5f;
                    if (rad > r) {
                        rad = r;
                    }
                    canvas.drawRoundRect(mRect, rad, rad, mFillPaint);
                }
                break;
        }

        canvas.drawBitmap(bitmap, 0, 0, mMaskPaint);

        return result;
    }
}
