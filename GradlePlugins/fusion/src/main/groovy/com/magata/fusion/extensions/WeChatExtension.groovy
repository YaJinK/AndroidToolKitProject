package com.magata.fusion.extensions;

import com.magata.fusion.extensions.base.ChannelInfoExtension;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.DependencyHandler;

public abstract class WeChatExtension extends ChannelInfoExtension {
    public void  handleConfig(Project project) {
        AddToManifestPlaceholders(project, "weChatAppId", getAppId());

        DependencyHandler dependencyHandler = project.getDependencies();
        dependencyHandler.add("implementation","com.magata.toolkits:wechat:1.0.0");
    }
}
