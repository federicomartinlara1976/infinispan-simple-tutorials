package org.infinispan.tutorial.simple.spring.embedded;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasqueName implements Serializable {

   private static final long serialVersionUID = -5000048047446171430L;
   private Integer id;
   private String name;

   @Override
   public boolean equals(Object o) {
      return Objects.deepEquals(o, this);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, name);
   }

}
