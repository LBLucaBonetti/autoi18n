package it.lbsoftware.autoi18n;

import it.lbsoftware.autoi18n.constants.Constants;
import it.lbsoftware.autoi18n.paramsproviders.TranslationEngineParams;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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

  public static String requiredTranslationEngineParamsForDefaultTranslationEngine() {
    return optionLongTranslationEngineParams(
        TranslationEngineParams.API_KEY_PARAM
            + "=fake-api-key,"
            + TranslationEngineParams.PROJECT_NUMBER_OR_ID_PARAM
            + "=project-number-or-id");
  }

  public static Path createPropertyResourceBundleFile(final Path directory, final String fileName)
      throws IOException {
    var content = """
app.key1=value1
app.key2=value2
app.key3=value3
""";
    var filePath = Files.createFile(Path.of(directory.toString(), fileName));
    try (var fileWriter = new FileWriter(filePath.toFile())) {
      fileWriter.write(content);
    }
    return filePath;
  }
}
