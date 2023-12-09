package it.lbsoftware.autoi18n.io.impl;

import it.lbsoftware.autoi18n.io.PropertyResourceBundlesRetriever;
import it.lbsoftware.autoi18n.utils.LanguageAndCountry;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class PropertyResourceBundlesRetrieverService implements PropertyResourceBundlesRetriever {

  public static final String POSSIBLE_NAMES =
      String.join(
          "|", Set.of("label", "labels", "language", "languages", "translation", "translations"));

  @Override
  public Map<LanguageAndCountry, File> retrieve(
      final Set<LanguageAndCountry> outputLanguageAndCountries, final File baseDirectory) {
    final File validatedBaseDirectory =
        Optional.ofNullable(baseDirectory).filter(File::isDirectory).orElseGet(FileUtils::current);
    Map<LanguageAndCountry, File> propertyResourceBundles = new HashMap<>();
    outputLanguageAndCountries.forEach(
        (LanguageAndCountry outputLanguageAndCountry) -> {
          var files = retrieve(outputLanguageAndCountry, validatedBaseDirectory);
          if (files.isEmpty()) {
            System.out.println("No file found for language " + outputLanguageAndCountry);
            return;
          }
          if (files.size() > 1) {
            System.out.println(
                "Multiple files found for "
                    + outputLanguageAndCountry
                    + " language: "
                    + files.stream().map(File::getAbsolutePath).collect(Collectors.joining(", ")));
            return;
          }
          var file = files.stream().findFirst().get();
          getResourceBundle(file)
              .ifPresentOrElse(
                  (ResourceBundle resourceBundle) -> {
                    System.out.println(
                        "Found Property Resource Bundle file "
                            + file.getAbsolutePath()
                            + " for language "
                            + outputLanguageAndCountry);
                    propertyResourceBundles.put(outputLanguageAndCountry, file);
                  },
                  () ->
                      System.out.println(
                          "No valid Property Resource Bundle file found for language"
                              + outputLanguageAndCountry));
        });
    return propertyResourceBundles;
  }

  private Optional<ResourceBundle> getResourceBundle(final File file) {
    try (final InputStream fileInputStream = Files.newInputStream(file.toPath())) {
      return Optional.of(new PropertyResourceBundle(fileInputStream));
    } catch (IOException | NullPointerException | IllegalArgumentException e) {
      return Optional.empty();
    }
  }

  /**
   * Finds files that can be read and written whose names are among the possible names, and that
   * indicate translations for the provided output language
   *
   * @param outputLanguageAndCountry The output language to translate to
   * @return The files matching the conditions, or an empty set
   */
  private Set<File> retrieve(
      final LanguageAndCountry outputLanguageAndCountry, final File baseDirectory) {
    return FileUtils.listFiles(
            baseDirectory, getIOFileFilter(outputLanguageAndCountry), TrueFileFilter.INSTANCE)
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

  private IOFileFilter getIOFileFilter(final LanguageAndCountry outputLanguageAndCountry) {
    var pattern = "(?i)(" + POSSIBLE_NAMES + ")(_|-)" + outputLanguageAndCountry + ".properties";
    return new RegexFileFilter(pattern);
  }
}
