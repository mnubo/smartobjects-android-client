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

package com.mnubo.platform.android.sdk.models.collections;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public class Collections {
    @JsonProperty("collectionList")
    private List<Collection> collections;
    private int count;

    public Collections() {
        collections = new ArrayList<Collection>(0);
    }

    public List<Collection> getCollections() {
        return collections;
    }

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }

    public void addCollection(Collection Collection) {
        collections.add(Collection);
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        String out = "";
        for (int i = 0; i < collections.size(); i++) {
            out = out + "CollectionLabel: " + collections.get(i).getLabel() + ", owner: "
                    + collections.get(i).getOwner() + "\n";
        }
        return out;
    }
}
