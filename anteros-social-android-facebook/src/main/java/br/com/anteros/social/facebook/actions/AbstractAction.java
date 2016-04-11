package br.com.anteros.social.facebook.actions;

import br.com.anteros.social.facebook.AnterosFacebookConfiguration;
import br.com.anteros.social.facebook.AnterosFacebookSession;

/**
 * Created by edson on 23/03/16.
 */


public abstract class AbstractAction {

    protected AnterosFacebookSession sessionManager;
    protected AnterosFacebookConfiguration configuration;

    public AbstractAction(AnterosFacebookSession sessionManager) {
        this.sessionManager = sessionManager;
        this.configuration = sessionManager.getConfiguration();
    }

    public void execute() {
        executeImpl();
    }

    protected abstract void executeImpl();



}

