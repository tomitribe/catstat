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

import org.hibernate.annotations.GenericGenerator;
import org.tomitribe.jeewiz.web.listener.PersistableListener;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.Date;

/**
 * @author WalmartLabs
 * @author Ryan McGuinness [rmcguinness@walmartlabs.com]
 *         Created: 6/5/15
 */
@XmlAccessorType(XmlAccessType.FIELD)
@MappedSuperclass
@EntityListeners({PersistableListener.class})
public abstract class AbstractPersistable implements Persistable {
    private static final long serialVersionUID = 6817430140136345673L;

    @XmlAttribute(name = "id")
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "ID", length = 36, nullable = false)
    private String id;

    @XmlAttribute(name = "ver")
    @Version
    @Column(name = "VER", nullable = false)
    private Integer version;

    @XmlElement(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    @Basic(optional = false)
    @Column(name = "DT_CR", nullable = false)
    private Date created = new Date();

    @XmlElement(name = "last-modified")
    @Temporal(TemporalType.TIMESTAMP)
    @Basic(optional = false)
    @Column(name = "DT_LM", nullable = false)
    private Date lastModified = new Date();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = (id != null && id.length() == 0) ? null : id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getCreated() {
        return (created != null) ? new Date(created.getTime()) : null;
    }

    public void setCreated(Date created) {

        this.created = (created != null) ? new Date(created.getTime()) : null;
    }

    public Date getLastModified() {
        return (lastModified != null) ? new Date(lastModified.getTime()) : null;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = (lastModified != null) ? new Date(lastModified.getTime()) : null;
    }
}
