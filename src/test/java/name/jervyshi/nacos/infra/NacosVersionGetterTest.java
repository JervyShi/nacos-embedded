package name.jervyshi.nacos.infra;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * The type Nacos version getter test.
 * @author JervyShi
 * @version $Id : NacosVersionGetterTest.java, v 0.1 2019-02-11 14:56 JervyShi Exp $$
 */
public class NacosVersionGetterTest {

    /**
     * Gets latest version.
     */
    @Test
    public void getLatestVersion() {
        String version = NacosVersionGetter.getLatestVersion();
        assertNotNull(version);
    }
}