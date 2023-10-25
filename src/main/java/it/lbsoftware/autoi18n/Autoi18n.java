package it.lbsoftware.autoi18n;

import static it.lbsoftware.autoi18n.constants.Constants.AUTOI18N_NAME;
import static it.lbsoftware.autoi18n.constants.Constants.OPTION_LONG_ENTRY;
import static it.lbsoftware.autoi18n.constants.Constants.OPTION_LONG_INPUT_LANGUAGE;
import static it.lbsoftware.autoi18n.constants.Constants.OPTION_LONG_OUTPUT_LANGUAGES;
import static it.lbsoftware.autoi18n.constants.Constants.OPTION_LONG_TRANSLATION_ENGINE;
import static it.lbsoftware.autoi18n.constants.Constants.OPTION_SHORT_ENTRY;
import static it.lbsoftware.autoi18n.constants.Constants.OPTION_SHORT_INPUT_LANGUAGE;
import static it.lbsoftware.autoi18n.constants.Constants.OPTION_SHORT_OUTPUT_LANGUAGES;
import static it.lbsoftware.autoi18n.constants.Constants.OPTION_SHORT_TRANSLATION_ENGINE;

import it.lbsoftware.autoi18n.converters.LocaleTypeConverter;
import it.lbsoftware.autoi18n.translations.TranslationEngine;
import it.lbsoftware.autoi18n.utils.VersionProvider;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

@Command(
    name = AUTOI18N_NAME,
    versionProvider = VersionProvider.class,
    mixinStandardHelpOptions = true)
public class Autoi18n implements Callable<Integer> {

  @Getter
  @Option(
      names = {OPTION_SHORT_OUTPUT_LANGUAGES, OPTION_LONG_OUTPUT_LANGUAGES},
      required = true,
      description =
          "The target languages, according to the ISO 639 alpha-2 or alpha-3 standard, case insensitive.",
      paramLabel = "<output-language>",
      split = ",",
      splitSynopsisLabel = ",",
      converter = LocaleTypeConverter.class)
  private LinkedHashSet<Locale> outputLocales;

  @Getter
  @Option(
      names = {OPTION_SHORT_ENTRY, OPTION_LONG_ENTRY},
      required = true,
      description =
          "Key-value item(s) to translate; an unspecified or blank value will not be translated nor inserted into language files.",
      paramLabel = "<key>=<value>",
      mapFallbackValue = StringUtils.EMPTY,
      split = ",",
      splitSynopsisLabel = ",")
  private Map<String, String> entries;

  @Getter
  @Option(
      names = {OPTION_SHORT_INPUT_LANGUAGE, OPTION_LONG_INPUT_LANGUAGE},
      required = true,
      description =
          "The source language, according to the ISO 639 alpha-2 or alpha-3 standard, case insensitive.",
      paramLabel = "<input-language>",
      converter = LocaleTypeConverter.class)
  private Locale inputLocale;

  @Getter
  @Option(
      names = {OPTION_SHORT_TRANSLATION_ENGINE, OPTION_LONG_TRANSLATION_ENGINE},
      required = false,
      description = "The translation engine to use",
      paramLabel = "<translationEngine>")
  private TranslationEngine translationEngine = TranslationEngine.GOOGLE_CLOUD_TRANSLATION_V3;

  @Spec private CommandSpec commandSpec;

  @Override
  public Integer call() {
    commandSpec.commandLine().usage(commandSpec.commandLine().getOut());
    return 0;
  }
}
