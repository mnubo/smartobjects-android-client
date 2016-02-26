/*
 * Copyright (c) 2016 Mnubo. Released under MIT License.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.mnubo.android.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import org.joda.time.DateTime;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.Value;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.google.common.base.Strings.isNullOrEmpty;


@Value
@JsonInclude(value = NON_EMPTY)
public class Event implements Serializable {
    public final static String EVENTS = "events";

    public static final String OBJECT = "x_object";
    public static final String EVENT_TYPE = "x_event_type";
    public static final String TIMESTAMP = "x_timestamp";

    @JsonProperty(EVENT_TYPE)
    final String eventType;
    @JsonProperty(TIMESTAMP)
    final DateTime timestamp;
    @Getter(onMethod = @__(@JsonAnyGetter))
    final ImmutableMap<String, Object> timeseries;

    @JsonCreator
    @Builder(toBuilder = true)
    public Event(@JsonProperty(EVENT_TYPE) String eventType,
                 @JsonProperty(TIMESTAMP) DateTime timestamp,
                 @Singular("timeserie") ImmutableMap<String, Object> timeseries) {
        Preconditions.checkArgument(!isNullOrEmpty(eventType), "Missing x_event_type");

        this.eventType = eventType;
        this.timeseries = timeseries;
        this.timestamp = timestamp;

    }
}