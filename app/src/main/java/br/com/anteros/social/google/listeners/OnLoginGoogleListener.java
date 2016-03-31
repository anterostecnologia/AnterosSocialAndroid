package br.com.anteros.social.google.listeners;

/**
 * Created by edson on 25/03/16.
 */
public interface OnLoginGoogleListener extends OnErrorGoogleListener {

    void onLogin();

    void onCancel();
}