package br.com.anteros.social.facebook.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.anteros.social.facebook.entities.FacebookProfile;

/**
 * Created by edson on 24/03/16.
 */
public enum Permission {

    /**
     * This permission not longer in use in latest graph versions.
     * Please us Permission#USER_ABOUT_ME instead.
     *
     * Provides access to a subset of items that are part of a person's public
     * profile. These {@link FacebookProfile} fields can be retrieved by using this
     * permission:<br>
     * <ul>
     * <li>{@link FacebookProfile.Properties#ID ID}</li>
     * <li>{@link FacebookProfile.Properties#NAME NAME}</li>
     * <li>{@link FacebookProfile.Properties#FIRST_NAME FIRST_NAME}</li>
     * <li>{@link FacebookProfile.Properties#LAST_NAME LAST_NAME}</li>
     * <li>{@link FacebookProfile.Properties#LINK LINK}</li>
     * <li>{@link FacebookProfile.Properties#GENDER GENDER}</li>
     * <li>{@link FacebookProfile.Properties#AGE_RANGE AGE_RANGE}</li>
     * </ul>
     *
     */
    @Deprecated
    PUBLIC_PROFILE("public_profile", Type.READ),

    /**
     * Provides access to {@link FacebookProfile.Properties#FIRST_NAME FIRST_NAME} property of the
     * {@link FacebookProfile}
     */
    USER_ABOUT_ME("user_about_me", Type.READ),

    /**
     * Access the date and month of a person's birthday. This may or may not
     * include the person's year of birth, dependent upon their privacy settings
     * and the access token being used to query this field.
     */
    USER_BIRTHDAY("user_birthday", Type.READ),

    /**
     * Provides access to a person's statuses. These are posts on Facebook which
     * don't include links, videos or photos.
     */
    USER_STATUS("user_status", Type.READ),

    /**
     * Provides access to the person's primary email address via the
     * {@link FacebookProfile.Properties#EMAIL} property on the {@link FacebookProfile} object.<br>
     * <br>
     * <b>Note:</b><br>
     * Even if you request the email permission it is not guaranteed you will
     * get an email address. For example, if someone signed up for Facebook with
     * a phone number instead of an email address, the email field may be empty.
     */
    EMAIL("email", Type.READ);

    /**
     * Permission type enum: <li>READ</li> <li>PUBLISH</li><br>
     */
    public static enum Type {
        PUBLISH,
        READ;
    };

    public static enum Role {
        /**
         * Manage admins<br>
         * Full Admin
         */
        ADMINISTER,
        /**
         * Edit the Page and add apps<br>
         * Full Admin, Content Creator
         */
        EDIT_PROFILE,
        /**
         * Create posts as the Page<br>
         * Full Admin, Content Creator
         */
        CREATE_CONTENT,
        /**
         * Respond to and delete comments, send messages as the Page<br>
         * Full Admin, Content Creator, Moderator
         */
        MODERATE_CONTENT,
        /**
         * Create ads and unpublished page posts<br>
         * Full Admin, Content Creator, Moderator, Ads Creator
         */
        CREATE_ADS,
        /**
         * View Insights<br>
         * Full Admin, Content Creator, Moderator, Ads Creator, Insights Manager
         */
        BASIC_ADMIN
    }

    private final String mValue;
    private final Type mType;

    private Permission(String value, Type type) {
        mValue = value;
        mType = type;
    }

    public String getValue() {
        return mValue;
    }

    public Type getType() {
        return mType;
    }

    public static Permission fromValue(String permissionValue) {
        for (Permission permission : values()) {
            if (permission.mValue.equals(permissionValue)) {
                return permission;
            }
        }
        return null;
    }

    public static List<Permission> convert(Collection<String> rawPermissions) {
        if (rawPermissions == null) {
            return null;
        }

        List<Permission> permissions = new ArrayList<>();
        for (Permission permission : values()) {
            if (rawPermissions.contains(permission.getValue())) {
                permissions.add(permission);
            }
        }
        return permissions;
    }

    public static List<String> convert(List<Permission> permissions) {
        if (permissions == null) {
            return null;
        }

        List<String> rawPermissions = new ArrayList<String>();
        for (Permission permission : permissions) {
            rawPermissions.add(permission.getValue());
        }

        return rawPermissions;
    }

    public static List<String> fetchPermissions(List<Permission> permissions, Permission.Type type) {
        List<String> perms = new ArrayList<String>();
        for (Permission permission : permissions) {
            if (type.equals(permission.getType())) {
                perms.add(permission.getValue());
            }
        }
        return perms;
    }

}
