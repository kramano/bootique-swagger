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

import io.bootique.ConfigModule;
import io.bootique.config.ConfigurationFactory;
import io.bootique.di.Binder;
import io.bootique.di.Provides;
import io.bootique.di.TypeLiteral;
import io.bootique.jersey.JerseyModule;
import io.bootique.jersey.MappedResource;
import io.bootique.type.TypeRef;
import org.glassfish.jersey.server.ResourceConfig;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Map;

public class SwaggerOpenapiModule extends ConfigModule {

    @Override
    public void configure(Binder binder) {
        JerseyModule.extend(binder).addMappedResource(new TypeLiteral<MappedResource<SwaggerOpenapiApi>>() {
        });
    }

    @Provides
    @Singleton
    MappedResource<SwaggerOpenapiApi> provideOpenApiResource(
            ConfigurationFactory configFactory,
            Provider<ResourceConfig> appProvider) {

        Map<String, OpenApiModelFactory> configs = config(new TypeRef<Map<String, OpenApiModelFactory>>() {
        }, configFactory);

        return new SwaggerOpenapiApiFactory(configs).createResource(appProvider);
    }
}
