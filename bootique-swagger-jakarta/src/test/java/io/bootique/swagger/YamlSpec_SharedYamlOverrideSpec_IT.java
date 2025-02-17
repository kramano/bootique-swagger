/*
 * Licensed to ObjectStyle LLC under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ObjectStyle LLC licenses
 * this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.bootique.swagger;

import io.bootique.BQRuntime;
import io.bootique.Bootique;
import io.bootique.jetty.junit5.JettyTester;
import io.bootique.junit5.BQApp;
import io.bootique.junit5.BQTest;
import io.bootique.resource.ResourceFactory;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

@BQTest
public class YamlSpec_SharedYamlOverrideSpec_IT {

    static final JettyTester jetty = JettyTester.create();

    @BQApp
    static final BQRuntime app = Bootique
            .app("-s", "-c", "classpath:config4/startup.yml")
            .autoLoadModules()
            .module(jetty.moduleReplacingConnectors())
            .createRuntime();

    @Test
    public void testJson() {
        Response r1 = jetty.getTarget().path("/c1/model.json").request().get();
        JettyTester.assertOk(r1)
                .assertContent(new ResourceFactory("classpath:config4/response1.json"));

        Response r2 = jetty.getTarget().path("/c2/model.json").request().get();
        JettyTester.assertOk(r2)
                .assertContent(new ResourceFactory("classpath:config4/response2.json"));
    }
}
