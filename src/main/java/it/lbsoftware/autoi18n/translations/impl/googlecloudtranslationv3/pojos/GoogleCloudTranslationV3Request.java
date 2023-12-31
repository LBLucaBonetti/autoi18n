package it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3.pojos;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.List;

@RegisterForReflection
public record GoogleCloudTranslationV3Request(
    List<String> contents, String sourceLanguageCode, String targetLanguageCode) {}
