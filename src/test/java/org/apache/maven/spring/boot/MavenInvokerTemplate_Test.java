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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import org.apache.maven.model.Model;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationOutputHandler;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.apache.maven.shared.invoker.PrintStreamHandler;
import org.apache.maven.shared.invoker.SystemOutHandler;
import org.apache.maven.shared.invoker.SystemOutLogger;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@link CalibreInvokerTemplate}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class MavenInvokerTemplate_Test {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	InvocationOutputHandler outputHandler = new SystemOutHandler();
	InvocationOutputHandler errorHandler = new PrintStreamHandler(System.err, false);

	public Invoker mavenInvoker(CalibreInvokerProperties properties) {

		Invoker invoker = new DefaultInvoker();

		// Sets the handler used to capture the error output from the Maven build.
		invoker.setErrorHandler(errorHandler);
		// Sets the path to the base directory of the local repository to use for the
		// Maven invocation.
		if (properties.getLocalRepository() != null && !properties.getLocalRepository().isEmpty()) {
			File localRepositoryDirectory = new File(properties.getLocalRepository());
			if (localRepositoryDirectory.exists() && localRepositoryDirectory.isDirectory()) {
				invoker.setLocalRepositoryDirectory(localRepositoryDirectory);
			} else {
				localRepositoryDirectory.mkdir();
				invoker.setLocalRepositoryDirectory(localRepositoryDirectory);
			}
		} else {
			File localRepositoryDirectory = new File(System.getProperty("user.home"),
					".m2" + File.separator + "repository");
			if (!localRepositoryDirectory.exists()) {
				localRepositoryDirectory.mkdir();
			}
			invoker.setLocalRepositoryDirectory(localRepositoryDirectory);
		}
		// Sets the logger used by this invoker to output diagnostic messages.
		invoker.setLogger(new SystemOutLogger());
		//
		if (properties.getMavenExecutable() != null && !properties.getMavenExecutable().isEmpty()) {
			invoker.setMavenExecutable(new File(properties.getMavenExecutable()));
		}
		// Sets the path to the base directory of the Maven installation used to invoke
		// Maven.
		if (properties.getMavenHome() != null && !properties.getMavenHome().isEmpty()) {
			invoker.setMavenHome(new File(properties.getMavenHome()));
		}
		// Sets the handler used to capture the standard output from the Maven build.
		invoker.setOutputHandler(outputHandler);

		return invoker;
	}

	@Test
	public void testInstallWithMockedInvoker() throws MavenInvocationException {

		Invoker mockInvoker = mock(Invoker.class);
		InvocationResult mockResult = mock(InvocationResult.class);
		when(mockResult.getExitCode()).thenReturn(0);
		when(mockInvoker.execute(any(InvocationRequest.class))).thenReturn(mockResult);

		CalibreInvokerProperties properties = new CalibreInvokerProperties();
		properties.setNonPluginUpdates(true);
		properties.setUpdateSnapshots(false);

		CalibreInvokerTemplate template = new CalibreInvokerTemplate(outputHandler, errorHandler, mockInvoker,
				properties);

		InvocationResult result = template.install("/tmp/test.jar", "test.group", "test-artifact", "1.0.0", "jar", true, true);

		assertNotNull(result);
		assertEquals(0, result.getExitCode());
		verify(mockInvoker).execute(any(InvocationRequest.class));
	}

	@Test
	public void testInstallWithBasedirAndMockedInvoker() throws MavenInvocationException {

		Invoker mockInvoker = mock(Invoker.class);
		InvocationResult mockResult = mock(InvocationResult.class);
		when(mockResult.getExitCode()).thenReturn(0);
		when(mockInvoker.execute(any(InvocationRequest.class))).thenReturn(mockResult);

		CalibreInvokerProperties properties = new CalibreInvokerProperties();
		properties.setNonPluginUpdates(true);
		properties.setUpdateSnapshots(false);

		CalibreInvokerTemplate template = new CalibreInvokerTemplate(outputHandler, errorHandler, mockInvoker,
				properties);

		InvocationResult result = template.install("/tmp", "/tmp/test.jar", "test.group", "test-artifact", "1.0.0", "jar", true, true);

		assertNotNull(result);
		assertEquals(0, result.getExitCode());
		verify(mockInvoker).execute(any(InvocationRequest.class));
	}

	@Test
	public void testDeployWithMockedInvoker() throws MavenInvocationException {

		Invoker mockInvoker = mock(Invoker.class);
		InvocationResult mockResult = mock(InvocationResult.class);
		when(mockResult.getExitCode()).thenReturn(0);
		when(mockInvoker.execute(any(InvocationRequest.class))).thenReturn(mockResult);

		CalibreInvokerProperties properties = new CalibreInvokerProperties();

		CalibreInvokerTemplate template = new CalibreInvokerTemplate(outputHandler, errorHandler, mockInvoker,
				properties);

		InvocationResult result = template.deploy("/tmp/test.jar", "test.group", "test-artifact", "1.0.0", "jar",
				"http://localhost:8081/releases", "releases");

		assertNotNull(result);
		assertEquals(0, result.getExitCode());
		verify(mockInvoker).execute(any(InvocationRequest.class));
	}

	@Test
	public void testDeployWithBasedirAndMockedInvoker() throws MavenInvocationException {

		Invoker mockInvoker = mock(Invoker.class);
		InvocationResult mockResult = mock(InvocationResult.class);
		when(mockResult.getExitCode()).thenReturn(0);
		when(mockInvoker.execute(any(InvocationRequest.class))).thenReturn(mockResult);

		CalibreInvokerProperties properties = new CalibreInvokerProperties();

		CalibreInvokerTemplate template = new CalibreInvokerTemplate(outputHandler, errorHandler, mockInvoker,
				properties);

		InvocationResult result = template.deploy("/tmp", "/tmp/test.jar", "test.group", "test-artifact", "1.0.0", "jar",
				"http://localhost:8081/releases", "releases");

		assertNotNull(result);
		assertEquals(0, result.getExitCode());
		verify(mockInvoker).execute(any(InvocationRequest.class));
	}

	@Test
	public void testExecuteWithBasedirFileAndMockedInvoker() throws MavenInvocationException {

		Invoker mockInvoker = mock(Invoker.class);
		InvocationResult mockResult = mock(InvocationResult.class);
		when(mockResult.getExitCode()).thenReturn(0);
		when(mockInvoker.execute(any(InvocationRequest.class))).thenReturn(mockResult);

		CalibreInvokerProperties properties = new CalibreInvokerProperties();

		CalibreInvokerTemplate template = new CalibreInvokerTemplate(outputHandler, errorHandler, mockInvoker,
				properties);

		InvocationResult result = template.execute(new File("/tmp"), "clean", "install");

		assertNotNull(result);
		assertEquals(0, result.getExitCode());
		verify(mockInvoker).execute(any(InvocationRequest.class));
	}

	@Test
	public void testExecuteWithBasedirStringAndMockedInvoker() throws MavenInvocationException {

		Invoker mockInvoker = mock(Invoker.class);
		InvocationResult mockResult = mock(InvocationResult.class);
		when(mockResult.getExitCode()).thenReturn(0);
		when(mockInvoker.execute(any(InvocationRequest.class))).thenReturn(mockResult);

		CalibreInvokerProperties properties = new CalibreInvokerProperties();

		CalibreInvokerTemplate template = new CalibreInvokerTemplate(outputHandler, errorHandler, mockInvoker,
				properties);

		InvocationResult result = template.execute("/tmp", "clean", "install");

		assertNotNull(result);
		assertEquals(0, result.getExitCode());
		verify(mockInvoker).execute(any(InvocationRequest.class));
	}

	@Test
	public void testReadModel() throws Exception {
		// Create a minimal JAR with a pom.xml inside META-INF/maven/
		File jarFile = tempFolder.newFile("test-artifact-1.0.0.jar");
		try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFile))) {
			// Add pom.xml entry
			jos.putNextEntry(new JarEntry("META-INF/maven/io.github.easy4j/test-artifact/pom.xml"));
			String pomXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
					+ "<project>\n"
					+ "  <modelVersion>4.0.0</modelVersion>\n"
					+ "  <groupId>io.github.easy4j</groupId>\n"
					+ "  <artifactId>test-artifact</artifactId>\n"
					+ "  <version>1.0.0</version>\n"
					+ "</project>\n";
			jos.write(pomXml.getBytes());
			jos.closeEntry();
		}

		CalibreInvokerProperties properties = new CalibreInvokerProperties();
		Invoker invoker = new DefaultInvoker();
		CalibreInvokerTemplate template = new CalibreInvokerTemplate(outputHandler, errorHandler, invoker, properties);

		Model model = template.readModel(jarFile);
		assertNotNull(model);
		assertEquals("io.github.easy4j", model.getGroupId());
		assertEquals("test-artifact", model.getArtifactId());
		assertEquals("1.0.0", model.getVersion());
	}

	@Test
	public void testReadModelThrowsIOExceptionForEmptyJar() throws Exception {
		// Create a JAR with no pom.xml
		File jarFile = tempFolder.newFile("empty.jar");
		try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFile))) {
			jos.putNextEntry(new JarEntry("dummy.txt"));
			jos.write("dummy".getBytes());
			jos.closeEntry();
		}

		CalibreInvokerProperties properties = new CalibreInvokerProperties();
		Invoker invoker = new DefaultInvoker();
		CalibreInvokerTemplate template = new CalibreInvokerTemplate(outputHandler, errorHandler, invoker, properties);

		try {
			template.readModel(jarFile);
			fail("Expected IOException");
		} catch (IOException e) {
			assertNotNull(e.getMessage());
		}
	}

	@Test
	public void testConstructorAndFields() {
		Invoker mockInvoker = mock(Invoker.class);
		CalibreInvokerProperties properties = new CalibreInvokerProperties();

		CalibreInvokerTemplate template = new CalibreInvokerTemplate(outputHandler, errorHandler, mockInvoker, properties);
		assertNotNull(template);
	}

	@Test
	public void testMavenInvokerHelper() {
		CalibreInvokerProperties properties = new CalibreInvokerProperties();
		Invoker invoker = mavenInvoker(properties);
		assertNotNull(invoker);
	}

	@Test
	public void testMavenInvokerHelperWithCustomLocalRepository() {
		CalibreInvokerProperties properties = new CalibreInvokerProperties();
		properties.setLocalRepository(System.getProperty("java.io.tmpdir"));
		Invoker invoker = mavenInvoker(properties);
		assertNotNull(invoker);
	}

	@Test
	public void testMavenInvokerHelperWithCustomMavenExecutable() {
		CalibreInvokerProperties properties = new CalibreInvokerProperties();
		properties.setMavenExecutable("/usr/bin/mvn");
		Invoker invoker = mavenInvoker(properties);
		assertNotNull(invoker);
	}

	@Test
	public void testMavenInvokerHelperWithCustomMavenHome() {
		CalibreInvokerProperties properties = new CalibreInvokerProperties();
		properties.setMavenHome("/opt/maven");
		Invoker invoker = mavenInvoker(properties);
		assertNotNull(invoker);
	}

	@Test
	public void testMavenInvokerHelperWithNonExistentLocalRepo() {
		CalibreInvokerProperties properties = new CalibreInvokerProperties();
		String nonExistent = System.getProperty("java.io.tmpdir") + File.separator + "nonexistent-template-repo-" + System.currentTimeMillis();
		properties.setLocalRepository(nonExistent);
		Invoker invoker = mavenInvoker(properties);
		assertNotNull(invoker);
		// Cleanup
		new File(nonExistent).delete();
	}

}
