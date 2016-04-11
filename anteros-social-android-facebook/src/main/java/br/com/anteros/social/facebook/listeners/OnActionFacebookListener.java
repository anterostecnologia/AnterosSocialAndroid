/*
 * ******************************************************************************
 *  * Copyright 2016 Anteros Tecnologia
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

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
