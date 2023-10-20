package it.lbsoftware.autoi18n.utils;

import static it.lbsoftware.autoi18n.constants.Constants.AUTOI18N_NAME;

import org.eclipse.microprofile.config.ConfigProvider;
import picocli.CommandLine.IVersionProvider;

public class VersionProvider implements IVersionProvider {

  @Override
  public String[] getVersion() {
    String versionConfig = "quarkus.application.version";
    return new String[] {
      AUTOI18N_NAME
          + " - version "
          + ConfigProvider.getConfig().getValue(versionConfig, String.class)
    };
  }
}
