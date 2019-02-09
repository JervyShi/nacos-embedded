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
package name.jervyshi.nacos.infra;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.jervyshi.nacos.exception.NacosEmbeddedException;

/**
 *
 * @author JervyShi
 * @version $Id: NacosWaiter.java, v 0.1 2019-02-09 18:48 JervyShi Exp $$
 */
public class NacosWaiter {

    /** logger */
    private static final Logger logger                       = LoggerFactory
                                                                 .getLogger(NacosWaiter.class);

    private static final int    DEFAULT_WAIT_TIME_IN_SECONDS = 30;

    private NacosClient         nacosClient;

    private String              host;

    private int                 port;

    private long                timeoutMillis;

    public NacosWaiter(String host, int port) {
        this(host, port, DEFAULT_WAIT_TIME_IN_SECONDS);
    }

    public NacosWaiter(String host, int port, int timeoutMillis) {
        this.host = host;
        this.port = port;
        this.timeoutMillis = TimeUnit.SECONDS.toMillis(timeoutMillis);
        this.nacosClient = new NacosClient(host, port);
    }

    public void avoidUntilNacosServerStarted() {
        long startTime = System.currentTimeMillis();

        boolean healthy;

        while (!(healthy = nacosClient.isHealthy()) && !isTimedOut(startTime)) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                logger.error("avoid sleep error", e);
            }
        }

        if (!healthy) {
            throw new NacosEmbeddedException(
                String.format("Can not start nacos server in %d seconds",
                    TimeUnit.MILLISECONDS.toSeconds(timeoutMillis)));
        }
    }

    public boolean avoidUntilNacosServerStopped() {
        long startTime = System.currentTimeMillis();
        while (!isTimedOut(startTime)) {
            try (Socket ignored = new Socket(host, port)) {
                Thread.sleep(100);
            } catch (IOException ignore) {
                return true;
            } catch (InterruptedException e) {
            }
        }

        return false;
    }

    private boolean isTimedOut(long startTime) {
        return System.currentTimeMillis() - startTime >= timeoutMillis;
    }
}
