package org.infinispan.tutorial.simple.spring.remote;

import java.security.SecureRandom;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.protostream.GeneratedSchema;
import org.infinispan.query.remote.client.ProtobufMetadataManagerConstants;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Reader {

   private final BasqueNamesRepository repository;
   private final SecureRandom random;
   private final RemoteCacheManager remoteCacheManager;

   public Reader(BasqueNamesRepository repository, RemoteCacheManager remoteCacheManager) {
      this.repository = repository;
      random = new SecureRandom();
      this.remoteCacheManager = remoteCacheManager;
      // Upload the generated schema in the server
      RemoteCache<String, String> metadataCache = this.remoteCacheManager.getCache(ProtobufMetadataManagerConstants.PROTOBUF_METADATA_CACHE_NAME);
      GeneratedSchema schema = new BasquesNamesSchemaBuilderImpl();
      metadataCache.put(schema.getProtoFileName(), schema.getProtoFile());
   }

   @Scheduled(fixedDelay = 10000)
   public void retrieveSize() {
      log.info(">>>> Cache size {}", remoteCacheManager.getCache(Data.BASQUE_NAMES_CACHE).size());
      log.info(">>>> Database size {}",  repository.size());
   }

   @Scheduled(fixedDelay = 1000)
   public void createOne() {
      int id = this.random.nextInt(Data.NAMES.size());
      this.repository.create(Integer.toString(id), Data.NAMES.get(id));
   }

   @Scheduled(fixedDelay = 3000)
   public void removeOne() {
      int id = this.random.nextInt(Data.NAMES.size());
      this.repository.removeById(Integer.toString(id));
   }

   @Scheduled(fixedDelay = 1000)
   public void retrieveBasqueName() {
      int id = this.random.nextInt(Data.NAMES.size());
      log.info("FIND RESULT {}", this.repository.findById(Integer.toString(id)));
   }
}
