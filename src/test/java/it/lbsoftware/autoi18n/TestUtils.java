package it.lbsoftware.autoi18n;

import it.lbsoftware.autoi18n.constants.Constants;
import it.lbsoftware.autoi18n.paramsproviders.TranslationEngineParams;

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
}
