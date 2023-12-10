package it.lbsoftware.autoi18n.facade;

import it.lbsoftware.autoi18n.io.PropertyResourceBundleWriterOptions;
import it.lbsoftware.autoi18n.io.impl.PropertyResourceBundleWriterService;
import it.lbsoftware.autoi18n.io.impl.PropertyResourceBundlesRetrieverService;
import it.lbsoftware.autoi18n.translations.TranslationEngine;
import it.lbsoftware.autoi18n.utils.LanguageAndCountry;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import picocli.CommandLine.ExitCode;

@RequiredArgsConstructor
public final class TranslationEngineFacade {

  private final TranslationEngine translationEngine;
  private final Map<String, String> params;
  private final Map<String, String> entries;
  private final LanguageAndCountry inputLanguageAndCountry;
  private final List<LanguageAndCountry> outputLanguageAndCountries;

  /**
   * Performs a whole translation operation and returns the resulting exit code
   *
   * @return The exit code for the whole translation operation
   */
  public Integer performTranslation() {
    var translationEngineParamsValidator = translationEngine.getTranslationEngineParamsValidator();
    if (!translationEngineParamsValidator.validate(params)) {
      System.err.println(
          "Invalid set of parameters for the translation engine "
              + translationEngine.getName()
              + ". See <translationEngine> description for more details");
      return ExitCode.USAGE;
    }
    if (entries == null) {
      System.err.println("No entries to translate; operation aborted");
      return ExitCode.USAGE;
    }
    var translationEngineParamsProvider = translationEngine.getTranslationEngineParamsProvider();
    var translationEngineParams = translationEngineParamsProvider.provide(params);
    var translationService = translationEngine.getTranslationService();
    var propertyResourceBundles =
        new PropertyResourceBundlesRetrieverService()
            .retrieve(
                outputLanguageAndCountries.stream()
                    .filter((LanguageAndCountry lac) -> !inputLanguageAndCountry.equals(lac))
                    .collect(Collectors.toSet()),
                FileUtils.current());
    var outputLanguageAndCountriesWithValidPropertyResourceBundles =
        propertyResourceBundles.keySet();
    System.out.println("Detected input language: " + inputLanguageAndCountry.toString());
    System.out.println(
        "Output languages with valid Property Resource Bundle file: "
            + outputLanguageAndCountriesWithValidPropertyResourceBundles);
    System.out.println("Detected entries: " + entries);
    System.out.println("Translation engine: " + translationEngine.getName());
    if (outputLanguageAndCountriesWithValidPropertyResourceBundles.isEmpty()) {
      System.err.println(
          "Could not find any valid Property Resource Bundle file for the detected output languages; operation aborted");
      return ExitCode.USAGE;
    }
    var translations =
        translationService.translate(
            entries,
            inputLanguageAndCountry,
            outputLanguageAndCountriesWithValidPropertyResourceBundles,
            translationEngineParams);
    var propertyResourceBundleWriter = new PropertyResourceBundleWriterService();
    translations.forEach(
        (languageAndCountry, translationsToWrite) -> {
          if (!propertyResourceBundles.containsKey(languageAndCountry)) {
            return;
          }
          var propertyResourceBundleFile = propertyResourceBundles.get(languageAndCountry);
          propertyResourceBundleWriter.write(
              propertyResourceBundleFile,
              translationsToWrite,
              new PropertyResourceBundleWriterOptions(true));
        });
    return ExitCode.OK;
  }
}
