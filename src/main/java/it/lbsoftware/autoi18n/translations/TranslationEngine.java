package it.lbsoftware.autoi18n.translations;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum TranslationEngine {
  GOOGLE_CLOUD_TRANSLATION_V3("Google Cloud Translation v3");

  private final String name;
}
