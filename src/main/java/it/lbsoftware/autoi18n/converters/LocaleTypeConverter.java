package it.lbsoftware.autoi18n.converters;

import java.util.Locale;
import java.util.Optional;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine.ITypeConverter;

public class LocaleTypeConverter implements ITypeConverter<Locale> {

  public static String getLanguageAndRegion(@NonNull final Locale locale) {
    return Optional.ofNullable(locale.getLanguage())
        .filter(StringUtils::isNotBlank)
        .map(
            (String language) ->
                language
                    + Optional.ofNullable(locale.getCountry())
                        .filter(StringUtils::isNotBlank)
                        .map((String country) -> "-" + country)
                        .orElse(StringUtils.EMPTY))
        .orElse(StringUtils.EMPTY);
  }

  @Override
  public Locale convert(final String value) {
    return Optional.ofNullable(value)
        .filter(StringUtils::isNotBlank)
        .map(this::toLocale)
        .orElseThrow(IllegalArgumentException::new);
  }

  private Locale toLocale(final String value) {
    var split = value.split("-");
    var localeBuilder = new Locale.Builder().setLanguage(split[0]);
    if (split.length > 1 && StringUtils.isNotBlank(split[1])) {
      localeBuilder.setRegion(split[1]);
    }
    return localeBuilder.build();
  }
}
