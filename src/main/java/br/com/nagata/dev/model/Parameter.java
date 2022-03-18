package br.com.nagata.dev.model;

import br.com.nagata.dev.model.enums.PlatformAvailabilityEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_PARM_SCHD", schema = "PUBLIC")
public class Parameter {

  @Id
  @Column(name = "CD_PARM_SCHD", length = 40, nullable = false)
  private String id;

  @Column(name = "IN_DISP_PLAT", length = 1, nullable = false)
  private PlatformAvailabilityEnum platformAvailability;

  @Column(name = "HR_ABER", nullable = false)
  private LocalTime openingTime;

  @Column(name = "HR_FECH", nullable = false)
  private LocalTime closingTime;

  @Column(name = "HR_PERI", nullable = false)
  private LocalTime periodTime;
}
