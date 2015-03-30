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

package com.mnubo.platform.android.sdk.api.store.impl;

import com.mnubo.platform.android.sdk.api.store.MnuboEntity;

import java.io.Serializable;

/**
 * Entity specifically designed to be persisted on the file system. Contains
 * it's location on the disk.
 */
class MnuboFileEntity extends MnuboEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String file;

    /**
     * No arg constructor required for Serializable
     */
    public MnuboFileEntity() {
        super();
    }

    public MnuboFileEntity(MnuboEntity entity, String file) {
        super(entity.getType(), entity.getId(), entity.getIdData(), entity.getValue());
        this.file = file;
    }

    /**
     * @return location of the file
     */
    public String getFile() {
        return file;
    }
}