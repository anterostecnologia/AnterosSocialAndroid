package br.com.anteros.social.core;

/**
 * Created by edson on 07/04/16.
 */
public interface OnProfileListener {

    public void onComplete(SocialProfile response);

    public void onFail(Throwable throwable);

    public void onThinking();

}
