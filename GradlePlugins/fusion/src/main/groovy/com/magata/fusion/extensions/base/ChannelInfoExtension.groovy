package com.magata.fusion.extensions.base;

import org.gradle.api.provider.Property;

public abstract class ChannelInfoExtension extends BaseExtension {
    abstract Property<String> getAppId();
}
