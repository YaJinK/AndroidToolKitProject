package com.magata.fusion.extensions.base;

import org.gradle.api.provider.Property;

public interface ChannelInfoExtension extends BaseExtension {
    Property<String> getAppId();
}
