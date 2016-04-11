package br.com.anteros.social;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by edson on 07/04/16.
 */
public interface AnterosSocialSession {

    public void onActivityResult(int requestCode, int resultCode, Intent data);

    public void login();

    public void logout();

    public boolean isLogged();

    public void setActivity(Activity activity);

    public void silentLogin();

    public Activity getActivity();

    public void getProfile(OnProfileListener onProfileListener);

    public void revoke();

    public void setOnLoginListener(OnLoginListener onLoginListener);

    public void setOnLogoutListener(OnLogoutListener onLogoutListener);

    public void checkListeners();
}
