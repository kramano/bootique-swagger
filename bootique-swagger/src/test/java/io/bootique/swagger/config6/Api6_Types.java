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
package io.bootique.swagger.config6;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import java.time.*;

@Path("api6")
public class Api6_Types {

    @GET
    @Path("date/{date}")
    public Response getDate(@PathParam("date") LocalDate date) {
        return Response.ok().build();
    }

    @GET
    @Path("time/{time}")
    public Response getTime(@PathParam("time") LocalTime time) {
        return Response.ok().build();
    }

    @GET
    @Path("datetime/{datetime}")
    public Response getDateTime(@PathParam("datetime") LocalDateTime datetime) {
        return Response.ok().build();
    }

    @GET
    @Path("year/{year}")
    public Response getYear(@PathParam("year") Year year) {
        return Response.ok().build();
    }

    @GET
    @Path("yearmonth/{yearmonth}")
    public Response getYearMonth(@PathParam("yearmonth") YearMonth yearmonth) {
        return Response.ok().build();
    }
}
