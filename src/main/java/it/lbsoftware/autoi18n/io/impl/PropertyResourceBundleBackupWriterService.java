package it.lbsoftware.autoi18n.io.impl;

import it.lbsoftware.autoi18n.io.PropertyResourceBundleBackupWriter;
import it.lbsoftware.autoi18n.io.PropertyResourceBundleBackupWriterOptions;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.io.FileUtils;

public class PropertyResourceBundleBackupWriterService implements
    PropertyResourceBundleBackupWriter {

  @Override
  public boolean backup(File resourceBundle,
      PropertyResourceBundleBackupWriterOptions propertyResourceBundleBackupWriterOptions) {
    try {
      FileUtils.copyFile(resourceBundle,
          Path.of(propertyResourceBundleBackupWriterOptions.backupDirectory().getAbsolutePath(),
              resourceBundle.getName() + "_" + LocalDateTime.now().format(
                  DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))).toFile(), false);
    } catch (NullPointerException | IOException e) {
      return false;
    }
    return true;
  }
}
