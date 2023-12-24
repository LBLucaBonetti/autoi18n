package it.lbsoftware.autoi18n.facade;

import static it.lbsoftware.autoi18n.constants.Constants.DEFAULT_BACKUP_DIRECTORY_NAME;
import static it.lbsoftware.autoi18n.constants.Constants.DEFAULT_BASE_DIRECTORY;

import it.lbsoftware.autoi18n.io.PropertyResourceBundleBackupWriterOptions;
import it.lbsoftware.autoi18n.io.PropertyResourceBundleWriterOptions;
import it.lbsoftware.autoi18n.io.impl.PropertyResourceBundleBackupWriterService;
import it.lbsoftware.autoi18n.io.impl.PropertyResourceBundleWriterService;
import it.lbsoftware.autoi18n.io.impl.PropertyResourceBundlesRetrieverService;
import it.lbsoftware.autoi18n.translations.TranslationEngine;
import it.lbsoftware.autoi18n.utils.LanguageAndCountry;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import picocli.CommandLine.ExitCode;

public final class TranslationEngineFacade {

  private final TranslationEngine translationEngine;
  private final Map<String, String> params;
  private final Map<String, String> entries;
  private final LanguageAndCountry inputLanguageAndCountry;
  private final List<LanguageAndCountry> outputLanguageAndCountries;
  private final File baseDirectory;

  public TranslationEngineFacade(final TranslationEngine translationEngine,
      final Map<String, String> params, final Map<String, String> entries,
      final LanguageAndCountry inputLanguageAndCountry,
      final List<LanguageAndCountry> outputLanguageAndCountries, final File baseDirectory) {
    this.translationEngine = translationEngine;
    this.params = params;
    this.entries = entries;
    this.inputLanguageAndCountry = inputLanguageAndCountry;
    this.outputLanguageAndCountries = outputLanguageAndCountries;
    this.baseDirectory = Optional.ofNullable(baseDirectory).orElse(DEFAULT_BASE_DIRECTORY);
  }

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
                baseDirectory);
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
    var propertyResourceBundleBackupWriter = new PropertyResourceBundleBackupWriterService();
    translations.forEach(
        (languageAndCountry, translationsToWrite) -> {
          if (!propertyResourceBundles.containsKey(languageAndCountry)) {
            return;
          }
          var propertyResourceBundleFile = propertyResourceBundles.get(languageAndCountry);
          // Backup
          propertyResourceBundleBackupWriter.backup(propertyResourceBundleFile,
              new PropertyResourceBundleBackupWriterOptions(
                  Path.of(baseDirectory.getAbsolutePath(), DEFAULT_BACKUP_DIRECTORY_NAME)
                      .toFile()));
          // Write
          if (!propertyResourceBundleWriter.write(
              propertyResourceBundleFile,
              translationsToWrite,
              new PropertyResourceBundleWriterOptions(true, StandardCharsets.ISO_8859_1))) {
            System.err.println("I/O critical error");
          }
        });
    return ExitCode.OK;
  }
}
