package it.lbsoftware.autoi18n.translations.impl.libretranslate.pojos;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

@RegisterForReflection
public record LibreTranslateResponse(@NonNull String translatedText) {
  public static LibreTranslateResponse EMPTY = new LibreTranslateResponse(StringUtils.EMPTY);
}
