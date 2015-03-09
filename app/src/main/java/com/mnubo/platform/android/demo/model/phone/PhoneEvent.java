package com.mnubo.platform.android.demo.model.phone;

import android.location.Location;

import com.mnubo.platform.android.sdk.models.smartobjects.samples.Sample;

public class PhoneEvent {

    public static enum Type {
        install,
        launch,
    }

    private static final String NAME = "generic_event";
    private static final String EVENT_TYPE = "event_type";
    private static final String BDADDR = "bdaddr";
    private static final String APP_VERSION = "app_version";
    private static final String APP_FUNNEL_STATE = "app_funnel_state";
    private static final String APP_LIFE_STATE = "app_life_state";
    private static final String APP_USAGE_STATE = "app_usage_state";

    private final Sample sample;

    public PhoneEvent(Sample sample) {
        this.sample = sample;
    }

    public PhoneEvent() {
        this.sample = new Sample();
        this.sample.setSensorName(NAME);
    }

    public PhoneEvent location(final Location location) {
        this.sample.setLocation(location);
        return this;
    }

    public PhoneEvent timestamp(final String timestamp) {
        this.sample.setTimestamp(timestamp);
        return this;
    }

    public PhoneEvent type(final Type type) {
        this.sample.addValue(EVENT_TYPE, type.toString());
        return this;
    }

    public PhoneEvent appVersion(final String appVersion) {
        this.sample.addValue(APP_VERSION, appVersion);
        return this;
    }

    public PhoneEvent bdaddr(final String bdaddr) {
        this.sample.addValue(BDADDR, bdaddr);
        return this;
    }

    public PhoneEvent appFunnelState(final String funnelState) {
        this.sample.addValue(APP_FUNNEL_STATE, funnelState);
        return this;
    }


    public PhoneEvent appUsageState(final String usageState) {
        this.sample.addValue(APP_USAGE_STATE, usageState);
        return this;
    }


    public PhoneEvent appLifeState(final String lifeState) {
        this.sample.addValue(APP_LIFE_STATE, lifeState);
        return this;
    }

    public Sample getSample() {
        return sample;
    }
}
