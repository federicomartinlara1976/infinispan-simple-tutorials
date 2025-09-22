package org.infinispan.tutorial.simple.spring.remote;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@CacheConfig(cacheNames = Data.BASQUE_NAMES_CACHE)
@Slf4j
public class BasqueNamesRepository {

   private Map<String, BasqueName> database = new HashMap<>();

   @Cacheable()
   public BasqueName findById(String id) {
      log.info("Call database to FIND name by id '{}'", id);
      return database.get(id);
   }

   public void create(String id, String name) {
      log.info("Call database to CREATE name by id '{}'", id);
      database.put(id, new BasqueName(id, name));
   }

   @CacheEvict
   public void removeById(String id) {
      log.info("Call database to REMOVE name by id '{}'", id);
      database.remove(id);
   }

   public int size() {
      return database.size();
   }

}
