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
