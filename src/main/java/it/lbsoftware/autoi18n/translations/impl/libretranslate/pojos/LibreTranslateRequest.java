package it.lbsoftware.autoi18n.translations.impl.libretranslate.pojos;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record LibreTranslateRequest(String q, String source, String target) {}
