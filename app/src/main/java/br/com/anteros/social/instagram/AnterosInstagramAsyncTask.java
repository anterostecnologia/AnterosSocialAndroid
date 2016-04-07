package br.com.anteros.social.instagram;

import android.os.AsyncTask;
import android.os.Bundle;

/**
 * Created by edson on 05/04/16.
 */

public abstract class AnterosInstagramAsyncTask extends AsyncTask<Bundle, Void, Bundle> {

    public static final String RESULT_ERROR = "AnterosInstagramAsyncTask.RESULT_ERROR";

    @Override
    protected Bundle doInBackground(Bundle... params) {
        return null;
    }
}
