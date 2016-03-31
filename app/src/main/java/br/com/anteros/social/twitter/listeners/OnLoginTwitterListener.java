package br.com.anteros.social.twitter.listeners;

/**
 * Created by edson on 25/03/16.
 */
public interface OnLoginTwitterListener extends OnErrorTwitterListener {

    void onLogin();

    void onCancel();
}