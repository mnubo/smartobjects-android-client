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

package com.mnubo.platform.android.sdk.models.groups;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

@JsonInclude(Include.NON_NULL)
public class Group {
    private String label;

    private String owner;

    private UUID id;

    private String type;

    @JsonProperty("natural_key")
    private String naturalKey;

    public UUID getId() {
        return id;
    }

    public void setId(UUID aGroupId) {
        id = aGroupId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String aOwner) {
        this.owner = aOwner;
    }

    public String getType() {
        return type;
    }

    public void setType(String aType) {
        type = aType;
    }

    public String getNaturalKey() {
        return naturalKey;
    }

    public void setNaturalKey(String aNaturalKey) {
        naturalKey = aNaturalKey;
    }
}
