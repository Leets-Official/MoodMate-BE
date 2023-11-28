package com.moodmate.moodmatebe.global.oauth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public abstract class OAuthAttribute {
    protected Map<String, Object> attributes;

    public abstract String getName();

    public abstract String getEmail();
}
