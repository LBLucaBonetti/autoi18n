package it.lbsoftware.autoi18n.utils;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class LanguageAndCountry {
  private static final Pattern ISO639 = Pattern.compile("^[a-zA-Z]{2}$");
  private final String language;
  private final String country;

  public LanguageAndCountry(final String language) {
    this(language, null);
  }

  public LanguageAndCountry(final String language, final String country) {
    // The language is traditionally lowercase and is required
    this.language =
        Optional.ofNullable(language)
            .filter(StringUtils::isNotBlank)
            .map(String::strip)
            .filter((String l) -> ISO639.matcher(l).matches())
            .map(StringUtils::toRootLowerCase)
            .orElseThrow(IllegalArgumentException::new);
    // The country is traditionally uppercase and is optional
    this.country =
        Optional.ofNullable(country)
            .filter(StringUtils::isNotBlank)
            .map(String::strip)
            .filter((String c) -> ISO639.matcher(c).matches())
            .map(StringUtils::toRootUpperCase)
            .orElse(StringUtils.EMPTY);
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder(language);
    if (StringUtils.isNotBlank(country)) {
      stringBuilder.append("-");
      stringBuilder.append(country);
    }
    return stringBuilder.toString();
  }

  /**
   * Comparison based on language field only; two LanguageAndCountry instances are equal if they
   * have the same language field (i.e.: {@code assertTrue(new
   * LanguageAndCountry("en-US").equals(new LanguageAndCountry("en");})
   *
   * @param obj the reference object with which to compare
   * @return Whether the objects are equal or not
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof LanguageAndCountry that)) {
      return false;
    }
    // The following check should not produce NullPointerException because of constructor checks
    return language.equals(that.language);
  }

  /**
   * Hash based on language field only
   *
   * @return The hash for the language field only
   */
  @Override
  public int hashCode() {
    return Objects.hash(language);
  }
}
