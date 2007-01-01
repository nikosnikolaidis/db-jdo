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
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package org.apache.jdo.pc;

/**
* A extended project class that differs from PCProject class
* in the type of the relationship field: it is Map of the
* employees.
*
* @author Marina Vatkina
*/
public class PCProject1 {
    public long projid;
    
    public String name;
    
    public java.util.Map employees;
    
    public String toString() {
        return "Project: " + name + ", id=" + projid +
            ", emps: " + employees.entrySet().size();
    }

    public PCProject1() {
    }
    
    public PCProject1(long _projid, String _name) {
        projid = _projid;
        name = _name;
    }
    
    public long getProjid() {
        return projid;
    }
    
    public void setProjid(long projid) {
        this. projid = projid;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this. name = name;
    }
    
    public java.util.Map getEmployees() {
        return employees;
    }
    
    public void setEmployees(java.util.Map employees) {
        this. employees = employees;
    }
    
    public static class Oid {
        public long projid;
        
        public Oid() {
        }
        
        public boolean equals(java.lang.Object obj) {
            if( obj==null ||
            !this.getClass().equals(obj.getClass()) ) return( false );
            Oid o=(Oid) obj;
            if( this.projid!=o.projid ) return( false );
            return( true );
        }
        
        public int hashCode() {
            int hashCode=0;
            hashCode += projid;
            return( hashCode );
        }
        
        
}
}

