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
