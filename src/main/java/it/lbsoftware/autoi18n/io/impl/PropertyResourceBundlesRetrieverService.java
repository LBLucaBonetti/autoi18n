package it.lbsoftware.autoi18n.io.impl;

import it.lbsoftware.autoi18n.io.PropertyResourceBundlesRetriever;
import it.lbsoftware.autoi18n.utils.LanguageAndCountry;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class PropertyResourceBundlesRetrieverService implements PropertyResourceBundlesRetriever {

  public static final Set<String> POSSIBLE_NAMES =
      Set.of("label", "labels", "language", "languages", "translation", "translations");
  private final Set<LanguageAndCountry> outputLanguageAndCountries;
  private final File baseDirectory;

  public PropertyResourceBundlesRetrieverService(
      final Set<LanguageAndCountry> outputLanguageAndCountries, final File baseDirectory) {
    this.outputLanguageAndCountries = outputLanguageAndCountries;
    this.baseDirectory =
        Optional.ofNullable(baseDirectory).filter(File::isDirectory).orElseGet(FileUtils::current);
  }

  @Override
  public Map<LanguageAndCountry, ResourceBundle> retrieve() {
    Map<LanguageAndCountry, ResourceBundle> propertyResourceBundles = new HashMap<>();
    retrieve(POSSIBLE_NAMES, outputLanguageAndCountries).forEach(System.out::println);
    return propertyResourceBundles;
  }

  /**
   * Finds files that can be read and written whose names are among the possible names and that
   * indicate translations for an output language
   *
   * @param possibleNames The possible names for the files; it does not indicate the full name of
   *     the file, but the first part (i.e.: "labels" with english language will match a file named
   *     "labels_EN.properties" or "labels-EN.properties")
   * @param outputLanguageAndCountries The output languages to translate to
   * @return A collection of files matching the conditions
   */
  public Collection<File> retrieve(
      final Set<String> possibleNames, final Set<LanguageAndCountry> outputLanguageAndCountries) {
    return FileUtils.listFiles(
            baseDirectory,
            getIOFileFilter(possibleNames, outputLanguageAndCountries),
            TrueFileFilter.INSTANCE)
        .stream()
        .filter(this::canReadAndWrite)
        .collect(Collectors.toSet());
  }

  private boolean canReadAndWrite(final File file) {
    try {
      return file.canRead() && file.canWrite();
    } catch (SecurityException e) {
      return false;
    }
  }

  private IOFileFilter getIOFileFilter(
      final Set<String> possibleNames, final Set<LanguageAndCountry> outputLanguageAndCountries) {
    var pattern =
        "(?i)("
            + String.join("|", possibleNames)
            + ")(_|-)("
            + String.join(
                "|",
                outputLanguageAndCountries.stream()
                    .map(LanguageAndCountry::toString)
                    .collect(Collectors.toSet()))
            + ").properties";
    return new RegexFileFilter(pattern);
  }
}
