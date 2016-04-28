package br.com.anteros.social.core.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;

import br.com.anteros.social.core.R;
import br.com.anteros.social.core.component.tipo.AnterosSocialButtonStyle;
import br.com.anteros.social.core.component.tipo.SocialNetwork;

/**
 * @author Eduardo Albertini (albertinieduardo@hotmail.com)
 */
public class AnterosSocialButton extends Button {


    private boolean mShowText;
    private int mSocialType = -1;
    private SocialNetwork mSocialNetwork;
    private int mButtonStyleType;
    private AnterosSocialButtonStyle mButtonStyle;

    public AnterosSocialButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AnterosSocialButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public AnterosSocialButton(Context context, SocialNetwork socialNetwork, AnterosSocialButtonStyle buttonStyle, boolean showText) {
        super(context);

        this.mSocialNetwork = socialNetwork;
        this.mButtonStyle = buttonStyle;
        this.mShowText = showText;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);
        updateBackground();
    }

    void init(Context context, AttributeSet attributeSet) {
        TypedArray attr = context.obtainStyledAttributes(attributeSet, R.styleable.AnterosSocialButton, 0, 0);
        mShowText = attr.getBoolean(R.styleable.AnterosSocialButton_asb_showText, true);
        mSocialType = attr.getInt(R.styleable.AnterosSocialButton_asb_type, -1);
        mButtonStyleType = attr.getInt(R.styleable.AnterosSocialButton_asb_style, 0);

        attr.recycle();
        updateSocialNetworkType();
        updateButtonStyleType();
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

    private void updateBackground() {
        Drawable drawableLogotipo = getDrawable(mSocialNetwork.getLogotipo());
        Drawable drawableBackground = null;
        if (mButtonStyle == AnterosSocialButtonStyle.STYLE_NORMAL) {
            setTextColor(getColor(mSocialNetwork.getLightColor()));
            drawableLogotipo.mutate();
            drawableLogotipo.setColorFilter(getColor(mSocialNetwork.getLightColor()), PorterDuff.Mode.SRC_ATOP);

            drawableBackground = ShapeBuilder.buildSelectorShapeFromColors(
                    getColor(mSocialNetwork.getDefaultColor()),
                    getColor(mSocialNetwork.getDefaultColorPressed()), 0, getDimension(R.dimen.asb_stroke_radius));
            setBackgroundStyle(drawableBackground);

        } else {
            setTextColor(getColor(mSocialNetwork.getDefaultColor()));
            drawableLogotipo.mutate();
            drawableLogotipo.setColorFilter(getColor(mSocialNetwork.getDefaultColor()), PorterDuff.Mode.SRC_ATOP);

            drawableBackground = ShapeBuilder.buildSelectorShapeFromColors(
                    getColor(mSocialNetwork.getLightColor()),
                    getColor(mSocialNetwork.getLightColorPressed()), 0, getDimension(R.dimen.asb_stroke_radius));

            setBackgroundStyle(drawableBackground);
        }

        if (mShowText) {
            setText(mSocialNetwork.getName());
            setPadding((int) getDimension(R.dimen.asb_padding_left), 0, (int) getDimension(R.dimen.asb_padding_right), 0);
            drawableLogotipo.setBounds(0, 0, (int) getDimension(R.dimen.asb_bound), (int) getDimension(R.dimen.asb_bound));
            setCompoundDrawablePadding(5);
            setCompoundDrawables(drawableLogotipo, null, null, null);
        } else {
            setPadding(0, (int) getDimension(R.dimen.asb_padding_top), 0, (int) getDimension(R.dimen.asb_padding_bottom));
            drawableLogotipo.setBounds(0, 0, (int) getDimension(R.dimen.asb_bound_text), (int) getDimension(R.dimen.asb_bound_text));
            setTextSize(1);
            setCompoundDrawables(null, drawableLogotipo, null, null);
        }
    }

    int getColor(@ColorRes int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return getResources().getColor(id, null);
        return getResources().getColor(id);
    }

    Drawable getDrawable(@DrawableRes int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return getResources().getDrawable(id, null);
        return getResources().getDrawable(id);
    }

    float getDimension(@DimenRes int id) {
        return getResources().getDimension(id);
    }


    void setBackgroundStyle(Drawable background) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.JELLY_BEAN)
            setBackground(background);
        else
            setBackgroundDrawable(background);
    }

    private static class ShapeBuilder {
        public static Drawable generateSelectorFromDrawables(Drawable pressed, Drawable normal) {
            StateListDrawable states = new StateListDrawable();
            states.addState(new int[]{-android.R.attr.state_focused, -android.R.attr.state_pressed, -android.R.attr.state_selected}, normal);
            states.addState(new int[]{android.R.attr.state_pressed}, pressed);
            states.addState(new int[]{android.R.attr.state_focused}, pressed);
            states.addState(new int[]{android.R.attr.state_selected}, pressed);
            return states;
        }

        public static Drawable generateShape(int color, int stokeSize, float strokeRadius) {
            GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{color, color});
            drawable.setStroke(stokeSize, color);
            drawable.setCornerRadius(strokeRadius);
            return drawable;
        }

        public static Drawable buildSelectorShapeFromColors(int colorNormalStroke, int colorPressedStroke, int strokeSize, float strokeRadius) {
            Drawable pressed = generateShape(colorPressedStroke, strokeSize, strokeRadius);
            Drawable normal = generateShape(colorNormalStroke, strokeSize, strokeRadius);
            return generateSelectorFromDrawables(pressed, normal);
        }
    }
}
