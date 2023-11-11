package it.lbsoftware.autoi18n.translations.impl.libretranslate;

import it.lbsoftware.autoi18n.paramsproviders.TranslationEngineParams;
import it.lbsoftware.autoi18n.translations.AbstractTranslationService;
import it.lbsoftware.autoi18n.translations.TranslationApi;
import it.lbsoftware.autoi18n.translations.impl.libretranslate.pojos.LibreTranslateRequest;
import it.lbsoftware.autoi18n.translations.impl.libretranslate.pojos.LibreTranslateResponse;
import org.apache.commons.lang3.StringUtils;

public final class LibreTranslateService
    extends AbstractTranslationService<LibreTranslateResponse, LibreTranslateRequest> {

  public LibreTranslateService(
      final TranslationApi<LibreTranslateResponse, LibreTranslateRequest> translationApi) {
    super(translationApi);
  }

  @Override
  protected String translate(
      final String entry,
      final String inputLanguage,
      final String outputLanguage,
      final TranslationEngineParams translationEngineParams) {
    System.out.printf(
        "Connecting to translate %s from %s to %s%n", entry, inputLanguage, outputLanguage);
    try {
      var translateTextRequest = new LibreTranslateRequest(entry, inputLanguage, outputLanguage);
      var translateTextResponse =
          translationApi.translate(translationEngineParams, translateTextRequest);
      return translateTextResponse.translatedText();
    } catch (Exception e) {
      System.err.printf(
          "Error translating %s from %s to %s%n", entry, inputLanguage, outputLanguage);
    }
    return StringUtils.EMPTY;
  }
}
