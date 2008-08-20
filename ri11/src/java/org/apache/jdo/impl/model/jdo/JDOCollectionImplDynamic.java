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

package org.apache.jdo.impl.model.jdo;

import org.apache.jdo.impl.model.jdo.util.TypeSupport;
import org.apache.jdo.model.ModelFatalException;
import org.apache.jdo.model.java.JavaType;
import org.apache.jdo.model.jdo.JDOClass;
import org.apache.jdo.model.jdo.JDOCollection;
import org.apache.jdo.model.jdo.JDOField;
import org.apache.jdo.model.jdo.JDOModel;
import org.apache.jdo.util.I18NHelper;

/**
 * An instance of this class represents the JDO relationship metadata 
 * of a collection relationship field. This dynamic implementation only
 * stores property values explicitly set by setter method. 
 *
 * @author Michael Bouschen
 * @since 1.1
 * @version 1.1
 */
public class JDOCollectionImplDynamic extends JDORelationshipImpl
    implements JDOCollection {
    
    /** Property embeddedElement. */
    protected Boolean embeddedElement;

    /** Property elementType. */
    protected transient JavaType elementType;

    /** Property elementTypeName. Defaults to java.lang.Object. */
    private String elementTypeName = "java.lang.Object"; //NOI18N

    /** I18N support */
    private final static I18NHelper msg =  
        I18NHelper.getInstance(JDOCollectionImplDynamic.class);
    
    /**
     * Determines whether the values of the elements should be stored if 
     * possible as part of the instance instead of as their own instances 
     * in the datastore.
     * @return <code>true</code> if the elements should be stored as part of 
     * the instance; <code>false</code> otherwise
     */
    public boolean isEmbeddedElement() {
        if (embeddedElement != null) {
            // return embeddedElement, if explicitly set by the setter
            return embeddedElement.booleanValue();
        }
        
        // not set => calculate
        JavaType type = getElementType();
        return (type != null) ? 
            TypeSupport.isEmbeddedElementType(type) : false;
    }
    
    /**
     * Set whether the values of the elements should be stored if possible as 
     * part of the instance instead of as their own instances in the datastore.
     * @param embeddedElement <code>true</code> if elements should be stored 
     * as part of the instance
     */
    public void setEmbeddedElement(boolean embeddedElement) {
        this.embeddedElement = (embeddedElement ? Boolean.TRUE : Boolean.FALSE);
    }

    /** 
     * Get the type representation of the collection elements. 
     * @return the element type
     */
    public JavaType getElementType() {
        if (elementType != null) {
            // return elementType, if explicitly set by the setter
            return elementType;
        }
    
        // not set => calculate
        JavaType type = null;
        if (elementTypeName != null) {
            JDOField jdoField = getDeclaringField();
            JDOClass jdoClass = jdoField.getDeclaringClass();
            JDOModel jdoModel = jdoClass.getDeclaringModel();
            type = TypeSupport.resolveType(jdoModel, elementTypeName,
                                           jdoClass.getPackagePrefix());
            if (type == null) {
                throw new ModelFatalException(
                    msg.msg("EXC_CannotResolveElementType", elementTypeName,
                            jdoField.getName(), jdoClass.getName())); //NOI18N
            }
        }
        
        return type;
    }

    /** 
     * Set the type representation of the collection elements.
     * @param elementType the type representation of the collection elements
     */
    public void setElementType(JavaType elementType) {
        this.elementType = elementType;
        if (elementType != null) {
            setElementTypeName(elementType.getName());
        }
    }

    /** 
     * Get the type of collection elements as string.
     * @return the element type as string
     */
    public String getElementTypeName() {
        return elementTypeName;
    }

    /** 
     * Set string representation of the type of collection elements.
     * @param elementTypeName a string representation of the type of elements in
     * the collection. 
     */
    public void setElementTypeName(String elementTypeName) {
        this.elementTypeName = elementTypeName;
    }
    
}