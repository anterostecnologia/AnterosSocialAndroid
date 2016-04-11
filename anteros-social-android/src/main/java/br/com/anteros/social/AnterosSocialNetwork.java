package br.com.anteros.social;

import android.content.Intent;

/**
 * Created by edson on 07/04/16.
 */
public interface AnterosSocialNetwork {

    public void silentLogin();

    public void login();

    public void logout();

    public void revoke();

    public boolean isLogged();

    public void onActivityResult(int requestCode, int resultCode, Intent data);

    public AnterosSocialSession getSession();

    public void getProfile(OnProfileListener onProfileListener);

    public void setOnLoginListener(OnLoginListener onLoginListener);

    public void setOnLogoutListener(OnLogoutListener onLogoutListener);
}
