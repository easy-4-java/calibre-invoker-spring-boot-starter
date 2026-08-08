package org.apache.maven.spring.boot;

import java.io.File;

import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationOutputHandler;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.InvokerLogger;
import org.apache.maven.shared.invoker.PrintStreamHandler;
import org.apache.maven.shared.invoker.SystemOutHandler;
import org.apache.maven.shared.invoker.SystemOutLogger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * Auto-configuration that wires the Maven {@link Invoker} infrastructure: output and error handlers,
 * an invoker logger, a configured {@link DefaultInvoker} and the {@link CalibreInvokerTemplate} helper.
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass({ DefaultInvoker.class })
@EnableConfigurationProperties({ CalibreInvokerProperties.class })
public class CalibreInvokerConfiguration {

	/** Provide a default standard-output {@link InvocationOutputHandler} unless one already exists. @return a SystemOutHandler */
	@Bean
	@ConditionalOnMissingBean
	public InvocationOutputHandler outputHandler() {
		return new SystemOutHandler();
	}

	/** Provide a default error-output {@link InvocationOutputHandler} unless one already exists. @return a PrintStreamHandler writing to stderr */
	@Bean
	@ConditionalOnMissingBean
	public InvocationOutputHandler errorHandler() {
		return new PrintStreamHandler(System.err, false);
	}

	/** Provide a default {@link InvokerLogger} unless one already exists. @return a SystemOutLogger */
	@Bean
	@ConditionalOnMissingBean
	public InvokerLogger invokerLogger() {
		return new SystemOutLogger();
	}

	/**
	 * Create the {@link Invoker} bean, configuring local repository, Maven executable, Maven home,
	 * output/error handlers and logger from the provided properties.
	 * @param outputHandler standard output handler
	 * @param errorHandler error output handler
	 * @param invokerLogger invoker logger
	 * @param properties invoker properties
	 * @return a configured Maven invoker
	 */
	@Bean
	@ConditionalOnMissingBean
	public Invoker mavenInvoker(InvocationOutputHandler outputHandler, InvocationOutputHandler errorHandler,
			InvokerLogger invokerLogger, CalibreInvokerProperties properties) {

		Invoker invoker = new DefaultInvoker();

		// Sets the handler used to capture the error output from the Maven build.
		invoker.setErrorHandler(errorHandler);
		// Sets the path to the base directory of the local repository to use for the
		// Maven invocation.
		if (StringUtils.hasText(properties.getLocalRepository())) {
			File localRepositoryDirectory = new File(properties.getLocalRepository());
			if (localRepositoryDirectory.exists() && localRepositoryDirectory.isDirectory()) {
				invoker.setLocalRepositoryDirectory(localRepositoryDirectory);
			} else {
				localRepositoryDirectory.mkdir();
				invoker.setLocalRepositoryDirectory(localRepositoryDirectory);
			}
		} else {
			invoker.setLocalRepositoryDirectory(CalibreInvokerProperties.defaultUserLocalRepository);
		}
		// Sets the logger used by this invoker to output diagnostic messages.
		invoker.setLogger(invokerLogger);
		//
		if (StringUtils.hasText(properties.getMavenExecutable())) {
			invoker.setMavenExecutable(new File(properties.getMavenExecutable()));
		}
		// Sets the path to the base directory of the Maven installation used to invoke
		// Maven.
		if (StringUtils.hasText(properties.getMavenHome())) {
			invoker.setMavenHome(new File(properties.getMavenHome()));
		}
		// Sets the handler used to capture the standard output from the Maven build.
		invoker.setOutputHandler(outputHandler);

		return invoker;
	}

	/** Create the {@link CalibreInvokerTemplate} bean wiring the handlers, invoker and properties. @param outputHandler standard output handler @param errorHandler error output handler @param mavenInvoker Maven invoker @param invokerProperties invoker properties @return a CalibreInvokerTemplate */
	@Bean
	public CalibreInvokerTemplate mavenInvokerTemplate(InvocationOutputHandler outputHandler,
			InvocationOutputHandler errorHandler, Invoker mavenInvoker, CalibreInvokerProperties invokerProperties) {
		return new CalibreInvokerTemplate(outputHandler, errorHandler, mavenInvoker, invokerProperties);
	}

}
