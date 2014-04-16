/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srxeditor.api;

import java.util.Properties;
import org.openide.filesystems.FileObject;

/**
 *
 * @author lukasz
 */
public abstract class SRXService {

    public static final String PROJECT_SRX_KEY_PREFIX = "srx.";
    public static final String PRODUCTION_SRX_SETTINGS_NAME = "production";

    public abstract FileObject render(FileObject document, String propertiesName);

    public abstract FileObject render(FileObject document);

    public abstract FileObject render();

    public abstract String[] getAvailableSRXRulesNames();

    public abstract Properties getSRXRules(String name);

    public abstract String getPreferredSRXRulesNames();

    public abstract String getDisplayName(String settingsName);
}
