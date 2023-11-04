package it.lbsoftware.autoi18n.translations;

import it.lbsoftware.autoi18n.paramsproviders.TranslationEngineParamsProvider;
import it.lbsoftware.autoi18n.paramsproviders.impl.googlecloudtranslationv3.GoogleCloudTranslationV3ParamsProvider;
import it.lbsoftware.autoi18n.paramsproviders.impl.libretranslate.LibreTranslateParamsProvider;
import it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3.GoogleCloudTranslationV3Service;
import it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3.api.GoogleCloudTranslationV3Api;
import it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3.api.GoogleCloudTranslationV3HttpClientProvider;
import it.lbsoftware.autoi18n.translations.impl.libretranslate.LibreTranslateService;
import it.lbsoftware.autoi18n.translations.impl.libretranslate.api.LibreTranslateApi;
import it.lbsoftware.autoi18n.translations.impl.libretranslate.api.LibreTranslateHttpClientProvider;
import it.lbsoftware.autoi18n.validators.TranslationEngineParamsValidator;
import it.lbsoftware.autoi18n.validators.impl.googlecloudtranslationv3.GoogleCloudTranslationV3ParamsValidator;
import it.lbsoftware.autoi18n.validators.impl.libretranslate.LibreTranslateParamsValidator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum TranslationEngine {
  GOOGLE_CLOUD_TRANSLATION_V3(
      "Google Cloud Translation v3",
      new GoogleCloudTranslationV3Service(
          new GoogleCloudTranslationV3Api(new GoogleCloudTranslationV3HttpClientProvider())),
      new GoogleCloudTranslationV3ParamsValidator(),
      new GoogleCloudTranslationV3ParamsProvider()),
  LIBRE_TRANSLATE(
      "LibreTranslate (expected to run locally @ localhost:5000)",
      new LibreTranslateService(new LibreTranslateApi(new LibreTranslateHttpClientProvider())),
      new LibreTranslateParamsValidator(),
      new LibreTranslateParamsProvider());

  private final String name;
  private final TranslationService translationService;
  private final TranslationEngineParamsValidator translationEngineParamsValidator;
  private final TranslationEngineParamsProvider translationEngineParamsProvider;
}
