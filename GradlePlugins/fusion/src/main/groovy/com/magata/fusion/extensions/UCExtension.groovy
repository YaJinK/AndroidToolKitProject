package com.magata.fusion.extensions

import com.magata.fusion.extensions.base.ChannelInfoExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.provider.Property

public abstract class UCExtension extends ChannelInfoExtension {
    abstract Property<Boolean> getDebugMode();
    abstract Property<Boolean> getEnablePayHistory();
    abstract Property<Boolean> getEnableUserChange();
    abstract Property<Integer> getOrientation();

    public void handleConfig(Project project) {
        AddToManifestPlaceholders(project, "ucAppId", getAppId());
        AddToManifestPlaceholders(project, "ucDebugMode", getDebugMode());
        AddToManifestPlaceholders(project, "enablePayHistory", getEnablePayHistory());
        AddToManifestPlaceholders(project, "enableUserChange", getEnableUserChange());
        AddToManifestPlaceholders(project, "orientation", getOrientation());

        DependencyHandler dependencyHandler = project.getDependencies();
        dependencyHandler.add("implementation","com.magata.toolkits:uc:1.0.0")
    }
}
