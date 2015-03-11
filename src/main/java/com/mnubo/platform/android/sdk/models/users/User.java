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

package com.mnubo.platform.android.sdk.models.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mnubo.platform.android.sdk.models.collections.Collection;
import com.mnubo.platform.android.sdk.models.common.Attribute;
import com.mnubo.platform.android.sdk.models.groups.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A Mnubo User
 *
 * @see com.mnubo.platform.android.sdk.models.users.Users
 */
@JsonInclude(Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private String username;

    private String password;

    @JsonProperty("confirmed_password")
    private String confirmedPassword;

    private String firstname;

    private String lastname;

    @JsonProperty("registration_date")
    private String registrationDate;

    private List<Collection> collections;

    private List<Group> groups;

    private List<Attribute> attributes = new ArrayList<>();

    private boolean isAdmin;

    @JsonIgnore
    private UUID id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmedPassword() {
        return confirmedPassword;
    }

    public void setConfirmedPassword(String confirmedPassword) {
        this.confirmedPassword = confirmedPassword;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public List<Collection> getCollections() {
        return collections;
    }

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFullname() {
        return String.format("%s %s", this.getFirstname(), this.getLastname());
    }

    @Override
    public String toString() {
        return String.format("%s", this.getUsername());
    }
}
