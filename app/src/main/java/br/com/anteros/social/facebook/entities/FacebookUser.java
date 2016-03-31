package br.com.anteros.social.facebook.entities;

/**
 * Created by edson on 23/03/16.
 */
public class FacebookUser extends FacebookIdName {

    public FacebookUser() {
    }

    public FacebookUser(String id, String name) {
        mId = id;
        mName = name;
    }

    public FacebookUser(FacebookIdName idName) {
        mId = idName.mId;
        mName = idName.mName;
    }
}