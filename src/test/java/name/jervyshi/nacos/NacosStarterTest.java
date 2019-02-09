package name.jervyshi.nacos;

import org.junit.Test;

/**
 * The type Nacos starter test.
 * @author JervyShi
 * @version $Id : NacosStarterTest.java, v 0.1 2019-02-09 17:36 JervyShi Exp $$
 */
public class NacosStarterTest {

    /**
     * Start.
     */
    @Test
    public void start() throws Exception {
        NacosStarter nacosStarter = NacosStarterBuilder.nacosStarter().withNacosVersion("0.8.0")
            .build();
        NacosProcess start = nacosStarter.start();
        
        start.close();
    }
}