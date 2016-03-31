package br.com.anteros.social.facebook.actions;

import br.com.anteros.social.facebook.AnterosFacebook;
import br.com.anteros.social.facebook.AnterosFacebookConfiguration;
import br.com.anteros.social.facebook.AnterosFacebookSessionManager;

/**
 * Created by edson on 23/03/16.
 */


public abstract class AbstractAction {

    protected AnterosFacebookSessionManager sessionManager;
    protected AnterosFacebookConfiguration configuration = AnterosFacebook.getConfiguration();

    public AbstractAction(AnterosFacebookSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void execute() {
        executeImpl();
    }

    protected abstract void executeImpl();



}

