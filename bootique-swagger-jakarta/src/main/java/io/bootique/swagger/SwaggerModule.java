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

import io.bootique.BQCoreModule;
import io.bootique.ConfigModule;
import io.bootique.config.ConfigurationFactory;
import io.bootique.di.Binder;
import io.bootique.di.Provides;
import io.bootique.di.TypeLiteral;
import io.bootique.jersey.JerseyModule;
import io.bootique.jersey.MappedResource;
import io.bootique.log.BootLogger;
import io.bootique.swagger.command.GenerateSpecCommand;
import io.bootique.swagger.factory.SwaggerServiceFactory;
import io.bootique.swagger.web.SwaggerApi;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverters;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Set;

public class SwaggerModule extends ConfigModule {

    /**
     * @since 2.0.B1
     */
    public static SwaggerModuleExtender extend(Binder binder) {
        return new SwaggerModuleExtender(binder);
    }

    @Override
    public void configure(Binder binder) {
        JerseyModule.extend(binder).addMappedResource(new TypeLiteral<MappedResource<SwaggerApi>>() {
        });

        SwaggerModule.extend(binder).initAllExtensions();
        BQCoreModule.extend(binder).addCommand(GenerateSpecCommand.class);
    }

    @Provides
    @Singleton
    GenerateSpecCommand provideGenerateSpecCommand(BootLogger bootLogger, Provider<SwaggerService> service) {
        return new GenerateSpecCommand(bootLogger, service);
    }

    @Provides
    @Singleton
    MappedResource<SwaggerApi> provideOpenApiResource(SwaggerService service) {
        var swaggerApi = new SwaggerApi(service);
        return new MappedResource<>(swaggerApi, service.getUrlPatterns());
    }

    @Provides
    @Singleton
    SwaggerService provideSwaggerService(ConfigurationFactory configFactory,
                                         Set<ModelConverter> converters) {
        // side effect - installing converters
        // TODO: suggest Swagger to tie converters to contexts instead of using static ModelConverters
        converters.forEach(SwaggerModule::installConverter);
        var config = config(SwaggerServiceFactory.class, configFactory);
        return config.createSwaggerService();
    }

    private static void installConverter(ModelConverter converter) {
        // since ModelConverters is a static singleton, lets at least make an attempt
        // to prevent multiple registrations of the same converter
        if (!ModelConverters.getInstance().getConverters().contains(converter)) {
            ModelConverters.getInstance().addConverter(converter);
        }
    }

}
