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

    private Path   downloadPath;

    private String nacosVersion;

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

    /**
     * Build nacos starter.
     *
     * @return the nacos starter
     */
    public NacosStarter build() {
        applyDefaults();
        return new NacosStarter(this.nacosVersion, this.downloadPath);
    }

    private void applyDefaults() {
        try {
            if (this.nacosVersion == null || this.nacosVersion.equals("")) {
                this.nacosVersion = NacosVersionGetter.getLatestVersion();
            }
            if (downloadPath == null) {
                downloadPath = Paths.get(Files.createTempDirectory("").getParent().toString(),
                    "nacos-embedded-" + this.nacosVersion);
            }
            if (!Files.exists(downloadPath)) {
                Files.createDirectories(downloadPath);
            }
        } catch (IOException e) {
            throw new NacosEmbeddedException("apply default settings error", e);
        }
    }
}
