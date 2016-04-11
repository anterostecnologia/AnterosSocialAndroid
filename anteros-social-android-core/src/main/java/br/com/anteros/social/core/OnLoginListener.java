package br.com.anteros.social.core;

/**
 * Created by edson on 07/04/16.
 */
public interface OnLoginListener {

    public void onLogin();

    public void onCancel();

    public void onFail(Throwable throwable);
}
