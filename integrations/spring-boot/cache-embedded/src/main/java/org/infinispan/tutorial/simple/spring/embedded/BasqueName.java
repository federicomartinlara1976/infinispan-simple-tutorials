package org.infinispan.tutorial.simple.spring.embedded;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BasqueName implements Serializable {

   public static final long serialVersionUID = -5000048047446171430L;
   
   private Integer id;
   
   private String name;
}
