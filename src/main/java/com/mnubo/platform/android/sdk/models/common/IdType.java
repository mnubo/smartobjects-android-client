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

/**
 * Are and objectid are basically the same but they translate to
 * different query parameters which are currently required by the
 * Mnubo API.
 *
 * deviceid and natural are also the same. They are human readable unique
 * identifier to be used along with the uuid of the identity.
 *
 * objectid and deviceid shouldn't be and they will be removed in a near future.
 * The objectid was originally the uuid of a {@link com.mnubo.platform.android.sdk.models.smartobjects.SmartObject} and
 * the deviceid was the natural key of an {@link com.mnubo.platform.android.sdk.models.smartobjects.SmartObject}
 *
 * @see com.mnubo.platform.android.sdk.models.common.SdkId
 */
public enum IdType {

    deviceid, objectid, natural, uuid;

    public boolean isUUID() {
        return this.equals(uuid) || this.equals(objectid);
    }

    public boolean isString() {
        return !isUUID();
    }

}
