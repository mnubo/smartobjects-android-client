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

package com.mnubo.platform.android.sdk.internal.spis.smartobjects;

import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.common.ValueType;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObject;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Sample;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Samples;
import com.mnubo.platform.android.sdk.models.users.Users;

import java.util.List;

public interface SmartObjectsSdkSpi {
    void delete(SdkId objectId);

    SmartObject create(SmartObject object, boolean updateIfExists);

    void update(SdkId objectId, SmartObject object);

    SmartObject findOne(SdkId objectId);

    SmartObject findOne(SdkId objectId, List<String> attributes);

    Samples searchSamples(SdkId objectId, String sensorName);

    Samples searchSamples(SdkId objectId, String sensorName, ValueType value);

    Samples searchSamples(SdkId objectId, String sensorName, ValueType value,
                          String from, String to);

    Samples searchSamples(SdkId objectId, String sensorName, ValueType value,
                          String from, String to, int resultSizeLimit);

    void addSamples(SdkId objectId, Samples samples);

    void addSampleOnPublicSensor(SdkId objectId, String sensorName, Sample expectedSample);

    Users listOwnersHistory(SdkId objectid);

    Users listOwnersHistory(SdkId objectid, boolean details);

}
