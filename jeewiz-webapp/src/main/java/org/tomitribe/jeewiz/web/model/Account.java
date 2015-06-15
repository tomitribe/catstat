/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tomitribe.jeewiz.web.model;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Email;
import org.tomitribe.jeewiz.web.annotations.Password;

import javax.inject.Named;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.security.Principal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author WalmartLabs
 * @author Ryan McGuinness [rmcguinness@walmartlabs.com]
 *         Created: 6/5/15
 */
@Named
@XmlRootElement(name = "account", namespace = "urn:example:model")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "ACCT", uniqueConstraints = {
    @UniqueConstraint(name = "UQ_ACCT_01", columnNames = {"USR_NM"}),
    @UniqueConstraint(name = "UQ_ACCT_02", columnNames = {"EMAIL"})
})
@NamedQueries({
    @NamedQuery(name = Account.QRY_FIND_BY_USERNAME, query = "SELECT a FROM Account  a WHERE a.userName = ?1")
})
public class Account extends AbstractPersistable implements Principal {
    public static final String QRY_FIND_BY_USERNAME = "Account.findByUserName";

    @XmlAttribute(name = "uid", required = true)
    @Basic(optional = false)
    @Column(name = "USR_NM", nullable = false)
    private String userName;

    @XmlAttribute(name = "email", required = true)
    @Basic(optional = false)
    @Column(name = "EMAIL", nullable = false)
    @Email(message = "Invalid email address")
    private String email;

    @XmlAttribute(name = "pwd", required = true)
    @Basic(optional = false)
    @Column(name = "PWD", nullable = false)
    @Password
    private String password;

    @XmlElement(name = "lastLogin", nillable = true)
    @Basic(optional = true)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LST_LGN", nullable = true)
    private Date lastLogin;

    @XmlAttribute(name = "enabled", required = true)
    @Basic(optional = false)
    @Type(type = "yes_no")
    @Column(name = "ENBLD", length = 1, nullable = false)
    private boolean enabled;

    @XmlElement(name = "locked", required = true)
    @Basic(optional = false)
    @Type(type = "yes_no")
    @Column(name = "LCKD", length = 1, nullable = false)
    private boolean locked;

    @XmlElement(name = "expired", required = true)
    @Basic(optional = false)
    @Type(type = "yes_no")
    @Column(name = "EXP", length = 1, nullable = false)
    private boolean expired;

    @XmlElement(name = "credentials-expired", required = true)
    @Basic(optional = false)
    @Type(type = "yes_no")
    @Column(name = "CRD_EXP", length = 1, nullable = false)
    private boolean credentialsExpired;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "ACCT_IMG",
        joinColumns = @JoinColumn(name = "ACCT_ID"),
        inverseJoinColumns = @JoinColumn(name = "IMG_ID"),
        uniqueConstraints = {
            @UniqueConstraint(
                name = "UQ_ACCT_IMG_001",
                columnNames = {"ACCT_ID", "IMG_ID"})})
    private Image image;

    @ManyToMany(cascade = CascadeType.ALL,
        targetEntity = Role.class,
        fetch = FetchType.EAGER)
    @JoinTable(name = "ACCT_ROLE",
        joinColumns = @JoinColumn(name = "ACCT_ID"),
        inverseJoinColumns = @JoinColumn(name = "ROLE_ID"),
        uniqueConstraints = {
            @UniqueConstraint(name = "UQ_ACCT_ROLE_001",
                columnNames = {"ACCT_ID", "ROLE_ID"})
        }
    )
    private Set<Role> roles = new HashSet<>();

    public String getUserName() {
        return userName;
    }

    public String getName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        if (userName != null ? !userName.equals(account.userName) : account.userName != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return userName != null ? userName.hashCode() : 0;
    }
}
