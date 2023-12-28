package it.lbsoftware.autoi18n.io;

import java.io.File;
import java.util.Map;

public interface PropertyResourceBundleWriter {

  /**
   * Writes the translations for the specific language to the specified Resource Bundle; this method
   * assumes that the Resource Bundle and the translations represent the same language, and the
   * output file is readable and writeable
   *
   * @param resourceBundle The Resource Bundle file to write to
   * @param translations The mappings to write
   * @param options The options to use when writing
   * @return True if the write operation succeeds, false otherwise
   */
  boolean write(
      File resourceBundle,
      Map<String, String> translations,
      PropertyResourceBundleWriterOptions options);
}
