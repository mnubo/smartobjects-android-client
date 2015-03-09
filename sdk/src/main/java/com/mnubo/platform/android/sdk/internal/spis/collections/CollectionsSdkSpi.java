/*
 * ---------------------------------------------------------------------------
 * 
 * COPYRIGHT (c) 2014 Mnubo Inc. All Rights Reserved.
 * 
 * The copyright to the computer program(s) herein is the property of Mnubo Inc. The program(s) may be used and/or
 * copied only with the written permission from Mnubo Inc. or in accordance with the terms and conditions stipulated in
 * the agreement/contract under which the program(s) have been supplied.
 * 
 * Author: pgehl Date : Aug 29, 2014
 * 
 * ---------------------------------------------------------------------------
 */

package com.mnubo.platform.android.sdk.internal.spis.collections;

import com.mnubo.platform.android.sdk.models.collections.Collection;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObjects;

public interface CollectionsSdkSpi {

    //[/rest/collections/{collectionid}],methods=[DELETE]
    void delete(SdkId collectionId);

    //    [/rest/collections],methods=[POST]
    Collection create(Collection collection);

    //[/rest/collections/{collectionid}],methods=[GET]
    Collection findOne(SdkId collectionId);

    //[/rest/collections/{collectionid}/objects],methods=[GET]
    SmartObjects listAllObjects(SdkId collectionId, int resultSizeLimit);

    //[/rest/collections/{collectionid}/objects],methods=[GET]
    SmartObjects listAllObjects(SdkId collectionId);

    //[/rest/collections/{collectionid}/objects/{id}],methods=[PUT]
    void addObject(SdkId collectionId, SdkId objectId);

    //[/rest/collections/{collectionid}/objects/{objectid}],methods=[DELETE]
    void removeObject(SdkId collectionId, SdkId objectId);
}
