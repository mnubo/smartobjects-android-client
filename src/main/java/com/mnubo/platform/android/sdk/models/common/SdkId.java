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

package com.mnubo.platform.android.sdk.models.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.apache.commons.lang3.Validate;

import java.io.Serializable;
import java.util.UUID;

import static com.mnubo.platform.android.sdk.models.common.IdType.objectid;
import static com.mnubo.platform.android.sdk.models.common.IdType.uuid;

/**
 * SdkId is used to perform query against the Mnubo API.
 * In Mnubo, each object has an ID which is a UUID and an
 * unique human readable identifier called the natural key.
 * <p/>
 * SdkId is used to translate correctly to the good query parameters
 * to perform the correct request when fetching an object.
 */
public class SdkId implements Serializable {

    @JsonIgnore
    private static final long serialVersionUID = 1L;

    private String id;
    private IdType idType = objectid;

    /**
     * Build an SdkId with the default IdType (objectid)
     *
     * @param stringValue used as the id value
     * @return SdkId with IdType(objectid) and given value
     */
    public static SdkId valueOf(String stringValue) {
        return new SdkId().withId(stringValue);
    }

    /**
     * Build a SdkId with the given value and type
     *
     * @param id     id value
     * @param idType IdType of the id
     * @return SdkId with given value and type
     */
    public static SdkId build(String id, IdType idType) {
        return valueOf(id).withIdType(idType != null ? idType : objectid);
    }

    /**
     * Build a SdkId with the UUID as a value and IdType objectid
     *
     * @param id id value
     * @return SdkId with given value and type objectid
     */
    public static SdkId build(UUID id) {
        return valueOf(id.toString()).withIdType(objectid);
    }

    /**
     * Build a SdkId with the UUID as a value and IdType uuid
     *
     * @param id id value
     * @return SdkId with given value and type uuid
     */
    public static SdkId withUuid(UUID id) {
        return valueOf(id.toString()).withIdType(uuid);
    }

    /**
     * Builder method that sets the id value
     *
     * @param id id value
     * @return return current SdkId instance
     */
    public SdkId withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Builder method that sets the idtype
     *
     * @param idType IdType value
     * @return return current SdkId instance
     */
    public SdkId withIdType(IdType idType) {
        Validate.notNull(idType);
        this.idType = idType;

        return this;
    }

    /**
     * Check method to see if IdType is uuid type
     *
     * @return true if IdType is objectid || uuid, false otherwise
     */
    public boolean isUUID() {
        return getIdType().isUUID();
    }

    /**
     * Check method to see if IdType is string type
     *
     * @return true if isUUID is false, true otherwise
     */
    public boolean isString() {
        return getIdType().isString();
    }

    public void setId(String id) {
        withId(id);
    }

    public void setIdType(IdType idType) {
        withIdType(idType);
    }

    public String getId() {
        return id;
    }

    public IdType getIdType() {
        return idType != null ? idType : objectid;
    }

    @Override
    public String toString() {
        return getId();
    }

}
