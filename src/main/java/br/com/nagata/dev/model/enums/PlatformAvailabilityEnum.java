package br.com.nagata.dev.model.enums;

import lombok.Getter;

@Getter
public enum PlatformAvailabilityEnum {
  ON("S"),
  OFF("N");

  private final String code;

  PlatformAvailabilityEnum(String code) {
    this.code = code;
  }
}
