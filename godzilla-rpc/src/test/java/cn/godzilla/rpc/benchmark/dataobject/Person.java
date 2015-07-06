/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.godzilla.rpc.benchmark.dataobject;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author ding.lid
 */
public class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    String                    personId;

    String                    loginName;

    PersonStatus              status;

    PersonInfo                info;

    byte[]                    attachment;

    public Person() {

    }

    public Person(String id) {
        this.personId = id;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public PersonInfo getInfo() {
        return info;
    }

    public void setInfo(PersonInfo infoProfile) {
        this.info = infoProfile;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setStatus(PersonStatus status) {
        this.status = status;
    }

    public String getLoginName() {
        return this.loginName;
    }

    public PersonStatus getStatus() {
        return this.status;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (!Arrays.equals(attachment, person.attachment)) return false;
        if (info != null ? !info.equals(person.info) : person.info != null)
            return false;
        if (loginName != null ? !loginName.equals(person.loginName) : person.loginName != null)
            return false;
        if (personId != null ? !personId.equals(person.personId) : person.personId != null)
            return false;
        if (status != person.status) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = personId != null ? personId.hashCode() : 0;
        result = 31 * result + (loginName != null ? loginName.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (info != null ? info.hashCode() : 0);
        result = 31 * result + (attachment != null ? Arrays.hashCode(attachment) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BigPerson [personId=" + personId + ", loginName=" + loginName + ", status="
                + status + ", info=" + info + "]";
    }
}