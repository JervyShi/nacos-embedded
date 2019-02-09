package name.jervyshi.nacos.infra;

/**
 * The type Os resolver.
 * @author JervyShi
 * @version $Id : OsResolver.java, v 0.1 2019-02-09 13:13 JervyShi Exp $$
 */
public class OsResolver {

    /**
     * Resolve string.
     *
     * @return the string
     */
    public static String resolve() {
        String os = System.getProperty("os.name").toLowerCase();
        String binaryVersion = "linux";
        if (os.contains("mac")) {
            binaryVersion = "darwin";
        } else if (os.contains("windows")) {
            binaryVersion = "windows";
        }

        return binaryVersion;
    }
}
