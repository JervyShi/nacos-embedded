package name.jervyshi.nacos.infra;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        File file = getNacosBinaryArchive("0.8.0", Paths.get("/tmp", "nacos-tmp.zip"));
        System.out.println(file.getAbsolutePath());
    }
}
