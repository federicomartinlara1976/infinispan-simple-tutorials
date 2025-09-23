package org.infinispan.tutorial.simple.spring.remote;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.SecureRandom;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.query.remote.client.ProtobufMetadataManagerConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ReaderTest {

	@Mock
	private BasqueNamesRepository repository;

	@Mock
	private RemoteCacheManager cacheManager;

	@Mock
	private RemoteCache<Object, Object> cache;

	private Reader reader;
	private SecureRandom secureRandom;

	@BeforeEach
	void setUp() {
		// Configuramos un SecureRandom predecible para las pruebas
		secureRandom = new SecureRandom() {
			private static final long serialVersionUID = 1L;
			private int nextIntValue = 5; // Valor fijo para pruebas

			@Override
			public int nextInt(int bound) {
				return nextIntValue;
			}
		};

		// Configuramos los mocks del cache manager
		when(cacheManager.getCache(ProtobufMetadataManagerConstants.PROTOBUF_METADATA_CACHE_NAME))
				.thenReturn(cache);
		when(cacheManager.getCache(Data.BASQUE_NAMES_CACHE)).thenReturn(cache);

		reader = new Reader(repository, cacheManager);

		// Inyectamos el SecureRandom controlado usando reflection
		ReflectionTestUtils.setField(reader, "random", secureRandom);

		// Configuramos el mock del cacheManager
		lenient().when(cacheManager.getCache(Data.BASQUE_NAMES_CACHE)).thenReturn(cache);
	}

	@Test
	void testRetrieveSize() {
		// Given - El cache tiene un tamaño específico
		when(cache.size()).thenReturn(10);

		// When - Ejecutamos el método retrieveSize
		reader.retrieveSize();

		// Then - Debe obtener el tamaño del cache y loguearlo
		verify(cacheManager).getCache(Data.BASQUE_NAMES_CACHE);
		verify(cache).size();
		// El log se verificaría con un appender de log, pero no lo hacemos en unit
		// tests
	}

	@Test
	void testRetrieveSizeWithEmptyCache() {
		// Given - El cache está vacío
		when(cache.size()).thenReturn(0);

		// When - Ejecutamos el método retrieveSize
		reader.retrieveSize();

		// Then - Debe obtener el tamaño del cache (0)
		verify(cacheManager).getCache(Data.BASQUE_NAMES_CACHE);
		verify(cache).size();
	}

	@Test
	void testRetrieveSizeCacheNotAvailable() {
		// Given - El cache manager devuelve null para el cache
		when(cacheManager.getCache(Data.BASQUE_NAMES_CACHE)).thenReturn(null);

		// When & Then - Debe lanzar IllegalArgumentException
		assertThrows(IllegalArgumentException.class, () -> {
			reader.retrieveSize();
		});

		// Then - Debe manejar gracefulmente el cache nulo
		verify(cacheManager).getCache(Data.BASQUE_NAMES_CACHE);
		verify(cache, never()).size(); // No debe llamar a size() en cache nulo
	}

	@Test
	void testRetrieveBasqueName() {
		// Given - Un ID específico (5) y el repositorio configurado

		// When - Ejecutamos el método retrieveBasqueName
		reader.retrieveBasqueName();

		// Then - Debe buscar el nombre por ID en el repositorio
		verify(repository).findById(5);
	}

	@Test
	void testRetrieveSizeMultipleCalls() {
		// Given - El cache tiene diferentes tamaños en cada llamada
		when(cache.size()).thenReturn(5).thenReturn(10).thenReturn(15);

		// When - Ejecutamos el método múltiples veces
		reader.retrieveSize();
		reader.retrieveSize();
		reader.retrieveSize();

		// Then - Debe obtener el tamaño cada vez
		verify(cacheManager, times(3)).getCache(Data.BASQUE_NAMES_CACHE);
		verify(cache, times(3)).size();
	}
	
	@Test
	void testCreateOne() {
		// Given - 
		
		// When - Ejecutamos el método
		reader.createOne();

		// Then - Se llama 1 vez a create
		verify(repository, times(1)).create(any(Integer.class), any(String.class));
	}
	
	@Test
	void testRemoveOne() {
		// Given - 
		
		// When - Ejecutamos el método 
		reader.removeOne();

		// Then - Se llama 1 vez a removeById
		verify(repository, times(1)).removeById(any(Integer.class));
	}
}