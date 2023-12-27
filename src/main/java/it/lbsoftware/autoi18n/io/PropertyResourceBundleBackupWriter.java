package it.lbsoftware.autoi18n.io;

import java.io.File;

public interface PropertyResourceBundleBackupWriter {

  /**
   * Copies the original file of the Resource Bundle before adding translations; the filename of the
   * copy is composed of the original one and a suffix indicating the timestamp of the operation and
   * is saved in the default backup folder
   *
   * @param resourceBundle The original file to copy
   * @param propertyResourceBundleBackupWriterOptions The options to use when performing the backup
   * @return True if the file is successfully copied, false otherwise
   */
  boolean backup(
      File resourceBundle,
      PropertyResourceBundleBackupWriterOptions propertyResourceBundleBackupWriterOptions);
}
