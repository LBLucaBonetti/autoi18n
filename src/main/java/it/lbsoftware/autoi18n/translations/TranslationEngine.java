package it.lbsoftware.autoi18n.translations;

import it.lbsoftware.autoi18n.paramsproviders.TranslationEngineParamsProvider;
import it.lbsoftware.autoi18n.paramsproviders.impl.googlecloudtranslationv3.GoogleCloudTranslationV3ParamsProvider;
import it.lbsoftware.autoi18n.paramsproviders.impl.libretranslate.LibreTranslateParamsProvider;
import it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3.GoogleCloudTranslationV3;
import it.lbsoftware.autoi18n.translations.impl.libretranslate.LibreTranslate;
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
      new GoogleCloudTranslationV3(),
      new GoogleCloudTranslationV3ParamsValidator(),
      new GoogleCloudTranslationV3ParamsProvider()),
  LIBRE_TRANSLATE(
      "LibreTranslate",
      new LibreTranslate(),
      new LibreTranslateParamsValidator(),
      new LibreTranslateParamsProvider());

  private final String name;
  private final TranslationService translationService;
  private final TranslationEngineParamsValidator translationEngineParamsValidator;
  private final TranslationEngineParamsProvider translationEngineParamsProvider;
}
