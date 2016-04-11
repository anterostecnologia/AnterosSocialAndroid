package br.com.anteros.social.facebook.listeners;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import br.com.anteros.social.facebook.actions.ActionCursor;

/**
 * Created by edson on 23/03/16.
 */
public abstract class OnActionFacebookListener<T> {

    private ActionCursor<T> cursor;

    public OnActionFacebookListener() {
    }

    public abstract void onComplete(T response);

    public abstract void onFail(Throwable throwable);

    public abstract void onThinking();

    public void setCursor(ActionCursor<T> cursor) {
        this.cursor = cursor;
    }

    public boolean hasNext() {
        if (cursor != null) {
            return cursor.hasNext();
        }
        return false;
    }

    public boolean hasPrev() {
        if (cursor != null) {
            return cursor.hasPrev();
        }
        return false;
    }

    public void getNext() {
        if (cursor != null) {
            cursor.next();
        }
    }

    public void getPrev() {
        if (cursor != null) {
            cursor.prev();
        }
    }

    public ActionCursor<T> getCursor() {
        return cursor;
    }

    public int getPageNum() {
        return cursor.getPageNum();
    }

    public Type getGenericType() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return parameterizedType.getActualTypeArguments()[0];
    }
}
