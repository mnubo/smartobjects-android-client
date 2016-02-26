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

import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.Value;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Sets.newHashSet;


@Value
@JsonInclude(value = NON_EMPTY)
public class Owner {

    public static final String OWNERS_PATH = "owners";

    public static final String REGISTRATION_DATE = "x_registration_date";
    public static final String PASSWORD = "x_password";
    public static final String USERNAME = "username";
    public static final String EVENT_ID = "event_id";

    @JsonProperty(USERNAME)
    final String username;
    @JsonProperty(PASSWORD)
    final String password;
    @JsonProperty(REGISTRATION_DATE)
    final DateTime registrationDate;
    @Getter(onMethod = @__(@JsonAnyGetter))
    final ImmutableMap<String, Object> attributes;

    @JsonCreator
    @Builder(toBuilder = true)
    public Owner(@JsonProperty(USERNAME) String username,
                 @JsonProperty(PASSWORD) String password,
                 @JsonProperty(REGISTRATION_DATE) DateTime registrationDate,
                 @Singular ImmutableMap<String, Object> attributes) {
        Preconditions.checkArgument(!isNullOrEmpty(username), "Missing username");

        this.username = username;
        this.password = password;
        this.attributes = attributes;

        this.registrationDate = registrationDate;
    }
}