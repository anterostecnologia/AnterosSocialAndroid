package br.com.anteros.social.facebook.listeners;

import java.util.List;

import br.com.anteros.social.facebook.actions.Permission;

/**
 * Created by edson on 23/03/16.
 */
public interface OnLoginFacebookListener extends OnErrorFacebookListener {

    void onLogin(String accessToken, List<Permission> acceptedPermissions, List<Permission> declinedPermissions);

    void onCancel();
}
