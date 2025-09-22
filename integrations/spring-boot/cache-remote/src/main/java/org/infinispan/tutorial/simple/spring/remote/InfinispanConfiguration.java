package org.infinispan.tutorial.simple.spring.remote;

import java.net.URI;
import java.net.URISyntaxException;

import org.infinispan.commons.marshall.ProtoStreamMarshaller;
import org.infinispan.spring.starter.remote.InfinispanRemoteCacheCustomizer;
import org.infinispan.tutorial.simple.spring.remote.exceptions.ConfigurationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import lombok.SneakyThrows;

@Configuration
public class InfinispanConfiguration {

   @Bean
   @Order(Ordered.HIGHEST_PRECEDENCE)
   @SneakyThrows(ConfigurationException.class)
   InfinispanRemoteCacheCustomizer caches() {
      return b -> {
         URI cacheConfigUri;
         try {
            cacheConfigUri = this.getClass().getClassLoader().getResource("basquesNamesCache.xml").toURI();
         } catch (URISyntaxException e) {
            throw new ConfigurationException(e);
         }

         b.remoteCache(Data.BASQUE_NAMES_CACHE)
                 .configurationURI(cacheConfigUri);

         b.remoteCache(Data.BASQUE_NAMES_CACHE).marshaller(ProtoStreamMarshaller.class);

         // Add marshaller in the client, the class is generated from the interface in compile time
         b.addContextInitializer(new BasquesNamesSchemaBuilderImpl());
      };
   }
}
