package com.dlt.kafkadlt.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String NORMAL_TOPIC = "normal";
    public static final String RETRYABLE_SINGLE_TOPIC = "retryable-single";
    public static final String RETRYABLE_DEFAULT_TOPIC = "retryable-default";
}

