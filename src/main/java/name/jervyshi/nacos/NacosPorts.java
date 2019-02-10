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

/**
 * The type Nacos ports.
 * @author JervyShi
 * @version $Id : NacosPorts.java, v 0.1 2019-02-10 22:36 JervyShi Exp $$
 */
public class NacosPorts {

    private int serverPort;

    /**
     * Instantiates a new Nacos ports.
     *
     * @param serverPort the server port
     */
    NacosPorts(int serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * Getter method for property <tt>serverPort</tt>.
     *
     * @return property value of serverPort
     */
    public int getServerPort() {
        return serverPort;
    }

}
