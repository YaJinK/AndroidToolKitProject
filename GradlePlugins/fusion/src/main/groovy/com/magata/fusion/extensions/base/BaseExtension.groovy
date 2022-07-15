package com.magata.fusion.extensions.base;

import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;

public abstract class BaseExtension {
    abstract Property<Boolean> getEnabled();
    abstract void handleConfig(Project project);
    void AddToManifestPlaceholders(Project project, String key, Property value) {
        project.extensions.android.buildTypes.debug.manifestPlaceholders.put(key, value.getOrNull())
        project.extensions.android.buildTypes.release.manifestPlaceholders.put(key, value.getOrNull())
        project.extensions.android.defaultConfig.manifestPlaceholders.put(key, value.getOrNull())
    }

    void AddToManifestPlaceholders(Project project, String key, String prefix, Property value) {
        project.extensions.android.buildTypes.debug.manifestPlaceholders.put(key, prefix + value.getOrNull())
        project.extensions.android.buildTypes.release.manifestPlaceholders.put(key, prefix + value.getOrNull())
        project.extensions.android.defaultConfig.manifestPlaceholders.put(key, prefix + value.getOrNull())
    }

    void AddToResValue(Project project, String type, String key, Property value) {
        project.extensions.android.defaultConfig.resValue(type, key, value.getOrNull())
        project.extensions.android.buildTypes.debug.resValue(type, key, value.getOrNull())
        project.extensions.android.buildTypes.release.resValue(type, key, value.getOrNull())
    }

    void AddToResArray(Project project, String type, String key, ListProperty value) {
        def array = value.get()
        def length = array.size()
        for (int index = 0; index < length; index++) {
            project.extensions.android.defaultConfig.resValue(type, key + index, array[index])
            project.extensions.android.buildTypes.debug.resValue(type, key + index, array[index])
            project.extensions.android.buildTypes.release.resValue(type, key + index, array[index])
        }
    }
}
