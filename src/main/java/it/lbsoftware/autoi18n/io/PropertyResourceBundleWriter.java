package it.lbsoftware.autoi18n.io;

import java.io.File;
import java.util.Map;

public interface PropertyResourceBundleWriter {

  /**
   * Writes the translations for the specific language to the specified resource bundle; this method
   * assumes that the resource bundle and the translations represent the same language, and the
   * output file is readable and writeable
   *
   * @param resourceBundle The resource bundle file to
   * @param translations The mappings to write
   * @param options The options to use when writing
   * @return True if the write operation succeeds, false otherwise
   */
  boolean write(
      File resourceBundle,
      Map<String, String> translations,
      PropertyResourceBundleWriterOptions options);
}
