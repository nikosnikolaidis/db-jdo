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

package org.apache.jdo.model;

/**
 * This exception indicates a problem during model update.
 *
 * @author Michael Bouschen
 * @since JDO 1.0.1
 */
public class ModelVetoException 
    extends ModelException
{
    /**
     * Creates new <code>ModelVetoException</code> without detail message.
     */
    public ModelVetoException() 
    {
    }
    
    /**
     * Constructs a <code>ModelVetoException</code> with the specified
     * detail message.
     * @param msg the detail message.
     */
    public ModelVetoException(String msg)
    {
        super(msg);
    }

    /** 
     * Constructs a new <code>ModelVetoException</code> with the specified
     * cause.
     * @param cause the cause <code>Throwable</code>.
     */
    public ModelVetoException(Throwable cause) 
    {
        super("", cause);
    }

    /** 
     * Constructs a new <code>ModelVetoException</code> with the specified
     * detail message and cause.
     * @param msg the detail message.
     * @param cause the cause <code>Throwable</code>.
     */
    public ModelVetoException(String msg, Throwable cause) 
    {
        super(msg, cause);
    }

}