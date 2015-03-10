/*
 * ---------------------------------------------------------------------------
 * 
 * COPYRIGHT (c) 2014 Mnubo Inc. All Rights Reserved.
 * 
 * The copyright to the computer program(s) herein is the property of Mnubo Inc. The program(s) may be used and/or
 * copied only with the written permission from Mnubo Inc. or in accordance with the terms and conditions stipulated in
 * the agreement/contract under which the program(s) have been supplied.
 * 
 * Author: dmasse Date : Aug 20, 2014
 * 
 * ---------------------------------------------------------------------------
 */

package com.mnubo.platform.android.sdk.models.smartobjects.samples;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of {@link com.mnubo.platform.android.sdk.models.smartobjects.samples.Sample}
 *
 * @see com.mnubo.platform.android.sdk.models.smartobjects.samples.Samples
 */
@JsonInclude(Include.NON_NULL)
public class Samples {
    private Sample common;
    private List<Sample> samples;
    private String status;
    private String error;

    public Samples() {
        samples = new ArrayList<>();
    }

    public Sample getCommon() {
        return common;
    }

    public void setCommon(Sample aCommonSample) {
        common = aCommonSample;
    }

    public List<Sample> getSamples() {
        return samples;
    }

    public void setSamples(List<Sample> samples) {
        this.samples = samples;
    }

    public void addSample(Sample sample) {
        this.samples.add(sample);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String aStatus) {
        status = aStatus;
    }

    public String getError() {
        return error;
    }

    public void setError(String aError) {
        error = aError;
    }

    public void clear() {
        this.samples.clear();
    }

    @Override
    public String toString() {
        return "Samples{" +
                "samples=" + samples +
                '}';
    }
}
