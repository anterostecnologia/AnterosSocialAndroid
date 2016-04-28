package br.com.anteros.social.core.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.widget.ImageButton;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import br.com.anteros.social.core.R;
import br.com.anteros.social.core.component.tipo.AnterosSocialButtonStyle;
import br.com.anteros.social.core.component.tipo.SocialNetwork;

/**
 * @author Eduardo Albertini (albertinieduardo@hotmail.com)
 */
public class AnterosSocialFloatingActionButton extends ImageButton {

    public static final int SIZE_NORMAL = 0;
    public static final int SIZE_MINI = 1;

    private int mSocialType = -1;
    private SocialNetwork mSocialNetwork;
    private int mButtonStyleType;
    private AnterosSocialButtonStyle mButtonStyle;
    private int mSize;
    private float mCircleSize;
    private float mShadowRadius;
    private float mShadowOffset;
    private int mDrawableSize;

    public AnterosSocialFloatingActionButton(Context context, SocialNetwork socialNetwork, AnterosSocialButtonStyle buttonStyle, @ASFAB_SIZE int buttonSize) {
        super(context);
        this.mSocialNetwork = socialNetwork;
        this.mButtonStyle = buttonStyle;
        this.mSize = buttonSize;

        updateCircleSize();
        mShadowRadius = getDimension(R.dimen.fab_shadow_radius);
        mShadowOffset = getDimension(R.dimen.fab_shadow_offset);
        updateDrawableSize();
        updateBackground();
    }
    public AnterosSocialFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AnterosSocialFloatingActionButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    void init(Context context, AttributeSet attributeSet) {
        TypedArray attr = context.obtainStyledAttributes(attributeSet, R.styleable.AnterosSocialFloatingActionButton, 0, 0);
        mSize = attr.getInt(R.styleable.AnterosSocialFloatingActionButton_asfab_size, SIZE_NORMAL);
        mSocialType = attr.getInt(R.styleable.AnterosSocialFloatingActionButton_asfab_type, -1);
        mButtonStyleType = attr.getInt(R.styleable.AnterosSocialFloatingActionButton_asfab_style, -1);
        attr.recycle();

        updateCircleSize();
        mShadowRadius = getDimension(R.dimen.fab_shadow_radius);
        mShadowOffset = getDimension(R.dimen.fab_shadow_offset);
        updateDrawableSize();
        updateButtonStyleType();
        updateSocialNetworkType();
        updateBackground();
    }

    private void updateButtonStyleType() {
        if (mButtonStyleType == AnterosSocialButtonStyle.STYLE_NORMAL.ordinal())
            mButtonStyle = AnterosSocialButtonStyle.STYLE_NORMAL;
        else if (mButtonStyleType == AnterosSocialButtonStyle.STYLE_LIGHT.ordinal())
            mButtonStyle = AnterosSocialButtonStyle.STYLE_LIGHT;
        else
            throw new IllegalArgumentException("Use AnterosSocialButtonStyle enum value only");
    }

    private void updateSocialNetworkType() {
        if (mSocialType == SocialNetwork.FACEBOOK.ordinal())
            mSocialNetwork = SocialNetwork.FACEBOOK;
        else if (mSocialType == SocialNetwork.GOOGLE.ordinal())
            mSocialNetwork = SocialNetwork.GOOGLE;
        else if (mSocialType == SocialNetwork.INSTAGRAM.ordinal())
            mSocialNetwork = SocialNetwork.INSTAGRAM;
        else if (mSocialType == SocialNetwork.LINKEDIN.ordinal())
            mSocialNetwork = SocialNetwork.LINKEDIN;
        else if (mSocialType == SocialNetwork.TWITTER.ordinal())
            mSocialNetwork = SocialNetwork.TWITTER;
        else if (mSocialType == SocialNetwork.YOUTUBE.ordinal())
            mSocialNetwork = SocialNetwork.YOUTUBE;
        else if (mSocialType == SocialNetwork.SNAPCHAT.ordinal())
            mSocialNetwork = SocialNetwork.SNAPCHAT;
        else
            throw new IllegalArgumentException("Use SocialNetwork enum value only");
    }

