/*
 * Copyright the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.citrusframework.knative.integration;

import org.citrusframework.annotations.CitrusTest;
import org.citrusframework.http.endpoint.builder.HttpEndpoints;
import org.citrusframework.http.server.HttpServer;
import org.citrusframework.spi.BindToRegistry;
import org.citrusframework.util.SocketUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import static org.citrusframework.knative.actions.KnativeActionBuilder.knative;

public class ReceiveEventIT extends AbstractKnativeIT {

    private final int port = SocketUtils.findAvailableTcpPort(8081);

    @BindToRegistry
    public HttpServer knativeBroker = HttpEndpoints.http()
            .server()
            .port(port)
            .defaultStatus(HttpStatus.ACCEPTED)
            .autoStart(true)
            .build();

    @AfterClass(alwaysRun = true)
    public void shutdown() {
        knativeBroker.stop();
    }

    @Test
    @CitrusTest
    public void shouldReceiveEvents() {
        given(knative()
                .event()
                .send()
                .fork(true)
                .brokerUrl("http://localhost:%d".formatted(port))
                .eventData("Hello Knative broker"));

        then(
            knative()
                    .event()
                    .receive()
                    .server(knativeBroker)
                    .eventData("Hello Knative broker")
                    .attribute("ce-id", "@notNull()@")
                    .attribute("ce-type", "org.citrusframework.event.test")
                    .attribute("ce-source", "citrus-test")
                    .attribute("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
        );
    }

}
