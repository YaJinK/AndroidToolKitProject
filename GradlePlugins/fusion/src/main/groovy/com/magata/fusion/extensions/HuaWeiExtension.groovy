package com.magata.fusion.extensions

import com.magata.fusion.extensions.base.ChannelInfoExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.provider.Property

public abstract class HuaWeiExtension extends ChannelInfoExtension {
    abstract Property<String> getCpId();

    public void handleConfig(Project project) {
        AddToManifestPlaceholders(project, "huaweiAppId", "appid=", getAppId());
        AddToManifestPlaceholders(project, "huaweiCpId", "cpid=", getCpId());

        DependencyHandler dependencyHandler = project.getDependencies();
        dependencyHandler.add("implementation","com.huawei.hms:hwid:6.4.0.300");
        dependencyHandler.add("implementation","com.huawei.hms:iap:6.3.0.300");
        dependencyHandler.add("implementation","com.huawei.hms:game:6.2.0.300");
        dependencyHandler.add("implementation","com.huawei.hms:hianalytics:6.4.0.300");
        dependencyHandler.add("implementation","com.magata.toolkits:huawei:1.0.0");
    }
}