    private void updateDrawableSize() {
        mDrawableSize = (int) (mCircleSize + 2 * mShadowRadius);
    }

    private void updateCircleSize() {
        mCircleSize = getDimension(mSize == SIZE_NORMAL ? R.dimen.fab_size_normal : R.dimen.fab_size_mini);
    }

    int getColor(@ColorRes int id) {
        return getResources().getColor(id);
    }

    float getDimension(@DimenRes int id) {
        return getResources().getDimension(id);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mDrawableSize, mDrawableSize);
    }

    void updateBackground() {
        final float strokeWidth = getDimension(R.dimen.fab_stroke_width);
        final float halfStrokeWidth = strokeWidth / 2f;

        LayerDrawable layerDrawable = new LayerDrawable(
                new Drawable[]{
                        getResources().getDrawable(
                                mSize == SIZE_NORMAL ? br.com.anteros.android.ui.controls.R.drawable.fab_bg_normal : br.com.anteros.android.ui.controls.R.drawable.fab_bg_mini),
                        createFillDrawable(strokeWidth),
                        createOuterStrokeDrawable(strokeWidth),
                        getIconDrawable()
                });

        int iconOffset = (int) (mCircleSize - getDimension(R.dimen.fab_icon_size)) / 2;

        int circleInsetHorizontal = (int) (mShadowRadius);
        int circleInsetTop = (int) (mShadowRadius - mShadowOffset);
        int circleInsetBottom = (int) (mShadowRadius + mShadowOffset);

        layerDrawable.setLayerInset(1,
                circleInsetHorizontal,
                circleInsetTop,
                circleInsetHorizontal,
                circleInsetBottom);

        layerDrawable.setLayerInset(2,
                (int) (circleInsetHorizontal - halfStrokeWidth),
                (int) (circleInsetTop - halfStrokeWidth),
                (int) (circleInsetHorizontal - halfStrokeWidth),
                (int) (circleInsetBottom - halfStrokeWidth));

        layerDrawable.setLayerInset(3,
                circleInsetHorizontal + iconOffset,
                circleInsetTop + iconOffset,
                circleInsetHorizontal + iconOffset,
                circleInsetBottom + iconOffset);

        setBackgroundCompat(layerDrawable);
    }

    Drawable getIconDrawable() {
        Drawable drawableLogotipo = getResources().getDrawable(mSocialNetwork.getLogotipo());
        if (mButtonStyle == AnterosSocialButtonStyle.STYLE_NORMAL) {
            drawableLogotipo.mutate();
            drawableLogotipo.setColorFilter(getResources().getColor(mSocialNetwork.getLightColor()), PorterDuff.Mode.SRC_ATOP);
        } else {

            drawableLogotipo.mutate();
            drawableLogotipo.setColorFilter(getResources().getColor(mSocialNetwork.getDefaultColor()), PorterDuff.Mode.SRC_ATOP);
        }
        return drawableLogotipo;
    }

    private StateListDrawable createFillDrawable(float strokeWidth) {
        StateListDrawable drawable = new StateListDrawable();
        if (mButtonStyle == AnterosSocialButtonStyle.STYLE_NORMAL) {
            drawable.addState(new int[]{android.R.attr.state_pressed}, createCircleDrawable(getResources().getColor(mSocialNetwork.getDefaultColorPressed()), strokeWidth));
            drawable.addState(new int[]{}, createCircleDrawable(getResources().getColor(mSocialNetwork.getDefaultColor()), strokeWidth));
        } else {
            drawable.addState(new int[]{android.R.attr.state_pressed}, createCircleDrawable(getResources().getColor(mSocialNetwork.getLightColorPressed()), strokeWidth));
            drawable.addState(new int[]{}, createCircleDrawable(getResources().getColor(mSocialNetwork.getLightColor()), strokeWidth));
        }
        return drawable;
    }

    private Drawable createCircleDrawable(int color, float strokeWidth) {
        int alpha = Color.alpha(color);
        int opaqueColor = opaque(color);

        ShapeDrawable fillDrawable = new ShapeDrawable(new OvalShape());

        final Paint paint = fillDrawable.getPaint();
        paint.setAntiAlias(true);
        paint.setColor(opaqueColor);

        Drawable[] layers = {
                fillDrawable,
                createInnerStrokesDrawable(opaqueColor, strokeWidth)
        };

        LayerDrawable drawable = new TranslucentLayerDrawable(alpha, layers);
        int halfStrokeWidth = (int) (strokeWidth / 2f);
        drawable.setLayerInset(1, halfStrokeWidth, halfStrokeWidth, halfStrokeWidth, halfStrokeWidth);

        return drawable;
    }

    private Drawable createOuterStrokeDrawable(float strokeWidth) {
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());

        final Paint paint = shapeDrawable.getPaint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setAlpha(opacityToAlpha(0.02f));

        return shapeDrawable;
    }

    private int opacityToAlpha(float opacity) {
        return (int) (255f * opacity);
    }

    private int darkenColor(int argb) {
        return adjustColorBrightness(argb, 0.9f);
    }

    private int lightenColor(int argb) {
        return adjustColorBrightness(argb, 1.1f);
    }

    private int adjustColorBrightness(int argb, float factor) {
        float[] hsv = new float[3];
        Color.colorToHSV(argb, hsv);

        hsv[2] = Math.min(hsv[2] * factor, 1f);

        return Color.HSVToColor(Color.alpha(argb), hsv);
    }

    private int halfTransparent(int argb) {
        return Color.argb(
                Color.alpha(argb) / 2,
                Color.red(argb),
                Color.green(argb),
                Color.blue(argb)
        );
    }

    private int opaque(int argb) {
        return Color.rgb(
                Color.red(argb),
                Color.green(argb),
                Color.blue(argb)
        );
    }

    private Drawable createInnerStrokesDrawable(final int color, float strokeWidth) {
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());

        final int bottomStrokeColor = darkenColor(color);
        final int bottomStrokeColorHalfTransparent = halfTransparent(bottomStrokeColor);
        final int topStrokeColor = lightenColor(color);
        final int topStrokeColorHalfTransparent = halfTransparent(topStrokeColor);

        final Paint paint = shapeDrawable.getPaint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        shapeDrawable.setShaderFactory(new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                return new LinearGradient(width / 2, 0, width / 2, height,
                        new int[]{topStrokeColor, topStrokeColorHalfTransparent, color,
                                bottomStrokeColorHalfTransparent, bottomStrokeColor},
                        new float[]{0f, 0.2f, 0.5f, 0.8f, 1f},
                        Shader.TileMode.CLAMP
                );
            }
        });

        return shapeDrawable;
    }

    @SuppressLint("NewApi")
    private void setBackgroundCompat(Drawable drawable) {
        setBackgroundDrawable(drawable);
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SIZE_NORMAL, SIZE_MINI})
    public @interface ASFAB_SIZE {
    }

    private static class TranslucentLayerDrawable extends LayerDrawable {
        private final int mAlpha;

        public TranslucentLayerDrawable(int alpha, Drawable... layers) {
            super(layers);
            mAlpha = alpha;
        }

        @Override
        public void draw(Canvas canvas) {
            Rect bounds = getBounds();
            canvas.saveLayerAlpha(bounds.left, bounds.top, bounds.right, bounds.bottom, mAlpha, Canvas.ALL_SAVE_FLAG);
            super.draw(canvas);
            canvas.restore();
        }
    }
}