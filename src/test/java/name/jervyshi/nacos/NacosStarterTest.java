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
package name.jervyshi.nacos;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import name.jervyshi.nacos.infra.NacosClient;

/**
 * The type Nacos starter test.
 * @author JervyShi
 * @version $Id : NacosStarterTest.java, v 0.1 2019-02-09 17:36 JervyShi Exp $$
 */
@RunWith(Parameterized.class)
public class NacosStarterTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays
            .asList(new Object[][] { { "0.8.0" }, { "0.9.0" }, { "1.0.0-RC2" }, { "latest" } });
    }

    @Parameter
    public String version;

    /**
     * Start.
     */
    @Test
    public void start() throws Exception {
        NacosStarter nacosStarter = NacosStarterBuilder.nacosStarter()
            .withNacosVersion(version)
            .withNacosTokenSecretKey("SecretKey012345678901234567890123456789012345678901234567890123456789")
            .build();
        NacosProcess process = nacosStarter.start();

        NacosClient client = new NacosClient(process.getHost(), process.getServerPort());
        assertTrue(client.isHealthy());

        process.close();
        assertFalse(client.isHealthy());
    }
}