package com.raiden.base;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain=true)
public class Info {
    private String value;
    private boolean isInsertAnnotation;
}