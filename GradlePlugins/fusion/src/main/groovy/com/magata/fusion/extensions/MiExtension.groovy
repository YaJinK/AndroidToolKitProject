package com.magata.fusion.extensions

import com.magata.fusion.extensions.base.ChannelInfoExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.provider.Property

public abstract class MiExtension extends ChannelInfoExtension {
    abstract Property<String> getAppKey();

    public void handleConfig(Project project) {
        AddToManifestPlaceholders(project, "miAppId", "appId_", getAppId());
        AddToManifestPlaceholders(project, "miAppKey", "appKey_", getAppKey());

        project.extensions.android.defaultConfig.multiDexEnabled = true
        project.extensions.android.buildTypes.debug.multiDexEnabled = true
        project.extensions.android.buildTypes.release.multiDexEnabled = true

        DependencyHandler dependencyHandler = project.getDependencies();
        dependencyHandler.add("implementation","com.magata.toolkits:mi:1.0.0")
        dependencyHandler.add("implementation","com.android.support:multidex:1.0.0")
    }
}
