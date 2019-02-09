package name.jervyshi.nacos;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

                File scriptFile = getNacosStartUpScript();

                List<String> command = new ArrayList<>();
                command.add(scriptFile.getAbsolutePath());
                if (!isWindows()) {
                    command.add("-m");
                    command.add("standalone");
                }
                Process innerProcess = new ProcessBuilder().directory(downloadPath.toFile())
                    .command(command).inheritIO().redirectOutput(Redirect.PIPE).start();

                // TODO log process result
                innerProcess.getInputStream();

                nacosProcess = new NacosProcess(downloadPath, getNacosShutdownScript(), host, port,
                    innerProcess);

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

        if (!isWindows()) {
            Set<PosixFilePermission> permissions = new HashSet<>();
            permissions.add(PosixFilePermission.OWNER_READ);
            permissions.add(PosixFilePermission.OWNER_WRITE);
            permissions.add(PosixFilePermission.OWNER_EXECUTE);
            permissions.add(PosixFilePermission.GROUP_READ);
            permissions.add(PosixFilePermission.OTHERS_READ);

            Files.setPosixFilePermissions(getNacosStartUpScript().toPath(), permissions);
            Files.setPosixFilePermissions(getNacosShutdownScript().toPath(), permissions);
        }
    }

    private boolean isWindows() {
        return OsResolver.resolve().contains("windows");
    }

    private boolean isBinaryDownloaded() {
        return getNacosStartUpScript().exists();
    }

    private File getNacosStartUpScript() {
        String scriptName = isWindows() ? "startup.cmd" : "startup.sh";
        Path path = Paths.get(downloadPath.toAbsolutePath().toString(), "nacos", "bin", scriptName);
        return path.toFile();
    }

    private File getNacosShutdownScript() {
        String scriptName = isWindows() ? "shutdown.cmd" : "shutdown.sh";
        Path path = Paths.get(downloadPath.toAbsolutePath().toString(), "nacos", "bin", scriptName);
        return path.toFile();
    }

    private void checkInitialState() {
        if (started.get()) {
            throw new NacosEmbeddedException(
                "This Nacos Starter already started nacos server. Create new Nacos Server Instance");
        }
    }

}
