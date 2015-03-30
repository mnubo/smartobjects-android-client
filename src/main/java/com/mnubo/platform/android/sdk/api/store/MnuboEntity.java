/*
 * Copyright (c) 2015 Mnubo. Released under MIT License.
 *
 *     Permission is hereby granted, free of charge, to any person obtaining a copy
 *     of this software and associated documentation files (the "Software"), to deal
 *     in the Software without restriction, including without limitation the rights
 *     to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *     copies of the Software, and to permit persons to whom the Software is
 *     furnished to do so, subject to the following conditions:
 *
 *     The above copyright notice and this permission notice shall be included in
 *     all copies or substantial portions of the Software.
 *
 *     THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *     IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *     FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *     AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *     LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *     OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *     THE SOFTWARE.
 */

package com.mnubo.platform.android.sdk.api.store;

import java.io.Serializable;
import java.util.Map;

/**
 * Object that contains all the relevant information to retry a request later.
 */
public class MnuboEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public static enum EntityType {
        ADD_SAMPLES, ADD_SAMPLE_PUBLIC
    }

    protected String id;
    protected Map<String, Object> idData;
    protected EntityType type;
    protected Object value;

    /**
     * Create a MnuboEntity instance
     */
    public MnuboEntity() {
    }

    /**
     * Create a MnuboEntity instance
     *
     * @param idData id data to query the resource (id, idtype, sensorname, etc...)
     * @param value  data
     */
    public MnuboEntity(EntityType type, String id, Map<String, Object> idData, Object value) {
        this.id = id;
        this.idData = idData;
        this.type = type;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public EntityType getType() {
        return type;
    }

    public Map<String, Object> getIdData() {
        return idData;
    }

    public Object getValue() {
        return value;
    }


}
