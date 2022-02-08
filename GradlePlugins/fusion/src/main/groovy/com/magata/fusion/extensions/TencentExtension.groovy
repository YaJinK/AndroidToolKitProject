package com.magata.fusion.extensions;

import com.magata.fusion.extensions.base.ChannelInfoExtension;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.DependencyHandler;

public abstract class TencentExtension implements ChannelInfoExtension {
    public void handleConfig(Project project) {
        project.extensions.android.buildTypes.debug.manifestPlaceholders.put("tencentAppId", getAppId().getOrNull())
        project.extensions.android.buildTypes.release.manifestPlaceholders.put("tencentAppId", getAppId().getOrNull())
        project.extensions.android.defaultConfig.manifestPlaceholders.put("tencentAppId", getAppId().getOrNull())

        DependencyHandler dependencyHandler = project.getDependencies();
        dependencyHandler.add("implementation","com.magata.toolkits:opensdk:1.0.0")
        dependencyHandler.add("implementation","com.magata.toolkits:event-listeners:1.0.0")
        dependencyHandler.add("implementation","com.magata.toolkits:file-provider:1.0.0")
        dependencyHandler.add("implementation","com.magata.toolkits:utility:1.0.0")
    }
}
