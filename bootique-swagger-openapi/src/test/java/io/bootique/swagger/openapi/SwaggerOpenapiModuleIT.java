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

package io.bootique.swagger.openapi;

import io.bootique.jersey.JerseyModule;
import io.bootique.test.junit.BQTestFactory;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class SwaggerOpenapiModuleIT {

    @ClassRule
    public static final BQTestFactory TEST_FACTORY = new BQTestFactory();
    private static final WebTarget target = ClientBuilder.newClient().target("http://127.0.0.1:8080/");

    @BeforeClass
    public static void beforeClass() {
        TEST_FACTORY.app("-s")
                .module(new SwaggerOpenapiModuleProvider())
                .module(b -> JerseyModule.extend(b).addResource(TestApi.class))
                .run();
    }

    @Test
    public void testApi_Yaml() {

        Response r = target.path("/openapi.yaml").request().get();
        assertEquals(200, r.getStatus());
        OpenApiAsserts.assertEqualsToResource(r.readEntity(String.class), "response1.yml", "response1_alt.yml");
    }

    @Test
    public void testApi_Json() {

        Response r = target.path("/openapi.json").request().get();
        assertEquals(200, r.getStatus());
        OpenApiAsserts.assertEqualsToResource(r.readEntity(String.class), "response1.json", "response1_alt.json");
    }

    @OpenAPIDefinition
    @Path("/")
    public static class TestApi {

        @GET
        public String get() {
            return "get_";
        }

        @GET
        @Path("/sub/{id}")
        public String subget(@PathParam("id") int id) {
            return "get_" + id;
        }
    }
}
