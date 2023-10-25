package it.lbsoftware.autoi18n.converters;

import java.util.Locale;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine.ITypeConverter;

public class LocaleTypeConverter implements ITypeConverter<Locale> {

  @Override
  public Locale convert(String value) {
    return Optional.ofNullable(value)
        .filter(StringUtils::isNotBlank)
        .map(this::toLocale)
        .orElseThrow(IllegalArgumentException::new);
  }

  private Locale toLocale(String value) {
    var split = value.split("-");
    var localeBuilder = new Locale.Builder().setLanguage(split[0]);
    if (split.length > 1 && StringUtils.isNotBlank(split[1])) {
      localeBuilder.setRegion(split[1]);
    }
    return localeBuilder.build();
  }
}
