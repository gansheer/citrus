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

package org.citrusframework.docker.command;

import com.github.dockerjava.api.model.ResponseItem;
import org.citrusframework.context.TestContext;
import org.citrusframework.docker.actions.DockerExecuteAction;
import org.citrusframework.docker.client.DockerClient;

/**
 * @since 2.4
 */
public class Ping extends AbstractDockerCommand<ResponseItem> {

    /**
     * Default constructor initializing the command name.
     */
    public Ping() {
        super("docker:ping");

        setCommandResult(new ResponseItem());
    }

    @Override
    public void execute(DockerClient dockerClient, TestContext context) {
        try (var command = dockerClient.getEndpointConfiguration().getDockerClient().pingCmd()) {
            command.exec();
        }

        setCommandResult(success());
    }

    /**
     * Command builder.
     */
    public static final class Builder extends AbstractDockerCommandBuilder<ResponseItem, Ping, Builder> {

        public Builder(DockerExecuteAction.Builder parent) {
            super(parent, new Ping());
        }
    }
}
