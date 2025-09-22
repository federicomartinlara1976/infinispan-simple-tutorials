package org.infinispan.tutorial.simple.spring.embedded;

import java.security.SecureRandom;

import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Reader {

	private final EmbeddedCacheManager cacheManager;
	private final BasqueNamesRepository repository;
	private final SecureRandom random;

	public Reader(BasqueNamesRepository repository, EmbeddedCacheManager embeddedCacheManager) {
		this.repository = repository;
		cacheManager = embeddedCacheManager;
		this.random = new SecureRandom();
	}

	@Scheduled(fixedDelay = 2000)
	public void retrieveSize() {
		Cache<Object, Object> cache = cacheManager.getCache(Data.BASQUE_NAMES_CACHE); 
		Assert.notNull(cache , "Cache cannot be null");
		
		log.info("Cache size {}", cache.size());
	}

	@Scheduled(fixedDelay = 1000)
	public void retrieveBasqueName() {
		int id = this.random.nextInt(Data.NAMES.size());
		log.info("Find name by id '{}'", id);
		this.repository.findById(id);
	}

}
