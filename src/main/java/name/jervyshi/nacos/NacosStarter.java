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

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.jervyshi.nacos.exception.NacosEmbeddedException;
import name.jervyshi.nacos.infra.NacosBinaryDownloader;
import name.jervyshi.nacos.infra.NacosWaiter;
import name.jervyshi.nacos.infra.OsResolver;
import name.jervyshi.nacos.infra.ZipUtil;

/**
 * The type Nacos starter.
 * @author JervyShi
 * @version $Id : NacosStarter.java, v 0.1 2019-02-08 17:18 JervyShi Exp $$
 */
public class NacosStarter {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(NacosStarter.class);

    private String              nacosVersion;

    private Path                downloadPath;

    private AtomicBoolean       started;

    private NacosProcess        nacosProcess;

    private String              host;

    private int                 port;

    /**
     * Instantiates a new Nacos starter.
     *
     * @param nacosVersion the nacos version 
     * @param downloadPath the download path
     */
    public NacosStarter(String nacosVersion, Path downloadPath) {
        this.nacosVersion = nacosVersion;
        this.downloadPath = downloadPath;
        this.host = "127.0.0.1";
        // TODO need support change port from nacos starter builder
        this.port = 8848;
        this.started = new AtomicBoolean(false);
    }

    /**
     * Start nacos process.
     *
     * @return the nacos process
     */
    public NacosProcess start() {
        try {
            logger.info("Start new nacos process.");
            checkInitialState();

            if (started.compareAndSet(false, true)) {
                if (!isBinaryDownloaded()) {
                    downloadAndUnpackBinary();
                }

                List<String> command = new ArrayList<>();
                if (isWindows()) {
                    command.add(Paths.get(System.getenv("JAVA_HOME"), "bin", "java.exe")
                        .toAbsolutePath().toString());
                } else {
                    command.add(Paths.get(System.getenv("JAVA_HOME"), "bin", "java")
                        .toAbsolutePath().toString());
                }
                command.add("-Dnacos.standalone=true");
                command.add("-Dnacos.home=" + getAbsolutePath("nacos"));
                command.add("-jar");
                command.add(getAbsolutePath("nacos", "target", "nacos-server.jar"));
                command.add(
                    "--spring.config.location=classpath:/,classpath:/config/,file:./,file:./config/,file:"
                            + getAbsolutePath("nacos", "conf") + File.separator);
                command.add(
                    "--logging.config=" + getAbsolutePath("nacos", "conf", "nacos-logback.xml"));

                Process innerProcess = new ProcessBuilder()
                    .directory(Paths.get(getAbsolutePath("nacos")).toFile()).command(command)
                    .inheritIO().redirectOutput(Redirect.PIPE).start();

                // TODO log process result
                innerProcess.getInputStream();

                nacosProcess = new NacosProcess(host, port, innerProcess);

                logger.info("Starting nacos server on port: {}", port);
                new NacosWaiter(host, port).avoidUntilNacosServerStarted();
                logger.info("Nacos server started");
            }
        } catch (IOException e) {
            logger.error("fail to start nacos process, nacosVersion: {}, downloadPath:{}",
                nacosVersion, downloadPath, e);
            throw new NacosEmbeddedException("fail to start nacos process");
        }
        return nacosProcess;
    }

    private void downloadAndUnpackBinary() throws IOException {
        Path filePath = Paths.get(downloadPath.toAbsolutePath().toString(),
            String.format("nacos-server-%s.zip", nacosVersion));
        logger.info("Download nacos archive to {}", filePath);

        File archive = NacosBinaryDownloader.getNacosBinaryArchive(nacosVersion, filePath);

        logger.info("Unzip nacos archive files into: {}", downloadPath);
        ZipUtil.unzip(archive.getAbsolutePath(), downloadPath.toAbsolutePath().toString());
    }

    private boolean isWindows() {
        return OsResolver.resolve().contains("windows");
    }

    private boolean isBinaryDownloaded() {
        return getNacosServerJar().exists();
    }

    private File getNacosServerJar() {
        Path path = Paths.get(downloadPath.toAbsolutePath().toString(), "nacos", "target",
            "nacos-server.jar");
        return path.toFile();
    }

    private void checkInitialState() {
        if (started.get()) {
            throw new NacosEmbeddedException(
                "This Nacos Starter already started nacos server. Create new Nacos Server Instance");
        }
    }

    private String getAbsolutePath(String... item) {
        return Paths.get(downloadPath.toAbsolutePath().toString(), item).toString();
    }
}
