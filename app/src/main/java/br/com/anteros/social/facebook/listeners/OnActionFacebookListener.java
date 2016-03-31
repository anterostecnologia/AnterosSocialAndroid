package br.com.anteros.social.facebook.listeners;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import br.com.anteros.social.facebook.actions.ActionCursor;

/**
 * Created by edson on 23/03/16.
 */
public abstract class OnActionFacebookListener<T> implements OnErrorFacebookListener {

    private ActionCursor<T> cursor;

    public OnActionFacebookListener() {
    }

    public void onComplete(T response) {
    }

    @Override
    public void onException(Throwable throwable) {
    }

    @Override
    public void onFail(String reason) {
    }

    public void onThinking() {
    }

    public void setCursor(ActionCursor<T> cursor) {
        this.cursor = cursor;
    }

    /**
     * Return <code>True</code> if there is another next page with more results.
     * You can iterate to the next page and get more results by calling to
     * {@link #getNext()} method.
     *
     * @return <code>True</code> if more results exist.
     */
    public boolean hasNext() {
        if (cursor != null) {
            return cursor.hasNext();
        }
        return false;
    }

    /**
     * Return <code>True</code> if there is another previous page with more
     * results. You can iterate to the next page and get more results by calling
     * to {@link #getPrev()} method.
     *
     * @return <code>True</code> if more results exist.
     */
    public boolean hasPrev() {
        if (cursor != null) {
            return cursor.hasPrev();
        }
        return false;
    }

    /**
     * Ask for the next page results in async way. When the response will arrive
     * {@link #onComplete(Object)} method will be invoked again.
     */
    public void getNext() {
        if (cursor != null) {
            cursor.next();
        }
    }

    /**
     * Ask for the prev page results in async way. When the response will arrive
     * {@link #onComplete(Object)} method will be invoked again.
     */
    public void getPrev() {
        if (cursor != null) {
            cursor.prev();
        }
    }

    /**
     * Get the cursor that actually does the 'getMore()' action. For example, if
     * you want to hold this instance of cursor somewhere in your app and only
     * in some point of time to use it.
     *
     * @return {@link br.com.anteros.social.facebook.actions.Cursor} for iteration over pages of response.
     */
    public ActionCursor<T> getCursor() {
        return cursor;
    }

    /**
     * Get the last page number that was retrieved.
     *
     * @return The page number.
     */
    public int getPageNum() {
        return cursor.getPageNum();
    }

    public Type getGenericType() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return parameterizedType.getActualTypeArguments()[0];
    }
}
