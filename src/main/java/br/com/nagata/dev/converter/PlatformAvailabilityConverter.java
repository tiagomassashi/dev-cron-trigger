package br.com.nagata.dev.converter;

import br.com.nagata.dev.model.enums.PlatformAvailabilityEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class PlatformAvailabilityConverter
    implements AttributeConverter<PlatformAvailabilityEnum, String> {

  @Override
  public String convertToDatabaseColumn(PlatformAvailabilityEnum platformAvailabilityEnum) {
    if (platformAvailabilityEnum == null) {
      return null;
    }
    return platformAvailabilityEnum.getCode();
  }

  @Override
  public PlatformAvailabilityEnum convertToEntityAttribute(String code) {
    if (code == null) {
      return null;
    }
    return Stream.of(PlatformAvailabilityEnum.values())
        .filter(c -> c.getCode().equals(code))
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }
}
