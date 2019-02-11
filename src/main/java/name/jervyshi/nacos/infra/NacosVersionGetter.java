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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * The type Nacos version getter.
 * @author JervyShi
 * @version $Id : NacosVersionGetter.java, v 0.1 2019-02-09 12:48 JervyShi Exp $$
 */
public class NacosVersionGetter {

    /**
     * The constant NACOS_LATEST_URL.
     */
    public static final String  NACOS_LATEST_URL = "https://api.github.com/repos/alibaba/nacos/releases/latest";

    /** logger */
    private static final Logger logger           = LoggerFactory
        .getLogger(NacosVersionGetter.class);

    /**
     * Gets latest version.
     *
     * @return the latest version
     */
    public static String getLatestVersion() {
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(NACOS_LATEST_URL);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.addRequestProperty("User-Agent", "nacos embedded version getter");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            JSONObject jsonObject = JSON.parseObject(result.toString());
            return String.valueOf(jsonObject.get("tag_name"));
        } catch (Exception e) {
            logger.error("get nacos lastest version failed", e);
        }
        return null;
    }
}
