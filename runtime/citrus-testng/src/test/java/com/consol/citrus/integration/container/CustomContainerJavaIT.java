/*
 * Copyright 2006-2016 the original author or authors.
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

package com.consol.citrus.integration.container;

import com.consol.citrus.TestActionContainerBuilder;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.container.AbstractActionContainer;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.testng.TestNGCitrusSupport;
import org.testng.annotations.Test;

import static com.consol.citrus.AbstractTestContainerBuilder.container;
import static com.consol.citrus.actions.CreateVariablesAction.Builder.createVariable;
import static com.consol.citrus.actions.EchoAction.Builder.echo;

/**
 * @author Christoph Deppisch
 * @since 2.6
 */
@Test
public class CustomContainerJavaIT extends TestNGCitrusSupport {

    @CitrusTest
    public void shouldExecuteReverseContainer() {
        run(reverse().actions(
            echo("${text}"),
            echo("Does it work?"),
            createVariable("text", "Yes it works!")
        ));
    }

    public TestActionContainerBuilder<ReverseActionContainer, ?> reverse() {
        return container(new ReverseActionContainer());
    }

    private static class ReverseActionContainer extends AbstractActionContainer {
        @Override
        public void doExecute(TestContext context) {
            for (int i = getActions().size(); i > 0; i--) {
                getActions().get(i - 1).execute(context);
            }
        }
    }
}
