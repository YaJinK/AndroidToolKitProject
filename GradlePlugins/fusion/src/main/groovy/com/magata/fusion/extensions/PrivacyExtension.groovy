package com.magata.fusion.extensions;

import com.magata.fusion.extensions.base.BaseExtension;

import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;

public abstract class PrivacyExtension extends BaseExtension {
    public abstract Property<String> getHtmlUrl();
    public abstract Property<String> getTitle();
    public abstract Property<String> getContent();
    public abstract Property<String> getJump();
    public abstract Property<Integer> getScreenOrientation();
    public abstract ListProperty<String> getSubTitleArray();
    public abstract ListProperty<String> getSubContentHtmlArray();

    public void handleConfig(Project project) {
        AddToManifestPlaceholders(project, "privacyScreenOrientation", getScreenOrientation());

        AddToResValue(project, "string", "privacy_html_url", getHtmlUrl())
        AddToResValue(project, "string", "privacy_entry_title", getTitle())
        AddToResValue(project, "string", "privacy_entry_content", getContent())
        AddToResValue(project, "string", "privacy_agree_jump", getJump())

        AddToResArray(project, "string", "privacy_title_arr", getSubTitleArray())
        AddToResArray(project, "string", "privacy_content_arr", getSubContentHtmlArray())

        DependencyHandler dependencyHandler = project.getDependencies();
        dependencyHandler.add("implementation","com.magata.toolkits:privacy:1.0.0");
    }
}