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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.jervyshi.nacos.exception.NacosEmbeddedException;
import name.jervyshi.nacos.infra.NacosBinaryDownloader;
import name.jervyshi.nacos.infra.NacosWaiter;
import name.jervyshi.nacos.infra.OsResolver;
import name.jervyshi.nacos.infra.ZipUtil;

/**
 * The type Nacos starter.
 *
 * @author JervyShi
 * @version $Id : NacosStarter.java, v 0.1 2019-02-08 17:18 JervyShi Exp $$
 */
public class NacosStarter {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(NacosStarter.class);

    private String nacosVersion;

    private Path downloadPath;

    private Path nacosHomePath;

    private AtomicBoolean started;

    private NacosProcess nacosProcess;

    private String host;

    private NacosPorts nacosPorts;

    /**
     * Instantiates a new Nacos starter.
     *
     * @param nacosVersion the nacos version
     * @param downloadPath the download path
     */
    NacosStarter(String nacosVersion, Path downloadPath, String host, NacosPorts nacosPorts) {
        this.nacosVersion = nacosVersion;
        this.downloadPath = downloadPath;
        this.host = host;
        this.nacosPorts = nacosPorts;
        this.started = new AtomicBoolean(false);
    }

    /**
     * Start nacos process.
     *
     * @return the nacos process
     */
    public NacosProcess start() {
        Process innerProcess = null;
        try {
            logger.info("Start new nacos process.");
            checkInitialState();

            if (started.compareAndSet(false, true)) {
                nacosHomePath = Paths.get(downloadPath.toAbsolutePath().toString(),
                        String.valueOf(nacosPorts.getServerPort()), "nacos");
                if (!isBinaryDownloaded()) {
                    downloadAndUnpackBinary();
                }

                int serverPort = nacosPorts.getServerPort();

                List<String> command = new ArrayList<>();
                if (isWindows()) {
                    command.add(Paths.get(System.getenv("JAVA_HOME"), "bin", "java.exe")
                            .toAbsolutePath().toString());
                } else {
                    command.add(Paths.get(System.getenv("JAVA_HOME"), "bin", "java")
                            .toAbsolutePath().toString());
                }
                command.add("-Dnacos.standalone=true");
                command.add("-Dnacos.home=" + nacosHomePath.toAbsolutePath().toString());
                command.add("-Dserver.port=" + serverPort);
                command.add("-jar");
                command.add(getAbsolutePath("target", "nacos-server.jar"));
                command.add(
                        "--spring.config.location=classpath:/,optional:classpath:/config/,optional:file:./,optional:file:./config/,optional:file:"
                                + getAbsolutePath("conf") + File.separator);
                command.add("--logging.config=" + getAbsolutePath("conf", "nacos-logback.xml"));

                innerProcess = new ProcessBuilder().directory(nacosHomePath.toFile())
                        .command(command).inheritIO().redirectOutput(Redirect.PIPE).start();

                nacosProcess = new NacosProcess(host, nacosPorts, innerProcess);

                logger.info("Starting nacos server on port: {}", serverPort);
                new NacosWaiter(host, serverPort).avoidUntilNacosServerStarted();
                logger.info("Nacos server started");
            }
        } catch (NacosEmbeddedException e) {
            // Log standard output and error stream
            try {
                List<String> outputLines = IOUtils.readLines(innerProcess.getInputStream(), "UTF-8");
                outputLines.forEach(System.err::println);
                List<String> errorLines = IOUtils.readLines(innerProcess.getErrorStream(), "UTF-8");
                errorLines.forEach(System.err::println);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IOException e) {
            logger.error("fail to start nacos process, nacosVersion: {}, downloadPath:{}",
                    nacosVersion, downloadPath, e);
            // Print error stream if there is any.
            throw new NacosEmbeddedException("fail to start nacos process");
        }
        return nacosProcess;
    }

    private void downloadAndUnpackBinary() throws IOException {
        Path filePath = getDownloadFilePath();
        // reuse when same zip file already downloaded
        if (!Files.exists(filePath)) {
            logger.info("Download nacos archive to {}", filePath);
            NacosBinaryDownloader.getNacosBinaryArchive(nacosVersion, filePath);
        }

        String unzipLocation = Paths.get(downloadPath.toAbsolutePath().toString(),
                String.valueOf(nacosPorts.getServerPort())).toAbsolutePath().toString();
        logger.info("Unzip nacos archive files into: {}", unzipLocation);

        ZipUtil.unzip(filePath.toAbsolutePath().toString(), unzipLocation);
    }

    private boolean isWindows() {
        return OsResolver.resolve().contains("windows");
    }

    private boolean isBinaryDownloaded() {
        return getNacosServerJar().exists();
    }

    private Path getDownloadFilePath() {
        return Paths.get(downloadPath.toAbsolutePath().toString(),
                String.format("nacos-server-%s.zip", nacosVersion));
    }

    private File getNacosServerJar() {
        Path path = Paths.get(nacosHomePath.toAbsolutePath().toString(), "target",
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
        return Paths.get(nacosHomePath.toAbsolutePath().toString(), item).toString();
    }
}
