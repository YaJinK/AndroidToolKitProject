package com.magata.fusion.extensions

import com.magata.fusion.extensions.base.BaseExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler

public abstract class AlipayExtension extends BaseExtension {
    public void  handleConfig(Project project) {

        DependencyHandler dependencyHandler = project.getDependencies();
        dependencyHandler.add("implementation","com.magata.toolkits:alipay:1.0.0");
    }
}
