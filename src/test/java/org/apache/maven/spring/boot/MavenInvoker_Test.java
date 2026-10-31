package org.apache.maven.spring.boot;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.shared.invoker.InvocationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

class MavenInvoker_Test {

    @TempDir Path home;

    @Test
    void mapsConfiguredOptionsIntoAnInvocationRequest() {
        CalibreInvokerProperties config = new CalibreInvokerProperties();
        config.setAlsoMake(true);
        config.setAlsoMakeDependents(true);
        config.setBatchMode(true);
        config.setDebug(true);
        config.setOffline(true);
        config.setGlobalSettings(home.resolve("global.xml").toString());
        config.setGlobalToolchains(home.resolve("toolchains.xml").toString());
        config.setJavaHome(home.resolve("jdk").toString());
        config.setLocalRepository(home.resolve("repository").toString());
        config.setMavenOpts("-Xmx256m");
        config.setProfiles(List.of("integration"));
        config.setProjects(List.of("module-a"));
        Properties properties = new Properties();
        properties.setProperty("mode", "test");
        config.setProperties(properties);
        config.setResumeFrom("module-a");
        config.setShellEnvironmentInherited(true);
        config.setShowErrors(true);
        config.setShowVersion(true);
        config.setThreads(2);
        config.setUpdateSnapshots(true);
        config.setUserSettings(home.resolve("user.xml").toString());

        InvocationRequest request = config.newRequest();
        assertTrue(request.isAlsoMake());
        assertTrue(request.isAlsoMakeDependents());
        assertTrue(request.isBatchMode());
        assertTrue(request.isDebug());
        assertTrue(request.isOffline());
        assertEquals(home.resolve("global.xml").toFile(), request.getGlobalSettingsFile());
        assertEquals(home.resolve("toolchains.xml").toFile(), request.getGlobalToolchainsFile());
        assertEquals(home.resolve("jdk").toFile(), request.getJavaHome());
        assertEquals(home.resolve("repository").toFile(), request.getLocalRepositoryDirectory(new File("unused")));
        assertTrue(home.resolve("repository").toFile().isDirectory());
        assertEquals("-Xmx256m", request.getMavenOpts());
        assertEquals(List.of("integration"), request.getProfiles());
        assertEquals(List.of("module-a"), request.getProjects());
        assertSame(properties, request.getProperties());
        assertEquals("module-a", request.getResumeFrom());
        assertTrue(request.isShellEnvironmentInherited());
        assertTrue(request.isShowErrors());
        assertTrue(request.isShowVersion());
        assertEquals("2", request.getThreads());
        assertTrue(request.isUpdateSnapshots());
        assertEquals(home.resolve("user.xml").toFile(), request.getUserSettingsFile());
    }

    @Test
    void defaultRequestUsesTheDefaultRepository() {
        InvocationRequest request = new CalibreInvokerProperties().newRequest();
        assertEquals(CalibreInvokerProperties.defaultUserLocalRepository,
                request.getLocalRepositoryDirectory(new File("unused")));
        assertFalse(request.isOffline());
    }

    @Test
    void configurationPropertiesKeepTheirJavaBeanValues() throws Exception {
        CalibreInvokerProperties config = new CalibreInvokerProperties();
        int tested = 0;
        for (PropertyDescriptor property : Introspector.getBeanInfo(CalibreInvokerProperties.class).getPropertyDescriptors()) {
            if (property.getReadMethod() == null || property.getWriteMethod() == null) continue;
            Object value = sample(property.getPropertyType());
            if (value == null) continue;
            property.getWriteMethod().invoke(config, value);
            assertEquals(value, property.getReadMethod().invoke(config), property.getName());
            tested++;
        }
        assertTrue(tested >= 25, "expected to exercise the configuration property contract");
    }

    private Object sample(Class<?> type) {
        if (type == boolean.class) return true;
        if (type == int.class) return 3;
        if (type == String.class) return "value";
        if (type == Map.class) return Map.of("key", "value");
        if (type == List.class) return List.of("value");
        if (type == Properties.class) return new Properties();
        if (type.isEnum()) return type.getEnumConstants()[0];
        return null;
    }
}
