package it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3.pojos;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.NonNull;

@RegisterForReflection
public record GoogleCloudTranslationV3Translation(@NonNull String translatedText) {}
