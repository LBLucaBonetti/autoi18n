package it.lbsoftware.autoi18n.converters;

import it.lbsoftware.autoi18n.utils.LanguageAndCountry;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine.ITypeConverter;

public class LanguageAndCountryTypeConverter implements ITypeConverter<LanguageAndCountry> {

  @Override
  public LanguageAndCountry convert(final String value) {
    String[] split = value.split("-");
    String language = split[0];
    String country = split.length > 1 ? split[1] : StringUtils.EMPTY;
    return new LanguageAndCountry(language, country);
  }
}
