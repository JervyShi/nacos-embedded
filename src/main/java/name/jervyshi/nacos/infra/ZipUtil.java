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

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import name.jervyshi.nacos.exception.NacosEmbeddedException;

/**
 * The type Zip util.
 * @author JervyShi
 * @version $Id : ZipUtil.java, v 0.1 2019-02-09 17:28 JervyShi Exp $$
 */
public class ZipUtil {

    /**
     * Unzip.
     *
     * @param zipFilePath the zip file path 
     * @param unzipLocation the unzip location
     */
    public static void unzip(final String zipFilePath, final String unzipLocation) {

        try {
            if (!(Files.exists(Paths.get(unzipLocation)))) {
                Files.createDirectories(Paths.get(unzipLocation));
            }
            try (ZipInputStream zipInputStream = new ZipInputStream(
                new FileInputStream(zipFilePath))) {
                ZipEntry entry = zipInputStream.getNextEntry();
                while (entry != null) {
                    Path filePath = Paths.get(unzipLocation, entry.getName());
                    if (!entry.isDirectory()) {
                        unzipFiles(zipInputStream, filePath);
                    } else {
                        Files.createDirectories(filePath);
                    }

                    zipInputStream.closeEntry();
                    entry = zipInputStream.getNextEntry();
                }
            }
        } catch (IOException e) {
            throw new NacosEmbeddedException("unzip files error", e);
        }
    }

    /**
     * Unzip files.
     *
     * @param zipInputStream the zip input stream 
     * @param unzipFilePath the unzip file path 
     * @throws IOException the io exception
     */
    public static void unzipFiles(final ZipInputStream zipInputStream,
                                  final Path unzipFilePath) throws IOException {

        try (BufferedOutputStream bos = new BufferedOutputStream(
            new FileOutputStream(unzipFilePath.toFile()))) {
            byte[] bytesIn = new byte[1024];
            int read;
            while ((read = zipInputStream.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        }

    }
}
