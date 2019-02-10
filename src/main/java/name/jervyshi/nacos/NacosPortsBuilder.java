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
package name.jervyshi.nacos;

import name.jervyshi.nacos.infra.Ports;

/**
 * The type Nacos ports builder.
 * @author JervyShi
 * @version $Id : NacosPortsBuilder.java, v 0.1 2019-02-10 22:38 JervyShi Exp $$
 */
public class NacosPortsBuilder {

    private int serverPort;

    /**
     * Nacos ports nacos ports builder.
     *
     * @return the nacos ports builder
     */
    public static NacosPortsBuilder nacosPorts() {
        return new NacosPortsBuilder();
    }

    /**
     * With server port nacos ports builder.
     *
     * @param serverPort the server port 
     * @return the nacos ports builder
     */
    public NacosPortsBuilder withServerPort(int serverPort) {
        this.serverPort = serverPort;
        return this;
    }

    /**
     * Build nacos ports.
     *
     * @return the nacos ports
     */
    public NacosPorts build() {
        return new NacosPorts(randomIfNotSet(this.serverPort));
    }

    private static int randomIfNotSet(int port) {
        return port > 0 ? port : Ports.nextAvailable();
    }
}
