package br.com.anteros.social.facebook.listeners;


import java.util.List;

import br.com.anteros.social.facebook.actions.Permission;

/**
 * Created by edson on 23/03/16.
 */
public interface OnNewFacebookPermissionsListener extends OnErrorFacebookListener {

    /**
     * If the permission was granted, this callback is invoked.
     */
    public abstract void onSuccess(String accessToken, List<Permission> acceptedPermissions, List<Permission> declinedPermissions);

    public abstract void onCancel();
}
