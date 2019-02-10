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
package name.jervyshi.nacos.exception;

/**
 * The type Nacos embedded exception.
 * @author JervyShi
 * @version $Id : NacosEmbeddedException.java, v 0.1 2019-02-08 21:44 JervyShi Exp $$
 */
public class NacosEmbeddedException extends RuntimeException {

    /**
     * Instantiates a new Nacos embedded exception.
     */
    public NacosEmbeddedException() {
        super();
    }

    /**
     * Instantiates a new Nacos embedded exception.
     *
     * @param message the message
     */
    public NacosEmbeddedException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Nacos embedded exception.
     *
     * @param message the message 
     * @param cause the cause
     */
    public NacosEmbeddedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Nacos embedded exception.
     *
     * @param cause the cause
     */
    public NacosEmbeddedException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Nacos embedded exception.
     *
     * @param message the message 
     * @param cause the cause 
     * @param enableSuppression the enable suppression 
     * @param writableStackTrace the writable stack trace
     */
    protected NacosEmbeddedException(String message, Throwable cause, boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
