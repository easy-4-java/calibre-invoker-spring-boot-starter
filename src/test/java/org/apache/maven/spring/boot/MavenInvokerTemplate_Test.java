/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.maven.spring.boot;

import java.io.File;
import java.util.List;

import org.apache.maven.shared.invoker.InvocationOutputHandler;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MavenInvokerTemplate_Test {

    @Test
    void buildsInstallDeployAndExecuteRequestsWithoutLaunchingMaven() throws Exception {
        InvocationOutputHandler output = mock(InvocationOutputHandler.class);
        InvocationOutputHandler error = mock(InvocationOutputHandler.class);
        Invoker invoker = mock(Invoker.class);
        InvocationResult result = mock(InvocationResult.class);
        when(invoker.execute(any(InvocationRequest.class))).thenReturn(result);
        CalibreInvokerTemplate template = new CalibreInvokerTemplate(output, error, invoker,
                new CalibreInvokerProperties());

        assertSame(result, template.install("work", "book.jar", "example", "book", "1.0", "jar", true, false));
        assertSame(result, template.deploy("work", "book.jar", "example", "book", "1.0", "jar",
                "https://repo.example.test", "releases"));
        assertSame(result, template.execute(new File("work"), "clean", "verify"));

        ArgumentCaptor<InvocationRequest> requests = ArgumentCaptor.forClass(InvocationRequest.class);
        verify(invoker, times(3)).execute(requests.capture());
        InvocationRequest install = requests.getAllValues().get(0);
        assertEquals(new File("work"), install.getBaseDirectory());
        assertEquals(List.of("install:install-file", "-Dfile=book.jar", "-DgroupId=example",
                "-DartifactId=book", "-Dversion=1.0", "-Dpackaging=jar",
                "-DgeneratePom=true", "-DcreateChecksum=false"), install.getGoals());
        assertSame(output, install.getOutputHandler(null));
        assertSame(error, install.getErrorHandler(null));

        InvocationRequest deploy = requests.getAllValues().get(1);
        assertEquals(new File("work"), deploy.getBaseDirectory());
        assertEquals(List.of("deploy:deploy-file", "-DgroupId=example", "-DartifactId=book",
                "-Dversion=1.0", "-Dpackaging=jar", "-Dfile=book.jar",
                "-Durl=https://repo.example.test", "-DrepositoryId=releases"), deploy.getGoals());
        InvocationRequest execute = requests.getAllValues().get(2);
        assertEquals(new File("work"), execute.getBaseDirectory());
        assertEquals(List.of("clean", "verify"), execute.getGoals());
    }

    @Test
    void convenienceOverloadsLeaveBaseDirectoryUnset() throws Exception {
        Invoker invoker = mock(Invoker.class);
        when(invoker.execute(any(InvocationRequest.class))).thenReturn(mock(InvocationResult.class));
        CalibreInvokerTemplate template = new CalibreInvokerTemplate(null, null, invoker,
                new CalibreInvokerProperties());
        template.install("book.jar", "example", "book", "1.0", "jar", false, true);
        template.deploy("book.jar", "example", "book", "1.0", "jar", "https://repo.example.test", "releases");
        template.execute("work", "test");
        ArgumentCaptor<InvocationRequest> requests = ArgumentCaptor.forClass(InvocationRequest.class);
        verify(invoker, times(3)).execute(requests.capture());
        assertNull(requests.getAllValues().get(0).getBaseDirectory());
        assertNull(requests.getAllValues().get(1).getBaseDirectory());
        assertEquals(new File("work"), requests.getAllValues().get(2).getBaseDirectory());
    }
}
