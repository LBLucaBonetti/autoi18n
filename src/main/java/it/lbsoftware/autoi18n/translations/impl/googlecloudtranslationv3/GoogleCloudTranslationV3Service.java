package it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3;

import it.lbsoftware.autoi18n.paramsproviders.TranslationEngineParams;
import it.lbsoftware.autoi18n.translations.AbstractTranslationService;
import it.lbsoftware.autoi18n.translations.TranslationApi;
import it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3.pojos.GoogleCloudTranslationV3Request;
import it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3.pojos.GoogleCloudTranslationV3Response;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public final class GoogleCloudTranslationV3Service
    extends AbstractTranslationService<
        GoogleCloudTranslationV3Response, GoogleCloudTranslationV3Request> {

  public GoogleCloudTranslationV3Service(
      final TranslationApi<GoogleCloudTranslationV3Response, GoogleCloudTranslationV3Request>
          translationApi) {
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
      var translateTextRequest =
          new GoogleCloudTranslationV3Request(List.of(entry), inputLanguage, outputLanguage);
      var translateTextResponse =
          translationApi.translate(translationEngineParams, translateTextRequest);
      return translateTextResponse.translations().get(0).translatedText();
    } catch (Exception e) {
      System.err.printf(
          "Error translating %s from %s to %s%n", entry, inputLanguage, outputLanguage);
    }
    return StringUtils.EMPTY;
  }
}
