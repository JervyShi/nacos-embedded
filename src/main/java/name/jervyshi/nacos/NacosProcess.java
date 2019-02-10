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

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import name.jervyshi.nacos.infra.NacosWaiter;

/**
 * The type Nacos process.
 * @author JervyShi
 * @version $Id : NacosProcess.java, v 0.1 2019-02-09 18:17 JervyShi Exp $$
 */
public class NacosProcess implements AutoCloseable {

    /** logger */
    private static final Logger logger = getLogger(NacosProcess.class);

    private String              host;

    private int                 port;

    private Process             process;

    /**
     * Instantiates a new Nacos process.
     *
     * @param host the host  
     * @param port the port
     */
    public NacosProcess(String host, int port, Process process) {
        this.host = host;
        this.port = port;
        this.process = process;
    }

    @Override
    public void close() throws Exception {
        logger.info("Stopping nacos server");
        process.destroy();
        if (new NacosWaiter(host, port).avoidUntilNacosServerStopped()) {
            logger.info("Stopped nacos server");
        } else {
            logger.warn("Can not stop nacos server");
        }
    }
}
