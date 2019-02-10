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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import name.jervyshi.nacos.exception.NacosEmbeddedException;
import name.jervyshi.nacos.infra.NacosVersionGetter;

/**
 * The type Nacos starter builder.
 * @author JervyShi
 * @version $Id : NacosStarterBuilder.java, v 0.1 2019-02-08 17:23 JervyShi Exp $$
 */
public class NacosStarterBuilder {

    private Path              downloadPath;

    private String            nacosVersion;

    private String            host;

    private NacosPortsBuilder nacosPortsBuilder = NacosPortsBuilder.nacosPorts();

    private NacosStarterBuilder() {
    }

    /**
     * Nacos starter nacos starter builder.
     *
     * @return the nacos starter builder
     */
    public static NacosStarterBuilder nacosStarter() {
        return new NacosStarterBuilder();
    }

    /**
     * With download path nacos starter builder.
     *
     * @param downloadPath the download path   
     * @return the nacos starter builder
     */
    public NacosStarterBuilder withDownloadPath(Path downloadPath) {
        this.downloadPath = downloadPath;
        return this;
    }

    /**
     * With nacos version nacos starter builder.
     *
     * @param nacosVersion the nacos version  
     * @return the nacos starter builder
     */
    public NacosStarterBuilder withNacosVersion(String nacosVersion) {
        this.nacosVersion = nacosVersion;
        return this;
    }

    public NacosStarterBuilder withHost(String host) {
        this.host = host;
        return this;
    }

    /**
     * With server port nacos starter builder.
     *
     * @param serverPort the server port 
     * @return the nacos starter builder
     */
    public NacosStarterBuilder withServerPort(int serverPort) {
        this.nacosPortsBuilder.withServerPort(serverPort);
        return this;
    }

    /**
     * Build nacos starter.
     *
     * @return the nacos starter
     */
    public NacosStarter build() {
        applyDefaults();
        return new NacosStarter(this.nacosVersion, this.downloadPath, this.host,
            this.nacosPortsBuilder.build());
    }

    private void applyDefaults() {
        try {
            if (this.nacosVersion == null || this.nacosVersion.equals("")
                || this.nacosVersion.equals("latest")) {
                this.nacosVersion = NacosVersionGetter.getLatestVersion();
            }
            if (downloadPath == null) {
                this.downloadPath = Paths.get(Files.createTempDirectory("").getParent().toString(),
                    "nacos-embedded-" + this.nacosVersion);
            }
            if (this.host == null || this.host.equals("")) {
                this.host = "127.0.0.1";
            }
            if (!Files.exists(downloadPath)) {
                Files.createDirectories(downloadPath);
            }
        } catch (IOException e) {
            throw new NacosEmbeddedException("apply default settings error", e);
        }
    }
}
