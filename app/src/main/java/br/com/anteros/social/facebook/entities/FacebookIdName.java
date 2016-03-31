package br.com.anteros.social.facebook.entities;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by edson on 23/03/16.
 */
public class FacebookIdName {

    private static final String ID = "id";
    private static final String NAME = "name";

    @SerializedName(ID)
    protected String mId;

    @SerializedName(NAME)
    protected String mName;

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "id=%s,name=%s", mId, mName);
    }
}
