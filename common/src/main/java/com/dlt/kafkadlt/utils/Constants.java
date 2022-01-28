package com.dlt.kafkadlt.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String INVALID_FORMAT_NORMAL_TOPIC = "invalid-format-normal";
    public static final String NORMAL_TOPIC = "normal";
    public static final String RETRYABLE_SINGLE_TOPIC = "retryable-single";
    public static final String RETRYABLE_SINGLE_TOPIC_INVALID = "retryable-single-invalid-format";
    public static final String RETRYABLE_DEFAULT_TOPIC = "retryable-default";
}

