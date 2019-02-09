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
