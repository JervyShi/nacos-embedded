package name.jervyshi.nacos;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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

    private Path                downloadPath;

    private File                shutdownScript;

    private String              host;

    private int                 port;

    private Process             process;

    /**
     * Instantiates a new Nacos process.
     *
     * @param downloadPath the download path 
     * @param shutdownScript the shutdown script 
     * @param host the host  
     * @param port the port
     */
    public NacosProcess(Path downloadPath, File shutdownScript, String host, int port,
                        Process process) {
        this.downloadPath = downloadPath;
        this.shutdownScript = shutdownScript;
        this.host = host;
        this.port = port;
        this.process = process;
    }

    @Override
    public void close() throws Exception {
        logger.info("Stopping nacos server");
        process.destroy();
        List<String> command = new ArrayList<>();
        command.add(shutdownScript.getAbsolutePath());
        Process innerProcess = new ProcessBuilder().directory(downloadPath.toFile())
            .command(command).inheritIO().redirectOutput(Redirect.PIPE).start();

        // TODO log process result
        innerProcess.getInputStream();
        if (new NacosWaiter(host, port).avoidUntilNacosServerStopped()) {
            logger.info("Stopped nacos server");
        } else {
            logger.warn("Can not stop nacos server");
        }
    }
}
