package br.com.anteros.social.facebook.actions;

import com.facebook.GraphRequest;

/**
 * Created by edson on 23/03/16.
 */
public class ActionCursor<T> {


    private final FacebookAction<T> facebookAction;
    private GraphRequest nextPage = null;
    private GraphRequest prevPage = null;
    private int mPageNum = 0;

    public ActionCursor(FacebookAction<T> getAction) {
        facebookAction = getAction;
    }

    public boolean hasNext() {
        return nextPage != null ? true : false;
    }

    public boolean hasPrev() {
        return prevPage != null ? true : false;
    }

    public int getPageNum() {
        return mPageNum;
    }

    public void next() {
        mPageNum++;
        facebookAction.runRequest(nextPage);
    }

    public void prev() {
        mPageNum--;
        facebookAction.runRequest(prevPage);
    }

    void setNextPage(GraphRequest requestNextPage) {
        nextPage = requestNextPage;
    }

    void setPrevPage(GraphRequest requestPrevPage) {
        prevPage = requestPrevPage;
    }
}
