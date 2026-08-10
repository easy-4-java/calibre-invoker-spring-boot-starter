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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.shared.invoker.InvocationOutputHandler;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationRequest.CheckSumPolicy;
import org.apache.maven.shared.invoker.InvocationRequest.ReactorFailureBehavior;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.InvokerLogger;
import org.apache.maven.spring.boot.options.EbookConvertOptions;
import org.apache.maven.spring.boot.options.EbookEditOptions;
import org.apache.maven.spring.boot.options.EbookMetaOptions;
import org.apache.maven.spring.boot.options.EbookPolishOptions;
import org.apache.maven.spring.boot.options.EbookViewerOptions;
import org.apache.maven.spring.boot.options.FetchEbookMetadataOptions;
import org.apache.maven.spring.boot.options.Lrf2lrsOptions;
import org.apache.maven.spring.boot.options.LrfviewerOptions;
import org.apache.maven.spring.boot.options.Lrs2lrfOptions;
import org.apache.maven.spring.boot.options.Web2diskOptions;
import org.junit.Test;

/**
 * Tests for {@link CalibreInvokerProperties} and all options POJOs.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class MavenInvoker_Test {

	// ==================== CalibreInvokerProperties tests ====================

	@Test
	public void testPropertiesDefaultValues() {
		CalibreInvokerProperties props = new CalibreInvokerProperties();

		assertFalse(props.isDebug());
		assertNull(props.getGlobalSettings());
		assertNull(props.getGlobalToolchains());
		assertEquals(CheckSumPolicy.Warn, props.getGlobalChecksumPolicy());
		assertNull(props.getJavaHome());
		assertNull(props.getLocalRepository());
		assertNull(props.getMavenExecutable());
		assertNull(props.getMavenHome());
		assertNull(props.getMavenOpts());
		assertNotNull(props.getMavenRepositorys());
		assertTrue(props.getMavenRepositorys().isEmpty());
		assertFalse(props.isNonPluginUpdates());
		assertFalse(props.isOffline());
		assertNull(props.getProperties());
		assertNull(props.getPomFilename());
		assertNull(props.getProfiles());
		assertNull(props.getProjects());
		assertEquals(ReactorFailureBehavior.FailFast, props.getReactorFailureBehavior());
		assertTrue(props.isRecursive());
		assertNull(props.getResumeFrom());
		assertTrue(props.isShellEnvironmentInherited());
		assertFalse(props.isShowErrors());
		assertFalse(props.isShowVersion());
		assertNull(props.getShellEnvironments());
		assertEquals(1, props.getThreads());
		assertFalse(props.isUpdateSnapshots());
		assertNull(props.getUserSettings());
		assertTrue(props.isAlsoMake());
		assertTrue(props.isAlsoMakeDependents());
		assertTrue(props.isBatchMode());
	}

	@Test
	public void testPropertiesSettersAndGetters() {
		CalibreInvokerProperties props = new CalibreInvokerProperties();

		props.setDebug(true);
		assertTrue(props.isDebug());

		props.setGlobalSettings("/path/to/global-settings.xml");
		assertEquals("/path/to/global-settings.xml", props.getGlobalSettings());

		props.setGlobalToolchains("/path/to/toolchains.xml");
		assertEquals("/path/to/toolchains.xml", props.getGlobalToolchains());

		props.setGlobalChecksumPolicy(CheckSumPolicy.Fail);
		assertEquals(CheckSumPolicy.Fail, props.getGlobalChecksumPolicy());

		props.setJavaHome("/path/to/java");
		assertEquals("/path/to/java", props.getJavaHome());

		props.setLocalRepository("/path/to/repo");
		assertEquals("/path/to/repo", props.getLocalRepository());

		props.setMavenExecutable("/path/to/mvn");
		assertEquals("/path/to/mvn", props.getMavenExecutable());

		props.setMavenHome("/path/to/maven");
		assertEquals("/path/to/maven", props.getMavenHome());

		props.setMavenOpts("-Xmx1024m");
		assertEquals("-Xmx1024m", props.getMavenOpts());

		Map<String, String> repos = new HashMap<>();
		repos.put("central", "https://repo.maven.apache.org/maven2");
		props.setMavenRepositorys(repos);
		assertEquals("https://repo.maven.apache.org/maven2", props.getMavenRepositorys().get("central"));

		props.setNonPluginUpdates(true);
		assertTrue(props.isNonPluginUpdates());

		props.setOffline(true);
		assertTrue(props.isOffline());

		Properties sysProps = new Properties();
		sysProps.setProperty("key", "value");
		props.setProperties(sysProps);
		assertEquals("value", props.getProperties().getProperty("key"));

		props.setPomFilename("custom-pom.xml");
		assertEquals("custom-pom.xml", props.getPomFilename());

		props.setProfiles(Arrays.asList("prod", "release"));
		assertEquals(2, props.getProfiles().size());

		props.setProjects(Arrays.asList("module-a", "module-b"));
		assertEquals(2, props.getProjects().size());

		props.setReactorFailureBehavior(ReactorFailureBehavior.FailAtEnd);
		assertEquals(ReactorFailureBehavior.FailAtEnd, props.getReactorFailureBehavior());

		props.setRecursive(false);
		assertFalse(props.isRecursive());

		props.setResumeFrom("module-a");
		assertEquals("module-a", props.getResumeFrom());

		props.setShellEnvironmentInherited(false);
		assertFalse(props.isShellEnvironmentInherited());

		props.setShowErrors(true);
		assertTrue(props.isShowErrors());

		props.setShowVersion(true);
		assertTrue(props.isShowVersion());

		Map<String, String> envs = new HashMap<>();
		envs.put("MAVEN_OPTS", "-Xmx512m");
		props.setShellEnvironments(envs);
		assertEquals("-Xmx512m", props.getShellEnvironments().get("MAVEN_OPTS"));

		props.setThreads(4);
		assertEquals(4, props.getThreads());

		props.setUpdateSnapshots(true);
		assertTrue(props.isUpdateSnapshots());

		props.setUserSettings("/path/to/user-settings.xml");
		assertEquals("/path/to/user-settings.xml", props.getUserSettings());

		props.setAlsoMake(false);
		assertFalse(props.isAlsoMake());

		props.setAlsoMakeDependents(false);
		assertFalse(props.isAlsoMakeDependents());

		props.setBatchMode(false);
		assertFalse(props.isBatchMode());
	}

	@Test
	public void testNewRequestWithDefaults() {
		CalibreInvokerProperties props = new CalibreInvokerProperties();
		InvocationRequest request = props.newRequest();

		assertNotNull(request);
		assertTrue(request.isBatchMode());
		assertFalse(request.isDebug());
		assertFalse(request.isNonPluginUpdates());
		assertFalse(request.isOffline());
		assertTrue(request.isRecursive());
		assertTrue(request.isShellEnvironmentInherited());
		assertFalse(request.isShowErrors());
		assertFalse(request.isShowVersion());
		assertFalse(request.isUpdateSnapshots());
	}

	@Test
	public void testNewRequestWithCustomLocalRepository() {
		CalibreInvokerProperties props = new CalibreInvokerProperties();
		String tempDir = System.getProperty("java.io.tmpdir");
		props.setLocalRepository(tempDir);

		InvocationRequest request = props.newRequest();
		assertNotNull(request);
	}

	@Test
	public void testNewRequestWithNonExistentLocalRepository() {
		CalibreInvokerProperties props = new CalibreInvokerProperties();
		String nonExistent = System.getProperty("java.io.tmpdir") + File.separator + "nonexistent-repo-" + System.currentTimeMillis();
		props.setLocalRepository(nonExistent);

		InvocationRequest request = props.newRequest();
		assertNotNull(request);
		// Cleanup
		new File(nonExistent).delete();
	}

	@Test
	public void testNewRequestWithAllSettings() {
		CalibreInvokerProperties props = new CalibreInvokerProperties();
		props.setGlobalSettings(System.getProperty("java.io.tmpdir"));
		props.setGlobalToolchains(System.getProperty("java.io.tmpdir"));
		props.setJavaHome(System.getProperty("java.home"));
		props.setLocalRepository(System.getProperty("java.io.tmpdir"));
		props.setMavenOpts("-Xmx1024m");
		props.setNonPluginUpdates(true);
		props.setOffline(true);
		props.setProfiles(Arrays.asList("prod"));
		props.setProjects(Arrays.asList("module-a"));
		props.setReactorFailureBehavior(ReactorFailureBehavior.FailAtEnd);
		props.setRecursive(false);
		props.setResumeFrom("module-a");
		props.setShellEnvironmentInherited(false);
		props.setShowErrors(true);
		props.setShowVersion(true);
		props.setThreads(4);
		props.setUpdateSnapshots(true);
		props.setUserSettings(System.getProperty("java.io.tmpdir"));
		props.setBatchMode(false);
		props.setDebug(true);

		Properties sysProps = new Properties();
		sysProps.setProperty("key", "value");
		props.setProperties(sysProps);

		Map<String, String> envs = new HashMap<>();
		envs.put("MAVEN_OPTS", "-Xmx512m");
		props.setShellEnvironments(envs);

		InvocationRequest request = props.newRequest();
		assertNotNull(request);
		assertFalse(request.isBatchMode());
		assertTrue(request.isDebug());
		assertTrue(request.isNonPluginUpdates());
		assertTrue(request.isOffline());
		assertFalse(request.isRecursive());
		assertFalse(request.isShellEnvironmentInherited());
		assertTrue(request.isShowErrors());
		assertTrue(request.isShowVersion());
		assertTrue(request.isUpdateSnapshots());
	}

	@Test
	public void testNewRequestWithGlobalChecksumPolicy() {
		CalibreInvokerProperties props = new CalibreInvokerProperties();
		props.setGlobalChecksumPolicy(CheckSumPolicy.Fail);

		InvocationRequest request = props.newRequest();
		assertNotNull(request);
	}

	@Test
	public void testNewRequestWithEmptyTextProperties() {
		CalibreInvokerProperties props = new CalibreInvokerProperties();
		// Set empty strings - should behave like null (StringUtils.hasText returns false)
		props.setGlobalSettings("");
		props.setGlobalToolchains("");
		props.setJavaHome("");
		props.setLocalRepository("");
		props.setMavenExecutable("");
		props.setMavenHome("");
		props.setMavenOpts("");
		props.setResumeFrom("");
		props.setUserSettings("");

		InvocationRequest request = props.newRequest();
		assertNotNull(request);
	}

	@Test
	public void testPrefixConstant() {
		assertEquals("maven.invoker", CalibreInvokerProperties.PREFIX);
	}

	// ==================== Options classes tests ====================

	@Test
	public void testEbookConvertOptions() {
		EbookConvertOptions options = new EbookConvertOptions();
		assertFalse(options.isDetach());
		options.setDetach(true);
		assertTrue(options.isDetach());
	}

	@Test
	public void testEbookEditOptions() {
		EbookEditOptions options = new EbookEditOptions();
		assertFalse(options.isDetach());
		options.setDetach(true);
		assertTrue(options.isDetach());
	}

	@Test
	public void testEbookMetaOptions() {
		EbookMetaOptions options = new EbookMetaOptions();
		// Test default values
		assertFalse(options.isAuthorSort());
		assertFalse(options.isFromOpf());
		assertFalse(options.isGetCover());
		assertFalse(options.isTitleSort());

		// Test setters
		options.setAuthorSort(true);
		options.setFromOpf(true);
		options.setGetCover(true);
		options.setTitleSort(true);

		assertTrue(options.isAuthorSort());
		assertTrue(options.isFromOpf());
		assertTrue(options.isGetCover());
		assertTrue(options.isTitleSort());
	}

	@Test
	public void testEbookPolishOptions() {
		EbookPolishOptions options = new EbookPolishOptions();
		// Test default values
		assertFalse(options.isCompressImages());
		assertFalse(options.isEmbedFonts());
		assertFalse(options.isRemoveJacket());
		assertFalse(options.isRemoveUnusedCss());
		assertFalse(options.isSmartenPunctuation());
		assertFalse(options.isSubsetFonts());
		assertFalse(options.isUpgradeBook());

		// Test setters
		options.setCompressImages(true);
		options.setEmbedFonts(true);
		options.setRemoveJacket(true);
		options.setRemoveUnusedCss(true);
		options.setSmartenPunctuation(true);
		options.setSubsetFonts(true);
		options.setUpgradeBook(true);

		assertTrue(options.isCompressImages());
		assertTrue(options.isEmbedFonts());
		assertTrue(options.isRemoveJacket());
		assertTrue(options.isRemoveUnusedCss());
		assertTrue(options.isSmartenPunctuation());
		assertTrue(options.isSubsetFonts());
		assertTrue(options.isUpgradeBook());
	}

	@Test
	public void testEbookViewerOptions() {
		EbookViewerOptions options = new EbookViewerOptions();
		// Test default values
		assertFalse(options.isContinueReading());
		assertFalse(options.isDebugJavascript());
		assertFalse(options.isFullscreen());
		assertFalse(options.isRaiseWindow());

		// Test setters
		options.setContinueReading(true);
		options.setDebugJavascript(true);
		options.setFullscreen(true);
		options.setRaiseWindow(true);

		assertTrue(options.isContinueReading());
		assertTrue(options.isDebugJavascript());
		assertTrue(options.isFullscreen());
		assertTrue(options.isRaiseWindow());
	}

	@Test
	public void testFetchEbookMetadataOptions() {
		FetchEbookMetadataOptions options = new FetchEbookMetadataOptions();
		// Test default values
		assertFalse(options.isAllowedPlugin());

		// Test setters
		options.setAllowedPlugin(true);
		assertTrue(options.isAllowedPlugin());
	}

	@Test
	public void testLrf2lrsOptions() {
		Lrf2lrsOptions options = new Lrf2lrsOptions();
		// Test default values
		assertFalse(options.isDontOutputResources());

		// Test setters
		options.setDontOutputResources(true);
		assertTrue(options.isDontOutputResources());
	}

	@Test
	public void testLrfviewerOptions() {
		LrfviewerOptions options = new LrfviewerOptions();
		// Test default values
		assertFalse(options.isDisableHyphenation());
		assertFalse(options.isVisualDebug());
		assertFalse(options.isWhiteBackground());

		// Test setters
		options.setDisableHyphenation(true);
		options.setVisualDebug(true);
		options.setWhiteBackground(true);

		assertTrue(options.isDisableHyphenation());
		assertTrue(options.isVisualDebug());
		assertTrue(options.isWhiteBackground());
	}

	@Test
	public void testLrs2lrfOptions() {
		// Lrs2lrfOptions is an empty class - just verify instantiation
		Lrs2lrfOptions options = new Lrs2lrfOptions();
		assertNotNull(options);
	}

	@Test
	public void testWeb2diskOptions() {
		Web2diskOptions options = new Web2diskOptions();
		// Test default values
		assertFalse(options.isDontDownloadStylesheets());

		// Test setters
		options.setDontDownloadStylesheets(true);
		assertTrue(options.isDontDownloadStylesheets());
	}

	// ==================== Configuration bean tests ====================

	@Test
	public void testConfigurationOutputHandler() {
		CalibreInvokerConfiguration config = new CalibreInvokerConfiguration();
		InvocationOutputHandler handler = config.outputHandler();
		assertNotNull(handler);
	}

	@Test
	public void testConfigurationErrorHandler() {
		CalibreInvokerConfiguration config = new CalibreInvokerConfiguration();
		InvocationOutputHandler handler = config.errorHandler();
		assertNotNull(handler);
	}

	@Test
	public void testConfigurationInvokerLogger() {
		CalibreInvokerConfiguration config = new CalibreInvokerConfiguration();
		InvokerLogger logger = config.invokerLogger();
		assertNotNull(logger);
	}

	@Test
	public void testConfigurationMavenInvokerWithDefaults() {
		CalibreInvokerConfiguration config = new CalibreInvokerConfiguration();
		InvocationOutputHandler outputHandler = config.outputHandler();
		InvocationOutputHandler errorHandler = config.errorHandler();
		InvokerLogger invokerLogger = config.invokerLogger();
		CalibreInvokerProperties properties = new CalibreInvokerProperties();

		Invoker invoker = config.mavenInvoker(outputHandler, errorHandler, invokerLogger, properties);
		assertNotNull(invoker);
	}

	@Test
	public void testConfigurationMavenInvokerWithCustomProperties() {
		CalibreInvokerConfiguration config = new CalibreInvokerConfiguration();
		InvocationOutputHandler outputHandler = config.outputHandler();
		InvocationOutputHandler errorHandler = config.errorHandler();
		InvokerLogger invokerLogger = config.invokerLogger();
		CalibreInvokerProperties properties = new CalibreInvokerProperties();
		properties.setLocalRepository(System.getProperty("java.io.tmpdir"));
		properties.setMavenExecutable("/usr/bin/mvn");
		properties.setMavenHome("/opt/maven");

		Invoker invoker = config.mavenInvoker(outputHandler, errorHandler, invokerLogger, properties);
		assertNotNull(invoker);
	}

	@Test
	public void testConfigurationMavenInvokerWithNonExistentLocalRepo() {
		CalibreInvokerConfiguration config = new CalibreInvokerConfiguration();
		InvocationOutputHandler outputHandler = config.outputHandler();
		InvocationOutputHandler errorHandler = config.errorHandler();
		InvokerLogger invokerLogger = config.invokerLogger();
		CalibreInvokerProperties properties = new CalibreInvokerProperties();
		String nonExistent = System.getProperty("java.io.tmpdir") + File.separator + "nonexistent-config-repo-" + System.currentTimeMillis();
		properties.setLocalRepository(nonExistent);

		Invoker invoker = config.mavenInvoker(outputHandler, errorHandler, invokerLogger, properties);
		assertNotNull(invoker);
		// Cleanup
		new File(nonExistent).delete();
	}

	@Test
	public void testConfigurationMavenInvokerTemplate() {
		CalibreInvokerConfiguration config = new CalibreInvokerConfiguration();
		InvocationOutputHandler outputHandler = config.outputHandler();
		InvocationOutputHandler errorHandler = config.errorHandler();
		InvokerLogger invokerLogger = config.invokerLogger();
		CalibreInvokerProperties properties = new CalibreInvokerProperties();
		Invoker invoker = config.mavenInvoker(outputHandler, errorHandler, invokerLogger, properties);

		CalibreInvokerTemplate template = config.mavenInvokerTemplate(outputHandler, errorHandler, invoker, properties);
		assertNotNull(template);
	}

	@Test
	public void testNewRequestWithBlankMavenOpts() {
		CalibreInvokerProperties props = new CalibreInvokerProperties();
		props.setMavenOpts("   ");

		InvocationRequest request = props.newRequest();
		assertNotNull(request);
	}

	@Test
	public void testNewRequestWithBlankLocalRepo() {
		CalibreInvokerProperties props = new CalibreInvokerProperties();
		props.setLocalRepository("   ");

		InvocationRequest request = props.newRequest();
		assertNotNull(request);
	}

	@Test
	public void testNewRequestWithBlankGlobalSettings() {
		CalibreInvokerProperties props = new CalibreInvokerProperties();
		props.setGlobalSettings("   ");

		InvocationRequest request = props.newRequest();
		assertNotNull(request);
	}

	@Test
	public void testNewRequestWithBlankGlobalToolchains() {
		CalibreInvokerProperties props = new CalibreInvokerProperties();
		props.setGlobalToolchains("   ");

		InvocationRequest request = props.newRequest();
		assertNotNull(request);
	}

	@Test
	public void testNewRequestWithBlankJavaHome() {
		CalibreInvokerProperties props = new CalibreInvokerProperties();
		props.setJavaHome("   ");

		InvocationRequest request = props.newRequest();
		assertNotNull(request);
	}

	@Test
	public void testNewRequestWithBlankUserSettings() {
		CalibreInvokerProperties props = new CalibreInvokerProperties();
		props.setUserSettings("   ");

		InvocationRequest request = props.newRequest();
		assertNotNull(request);
	}

	@Test
	public void testNewRequestWithBlankResumeFrom() {
		CalibreInvokerProperties props = new CalibreInvokerProperties();
		props.setResumeFrom("   ");

		InvocationRequest request = props.newRequest();
		assertNotNull(request);
	}

}
