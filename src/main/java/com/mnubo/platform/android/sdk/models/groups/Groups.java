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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public class Groups {
    private List<Group> groupList;
    private int count;

    public Groups() {
        groupList = new ArrayList<>(0);
    }

    public List<Group> getGroupList() {
        if (groupList == null) {
            groupList = new LinkedList<>();
        }

        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }

    public void addGroup(Group group) {
        groupList.add(group);
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
        for (int i = 0; i < groupList.size(); i++) {
            out = out + "GroupLabel: " + groupList.get(i).getLabel() + ", owner: "
                    + groupList.get(i).getOwner() + "\n";
        }
        return out;
    }
}
