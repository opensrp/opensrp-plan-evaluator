package org.smartregister.domain;


import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Created by Ephraim Kigamba - nek.eam@gmail.com on 01-03-2021.
 */
public class Practitioner implements Serializable {

    private static final long serialVersionUID = -8367551045898354954L;

    private String identifier;

    private Boolean active;

    private String name;

    private String userId;

    private String username;

    private DateTime dateCreated;

    private DateTime dateEdited;

    private long serverVersion;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public DateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(DateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public DateTime getDateEdited() {
        return dateEdited;
    }

    public void setDateEdited(DateTime dateEdited) {
        this.dateEdited = dateEdited;
    }

    public long getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(long serverVersion) {
        this.serverVersion = serverVersion;
    }
}
