package com.magata.fusion.extensions.base;

import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.provider.Property;

public interface BaseExtension {
    Property<Boolean> getEnabled();
    void handleConfig(Project project);
}
