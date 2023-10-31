package it.lbsoftware.autoi18n.validators;

import java.util.Map;

public interface TranslationEngineParamsValidator {

  /**
   * Validates params for the translation engine; a set of params is considered valid if it allows
   * the translation engine to perform its translation without errors produced by the absence of
   * required params
   *
   * @param params Input params
   * @return Whether the params are considered valid or not
   */
  boolean validate(Map<String, String> params);
}
