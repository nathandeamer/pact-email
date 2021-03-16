package com.nathandeamer.email;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
class OrderEvent {
  private int id;
  private String type;
}