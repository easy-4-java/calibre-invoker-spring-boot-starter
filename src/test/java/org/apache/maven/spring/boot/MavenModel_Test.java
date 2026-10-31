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

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.maven.model.Model;
import org.apache.maven.shared.invoker.InvocationOutputHandler;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.InvokerLogger;
import org.apache.maven.spring.boot.options.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

class MavenModel_Test {

    @TempDir Path home;

    @Test
    void readsPomEmbeddedInJarAndRejectsJarWithoutPom() throws Exception {
        Path withPom = home.resolve("with-pom.jar");
        try (ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(withPom))) {
            zip.putNextEntry(new ZipEntry("META-INF/maven/example/book/pom.xml"));
            zip.write(("<project><modelVersion>4.0.0</modelVersion><groupId>example</groupId>"
                    + "<artifactId>book</artifactId><version>1.0</version></project>")
                    .getBytes(StandardCharsets.UTF_8));
            zip.closeEntry();
        }
        CalibreInvokerTemplate template = new CalibreInvokerTemplate(null, null, null,
                new CalibreInvokerProperties());
        Model model = template.readModel(withPom.toFile());
        assertEquals("example", model.getGroupId());
        assertEquals("book", model.getArtifactId());
        assertEquals("1.0", model.getVersion());

        Path withoutPom = home.resolve("without-pom.jar");
        try (ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(withoutPom))) {
            zip.putNextEntry(new ZipEntry("book.txt"));
            zip.write("content".getBytes(StandardCharsets.UTF_8));
            zip.closeEntry();
        }
        assertThrows(IOException.class, () -> template.readModel(withoutPom.toFile()));
    }

    @Test
    void createsInvokerAndTemplateFromConfiguration() {
        CalibreInvokerConfiguration configuration = new CalibreInvokerConfiguration();
        InvocationOutputHandler output = configuration.outputHandler();
        InvocationOutputHandler error = configuration.errorHandler();
        InvokerLogger logger = configuration.invokerLogger();
        CalibreInvokerProperties properties = new CalibreInvokerProperties();
        properties.setLocalRepository(home.resolve("repository").toString());
        Invoker invoker = configuration.mavenInvoker(output, error, logger, properties);
        assertNotNull(invoker);
        assertNotNull(configuration.mavenInvokerTemplate(output, error, invoker, properties));
        assertTrue(home.resolve("repository").toFile().isDirectory());
    }

    @Test
    void calibreOptionBeansExposeWritableBooleanFlags() throws Exception {
        Class<?>[] optionTypes = { EbookConvertOptions.class, EbookEditOptions.class,
                EbookMetaOptions.class, EbookPolishOptions.class, EbookViewerOptions.class,
                FetchEbookMetadataOptions.class, Lrf2lrsOptions.class,
                LrfviewerOptions.class, Lrs2lrfOptions.class, Web2diskOptions.class };
        int flags = 0;
        for (Class<?> type : optionTypes) {
            Object options = type.getDeclaredConstructor().newInstance();
            for (PropertyDescriptor property : Introspector.getBeanInfo(type).getPropertyDescriptors()) {
                if (property.getPropertyType() != boolean.class || property.getWriteMethod() == null
                        || property.getReadMethod() == null) continue;
                assertEquals(false, property.getReadMethod().invoke(options), type.getSimpleName() + "." + property.getName());
                property.getWriteMethod().invoke(options, true);
                assertEquals(true, property.getReadMethod().invoke(options), type.getSimpleName() + "." + property.getName());
                flags++;
            }
        }
        assertTrue(flags >= 20, "expected to check the Calibre option binding contract");
    }
}
