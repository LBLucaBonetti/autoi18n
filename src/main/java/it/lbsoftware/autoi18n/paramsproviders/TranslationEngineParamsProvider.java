package it.lbsoftware.autoi18n.paramsproviders;

import java.util.Map;

public interface TranslationEngineParamsProvider {

  /**
   * Provides the params the translation engine needs; params should already be validated
   *
   * @param params Input params
   * @return The params the translation engine needs
   */
  TranslationEngineParams provide(Map<String, String> params);
}
