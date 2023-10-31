package it.lbsoftware.autoi18n;

import it.lbsoftware.autoi18n.constants.Constants;
import it.lbsoftware.autoi18n.paramsproviders.TranslationEngineParams;

public final class TestUtils {

  private TestUtils() {
    throw new UnsupportedOperationException("This class cannot be instantiated");
  }

  public static String optionShortEntry(final String value) {
    return Constants.OPTION_SHORT_ENTRY + value;
  }

  public static String optionLongEntry(final String value) {
    return Constants.OPTION_LONG_ENTRY + "=" + value;
  }

  public static String optionShortInputLanguage(final String value) {
    return Constants.OPTION_SHORT_INPUT_LANGUAGE + value;
  }

  public static String optionLongInputLanguage(final String value) {
    return Constants.OPTION_LONG_INPUT_LANGUAGE + "=" + value;
  }

  public static String optionShortTranslationEngine(final String value) {
    return Constants.OPTION_SHORT_TRANSLATION_ENGINE + value;
  }

  public static String optionLongTranslationEngine(final String value) {
    return Constants.OPTION_LONG_TRANSLATION_ENGINE + "=" + value;
  }

  public static String optionShortOutputLanguages(final String value) {
    return Constants.OPTION_SHORT_OUTPUT_LANGUAGES + value;
  }

  public static String optionLongOutputLanguages(final String value) {
    return Constants.OPTION_LONG_OUTPUT_LANGUAGES + "=" + value;
  }

  public static String optionLongTranslationEngineParams(final String value) {
    return Constants.OPTION_LONG_TRANSLATION_ENGINE_PARAMS + "=" + value;
  }

  public static String requiredTranslationEngineParamsForDefaultTranslationEngine() {
    return optionLongTranslationEngineParams(
        TranslationEngineParams.API_KEY_PARAM
            + "=fake-api-key,"
            + TranslationEngineParams.PROJECT_NUMBER_OR_ID_PARAM
            + "=project-number-or-id");
  }
}
