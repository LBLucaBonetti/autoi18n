package it.lbsoftware.autoi18n.io;

import it.lbsoftware.autoi18n.utils.LanguageAndCountry;
import java.util.Map;
import java.util.ResourceBundle;

public interface PropertyResourceBundlesRetriever {

  /**
   * Retrieves the resource bundles to write translations to, one per output language; resource
   * bundles are searched starting from the directory the command was launched by, according to the
   * following algorithm:
   *
   * <ol>
   *   <li>The name of the file must be of the form n_l.properties where n is a word among "label",
   *       "language", "translation" or their english plurals "labels", "languages", "translations"
   *       and l is the complete language code (i.e.: for "en", it is "en", for "en-US", it is
   *       "en-US") matched in a case-insensitive way
   *   <li>The resource bundle file is checked for read and write operations and if it is not
   *       readable and/or writeable, or it is not a valid resource bundle, it is considered as not
   *       found
   *   <li>If the resource bundle is still not found for a given language, then the entry will not
   *       be put in the output map, so the output map will lack a key-value pair for that language
   * </ol>
   *
   * @return A map containing the resource bundles found for the provided output languages; note
   *     that if a resource bundle is not found for a certain language, the resulting map will not
   *     contain that language key
   */
  Map<LanguageAndCountry, ResourceBundle> retrieve();
}