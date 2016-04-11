package br.com.anteros.social.core;

import android.graphics.Bitmap;

/**
 * Created by edson on 03/04/16.
 */
public interface SocialProfile {

    public String getId();

    public String getUserName();

    public String getFirstName();

    public String getMiddleName();

    public String getLastName();

    public String getFullName();

    public String getGender();

    public String getLink();

    public AgeRange getAgeRange();

    public String getBirthday();

    public String getEmail();

    public String getImageUrl();

    public Bitmap getImageBitmap();

    public SocialNetworkType getProfileType();

    public String getProfileName();
}