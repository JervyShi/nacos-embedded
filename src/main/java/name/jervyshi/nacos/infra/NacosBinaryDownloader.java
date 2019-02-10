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

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.jervyshi.nacos.exception.NacosEmbeddedException;

/**
 * The type Nacos binary downloader.
 * @author JervyShi
 * @version $Id : NacosBinaryDownloader.java, v 0.1 2019-02-09 13:41 JervyShi Exp $$
 */
public class NacosBinaryDownloader {

    /**
     * The constant NACOS_BINARY_URL.
     */
    public static final String  NACOS_BINARY_URL = "https://github.com/alibaba/nacos/releases/download/%s/nacos-server-%s.zip";

    /** logger */
    private static final Logger logger           = LoggerFactory
        .getLogger(NacosBinaryDownloader.class);

    /**
     * Gets nacos binary archive.
     *
     * @param version the version 
     * @param filePath the file path 
     * @return the nacos binary archive
     */
    public static File getNacosBinaryArchive(String version, Path filePath) {
        try {
            URL url = new URL(String.format(NACOS_BINARY_URL, version, version));
            InputStream inputStream = url.openStream();
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return filePath.toFile();
        } catch (Exception e) {
            logger.error("get nacos binary archive failed, version: {}, filePath: {}", version,
                filePath, e);
            throw new NacosEmbeddedException("get nacos binary archive failed", e);
        }
    }
}
