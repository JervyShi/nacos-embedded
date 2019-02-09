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
