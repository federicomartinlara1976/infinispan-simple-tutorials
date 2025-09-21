package org.infinispan.tutorial.simple.spring.embedded;

import java.util.Random;

import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Reader {

   private final EmbeddedCacheManager cacheManager;
   private final BasqueNamesRepository repository;
   private final Random random;

   public Reader(BasqueNamesRepository repository, EmbeddedCacheManager embeddedCacheManager) {
      this.repository = repository;
      cacheManager = embeddedCacheManager;
      this.random = new Random();
   }

   @Scheduled(fixedDelay = 2000)
   public void retrieveSize() {
      log.info("Cache size {}", cacheManager.getCache(Data.BASQUE_NAMES_CACHE).size());
   }

   @Scheduled(fixedDelay = 1000)
   public void retrieveBasqueName() {
      int id = this.random.nextInt(Data.NAMES.size());
      log.info("Find name by id '{}'", id);
      this.repository.findById(id);
   }

}
