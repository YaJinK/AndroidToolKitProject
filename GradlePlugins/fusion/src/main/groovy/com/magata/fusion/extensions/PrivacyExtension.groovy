package com.magata.fusion.extensions;

import com.magata.fusion.extensions.base.BaseExtension;

import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;

public abstract class PrivacyExtension implements BaseExtension {
    public abstract Property<String> getHtmlUrl();
    public abstract Property<String> getTitle();
    public abstract Property<String> getContent();
    public abstract Property<String> getJump();
    public abstract Property<Integer> getScreenOrientation();
    public abstract ListProperty<String> getSubTitleArray();
    public abstract ListProperty<String> getSubContentHtmlArray();

    public void handleConfig(Project project) {
        project.extensions.android.buildTypes.debug.manifestPlaceholders.put("privacyScreenOrientation", getScreenOrientation().getOrNull())
        project.extensions.android.buildTypes.release.manifestPlaceholders.put("privacyScreenOrientation", getScreenOrientation().getOrNull())
        project.extensions.android.defaultConfig.manifestPlaceholders.put("privacyScreenOrientation", getScreenOrientation().getOrNull())

        project.extensions.android.defaultConfig.resValue("string", "privacy_html_url", getHtmlUrl().get())
        project.extensions.android.buildTypes.debug.resValue("string", "privacy_html_url", getHtmlUrl().get())
        project.extensions.android.buildTypes.release.resValue("string", "privacy_html_url", getHtmlUrl().get())

        project.extensions.android.defaultConfig.resValue("string", "privacy_entry_title", getTitle().get())
        project.extensions.android.buildTypes.debug.resValue("string", "privacy_entry_title", getTitle().get())
        project.extensions.android.buildTypes.release.resValue("string", "privacy_entry_title", getTitle().get())

        project.extensions.android.defaultConfig.resValue("string", "privacy_entry_content", getContent().get())
        project.extensions.android.buildTypes.debug.resValue("string", "privacy_entry_content", getContent().get())
        project.extensions.android.buildTypes.release.resValue("string", "privacy_entry_content", getContent().get())

        project.extensions.android.defaultConfig.resValue("string", "privacy_agree_jump", getJump().get())
        project.extensions.android.buildTypes.debug.resValue("string", "privacy_agree_jump", getJump().get())
        project.extensions.android.buildTypes.release.resValue("string", "privacy_agree_jump", getJump().get())


        def titleArray = getSubTitleArray().get()
        def contentArray = getSubContentHtmlArray().get()
        def length = titleArray.size()
        for (int index = 0; index < length; index++) {
            project.extensions.android.defaultConfig.resValue("string", "privacy_title_arr" + index , titleArray[index])
            project.extensions.android.buildTypes.debug.resValue("string", "privacy_title_arr" + index, titleArray[index])
            project.extensions.android.buildTypes.release.resValue("string", "privacy_title_arr" + index, titleArray[index])

            project.extensions.android.defaultConfig.resValue("string", "privacy_content_arr" + index, contentArray[index])
            project.extensions.android.buildTypes.debug.resValue("string", "privacy_content_arr" + index, contentArray[index])
            project.extensions.android.buildTypes.release.resValue("string", "privacy_content_arr" + index, contentArray[index])
        }

        DependencyHandler dependencyHandler = project.getDependencies();
        dependencyHandler.add("implementation","com.magata.toolkits:privacy:1.0.0");
        dependencyHandler.add("implementation","com.magata.toolkits:utility:1.0.0");
    }
}