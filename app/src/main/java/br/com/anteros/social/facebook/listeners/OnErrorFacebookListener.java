package br.com.anteros.social.facebook.listeners;

/**
 * Created by edson on 23/03/16.
 */
public interface OnErrorFacebookListener {

    void onException(Throwable throwable);

    void onFail(String reason);
}
