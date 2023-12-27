package it.lbsoftware.autoi18n;

import it.lbsoftware.autoi18n.constants.Constants;
import it.lbsoftware.autoi18n.paramsproviders.TranslationEngineParams;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import org.opentest4j.AssertionFailedError;

public final class TestUtils {

  private TestUtils() {
    throw new UnsupportedOperationException("This class cannot be instantiated");
  }

  public static String parameterEntry(final String value) {
    return value;
  }

  public static String parameterInputLanguage(final String value) {
    return value;
  }

  public static String optionShortTranslationEngine(final String value) {
    return Constants.OPTION_SHORT_TRANSLATION_ENGINE + value;
  }

  public static String optionLongTranslationEngineParams(final String value) {
    return Constants.OPTION_LONG_TRANSLATION_ENGINE_PARAMS + "=" + value;
  }

  public static String parameterOutputLanguages(final String value) {
    return value;
  }

  public static String optionShortBaseDirectory(final String value) {
    return Constants.OPTION_SHORT_BASE_DIRECTORY + value;
  }

  public static String optionLongBaseDirectory(final String value) {
    return Constants.OPTION_LONG_BASE_DIRECTORY + "=" + value;
  }

  public static String requiredTranslationEngineParamsForDefaultTranslationEngine() {
    return optionLongTranslationEngineParams(
        TranslationEngineParams.API_KEY_PARAM
            + "=fake-api-key,"
            + TranslationEngineParams.PROJECT_NUMBER_OR_ID_PARAM
            + "=project-number-or-id");
  }

  public static Path createPropertyResourceBundleFile(final Path directory, final String fileName)
      throws IOException {
    var content =
        """
        app.key1=value1
        app.key2=value2
        app.key3=value3
        """;
    return createPropertyResourceBundleFileWithContent(directory, fileName, content);
  }

  public static Path createPropertyResourceBundleFileWithContent(
      final Path directory, final String fileName, final String content) throws IOException {
    var filePath = Files.createFile(Path.of(directory.toString(), fileName));
    try (var fileWriter = new FileWriter(filePath.toFile())) {
      fileWriter.write(content);
    }
    return filePath;
  }

  public static void assertPropertyResourceBundleFileIsReadableAndContainsPairs(
      final Path propertyResourceBundleFile, final Map<String, String> pairs) {
    boolean containsPairs;
    try (final InputStream fileInputStream = Files.newInputStream(propertyResourceBundleFile)) {
      var properties = new Properties();
      properties.load(fileInputStream);
      containsPairs =
          pairs.entrySet().stream()
              .allMatch(
                  (Entry<String, String> entry) ->
                      properties.containsKey(entry.getKey())
                          && properties.get(entry.getKey()).equals(entry.getValue()));
    } catch (IOException e) {
      throw new AssertionFailedError(
          "File " + propertyResourceBundleFile.toFile().getAbsolutePath() + " is not readable");
    }
    if (!containsPairs) {
      throw new AssertionFailedError(
          "The Property Resource Bundle file does not contain all the pairs");
    }
  }
}
