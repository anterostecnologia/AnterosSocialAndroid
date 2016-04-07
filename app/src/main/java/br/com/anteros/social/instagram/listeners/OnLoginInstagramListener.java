package br.com.anteros.social.instagram.listeners;

/**
 * Created by edson on 25/03/16.
 */
public interface OnLoginInstagramListener extends OnErrorInstagramListener {

    void onLogin();

    void onCancel();
}