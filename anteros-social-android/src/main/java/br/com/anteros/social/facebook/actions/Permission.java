package br.com.anteros.social.facebook.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.anteros.social.facebook.entities.FacebookProfile;

/**
 * Created by edson on 24/03/16.
 */
public enum Permission {

    @Deprecated
    PUBLIC_PROFILE("public_profile", Type.READ),

    USER_ABOUT_ME("user_about_me", Type.READ),

    USER_BIRTHDAY("user_birthday", Type.READ),

    USER_STATUS("user_status", Type.READ),

    EMAIL("email", Type.READ);

    public static enum Type {
        PUBLISH,
        READ;
    };

    public static enum Role {

        ADMINISTER,
        EDIT_PROFILE,
        CREATE_CONTENT,
        MODERATE_CONTENT,
        CREATE_ADS,
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
