package it.lbsoftware.autoi18n.translations;

import it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3.GoogleCloudTranslationV3;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum TranslationEngine {
  GOOGLE_CLOUD_TRANSLATION_V3(new GoogleCloudTranslationV3(), "Google Cloud Translation v3");

  private final TranslationService translationService;
  private final String name;
}
