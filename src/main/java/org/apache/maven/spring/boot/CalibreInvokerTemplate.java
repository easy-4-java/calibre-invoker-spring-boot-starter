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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.shared.invoker.InvocationOutputHandler;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.util.StringUtils;

/**
 * Maven build helper based on the Maven Invoker API; depends on a local Maven installation.
 * <p>Provides convenience methods to install/deploy artifacts and execute arbitrary Maven goals, as well
 * as reading the {@link Model} from a packaged artifact's {@code pom.xml}.</p>
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class CalibreInvokerTemplate {

	private InvocationOutputHandler outputHandler;
	private InvocationOutputHandler errorHandler;
	private Invoker mavenInvoker;
	private CalibreInvokerProperties properties;
	private MavenXpp3Reader modelReader = new MavenXpp3Reader();

	/** Create a template bound to the given handlers, invoker and properties. @param outputHandler standard output handler @param errorHandler error output handler @param mavenInvoker the Maven invoker @param invokerProperties invoker properties */
	public CalibreInvokerTemplate(InvocationOutputHandler outputHandler, InvocationOutputHandler errorHandler,
			Invoker mavenInvoker, CalibreInvokerProperties invokerProperties) {
		this.outputHandler = outputHandler;
		this.errorHandler = errorHandler;
		this.mavenInvoker = mavenInvoker;
		this.properties = invokerProperties;
	}

	/** Install an artifact into the local repository using the default base directory. @param file artifact file path @param groupId group id @param artifactId artifact id @param version version @param packaging packaging @param generatePom whether to generate a POM @param createChecksum whether to create checksums @return the invocation result @throws MavenInvocationException if the invocation fails */
	public InvocationResult install(String file, String groupId, String artifactId, String version, String packaging,
			boolean generatePom, boolean createChecksum) throws MavenInvocationException {
		return this.install(null, file, groupId, artifactId, version, packaging, generatePom, createChecksum);
	}

	/** Install an artifact into the local repository. @param basedir base directory or null @param file artifact file path @param groupId group id @param artifactId artifact id @param version version @param packaging packaging @param generatePom whether to generate a POM @param createChecksum whether to create checksums @return the invocation result @throws MavenInvocationException if the invocation fails */
	public InvocationResult install(String basedir, String file, String groupId, String artifactId, String version,
			String packaging, boolean generatePom, boolean createChecksum) throws MavenInvocationException {

		InvocationRequest request = properties.newRequest();
		request.setErrorHandler(errorHandler);
		request.setOutputHandler(outputHandler);

		if (StringUtils.hasText(basedir)) {
			request.setBaseDirectory(new File(basedir));
		}

		request.setGoals(Arrays.asList("install:install-file", "-Dfile=" + file, "-DgroupId=" + groupId,
				"-DartifactId=" + artifactId, "-Dversion=" + version, "-Dpackaging=" + packaging,
				"-DgeneratePom=" + generatePom, "-DcreateChecksum=" + createChecksum));

		return mavenInvoker.execute(request);
	}

	/** Deploy an artifact to a remote repository using the default base directory. @param file artifact file path @param groupId group id @param artifactId artifact id @param version version @param packaging packaging @param url repository URL @param repositoryId repository id @return the invocation result @throws MavenInvocationException if the invocation fails */
	public InvocationResult deploy(String file, String groupId, String artifactId, String version, String packaging,
			String url, String repositoryId) throws MavenInvocationException {
		return this.deploy(null, file, groupId, artifactId, version, packaging, url, repositoryId);
	}

	/** Deploy an artifact to a remote repository. @param basedir base directory or null @param file artifact file path @param groupId group id @param artifactId artifact id @param version version @param packaging packaging @param url repository URL @param repositoryId repository id @return the invocation result @throws MavenInvocationException if the invocation fails */
	public InvocationResult deploy(String basedir, String file, String groupId, String artifactId, String version,
			String packaging, String url, String repositoryId) throws MavenInvocationException {

		InvocationRequest request = properties.newRequest();
		request.setErrorHandler(errorHandler);
		request.setOutputHandler(outputHandler);
		if (StringUtils.hasText(basedir)) {
			request.setBaseDirectory(new File(basedir));
		}

		request.setGoals(Arrays.asList("deploy:deploy-file", "-DgroupId=" + groupId, "-DartifactId=" + artifactId,
				"-Dversion=" + version, "-Dpackaging=" + packaging, "-Dfile=" + file, "-Durl=" + url,
				"-DrepositoryId=" + repositoryId));

		return mavenInvoker.execute(request);
	}

	/** Execute the given Maven goals in the specified base directory. @param basedir base directory @param goals goals to execute @return the invocation result @throws MavenInvocationException if the invocation fails */
	public InvocationResult execute(File basedir, String... goals) throws MavenInvocationException {

		InvocationRequest request = properties.newRequest();
		request.setErrorHandler(errorHandler);
		request.setOutputHandler(outputHandler);

		request.setBaseDirectory(basedir);
		request.setGoals(Arrays.asList(goals));

		return mavenInvoker.execute(request);
	}

	/** Execute the given Maven goals in the specified base directory. @param basedir base directory path @param goals goals to execute @return the invocation result @throws MavenInvocationException if the invocation fails */
	public InvocationResult execute(String basedir, String... goals) throws MavenInvocationException {
		return this.execute(new File(basedir), goals);
	}

	/** Read the Maven {@link Model} from the {@code pom.xml} embedded in a packaged artifact. @param file the artifact file (jar/zip) @return the parsed Maven model @throws XmlPullParserException if the POM cannot be parsed @throws IOException if the file cannot be read or no POM is found */
	public Model readModel(File file) throws XmlPullParserException, IOException {
		try (
			ZipFile zipFile = new ZipFile(file)) {
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				//System.out.println(entry.getName());
				if (entry.getName().endsWith("pom.xml")) {
					InputStream input = zipFile.getInputStream(entry);
					Model model = modelReader.read(new InputStreamReader(input));
					return model;
				}
			}
		} 
		throw new IOException("Not a maven project, unable to parse version information.");
	}
	
}
