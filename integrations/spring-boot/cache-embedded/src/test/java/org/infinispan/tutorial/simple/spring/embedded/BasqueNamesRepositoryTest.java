package org.infinispan.tutorial.simple.spring.embedded;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;

@ExtendWith(MockitoExtension.class)
class BasqueNamesRepositoryTest {

    private BasqueNamesRepository repository;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @BeforeEach
    void setUp() {
        repository = new BasqueNamesRepository();
    }

    @Test
    void testFindById() {
        // Given - Un ID válido
        Integer id = 5;
        String expectedName = Data.NAMES.get(id);

        // When - Buscamos el nombre por ID
        BasqueName result = repository.findById(id);

        // Then - Debe retornar el objeto BasqueName correcto
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(expectedName, result.getName());
    }

    @Test
    void testFindByIdWithZero() {
        // Given - ID 0 (primero en la lista)
    	Integer id = 0;
        String expectedName = Data.NAMES.get(id);

        // When - Buscamos el nombre por ID
        BasqueName result = repository.findById(id);

        // Then - Debe retornar el primer nombre
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(expectedName, result.getName());
        assertEquals("Aitor", result.getName()); // Verificación explícita
    }

    @Test
    void testFindByIdWithLastIndex() {
        // Given - Último ID válido
    	Integer id = Data.NAMES.size() - 1;
        String expectedName = Data.NAMES.get(id);

        // When - Buscamos el nombre por ID
        BasqueName result = repository.findById(id);

        // Then - Debe retornar el último nombre
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(expectedName, result.getName());
        assertEquals("Itxaso", result.getName()); // Verificación explícita
    }

    @Test
    void testFindByIdWithMiddleIndex() {
        // Given - Un ID en el medio de la lista
    	Integer id = Data.NAMES.size() / 2;
        String expectedName = Data.NAMES.get(id);

        // When - Buscamos el nombre por ID
        BasqueName result = repository.findById(id);

        // Then - Debe retornar el nombre correcto
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(expectedName, result.getName());
    }

    @Test
    void testFindByIdWithInvalidIndex() {
        // Given - Un ID inválido (fuera de rango)
        int invalidId = Data.NAMES.size();

        // When & Then - Debe lanzar IndexOutOfBoundsException
        assertThrows(IndexOutOfBoundsException.class, () -> {
            repository.findById(invalidId);
        });
    }

    @Test
    void testFindByIdWithNegativeIndex() {
        // Given - Un ID negativo
        int negativeId = -1;

        // When & Then - Debe lanzar IndexOutOfBoundsException
        assertThrows(IndexOutOfBoundsException.class, () -> {
            repository.findById(negativeId);
        });
    }

    @Test
    void testFindByIdMultipleCalls() {
        // Given - Múltiples IDs
        int[] ids = {0, 5, 10, 15, Data.NAMES.size() - 1};

        // When - Buscamos múltiples nombres
        for (Integer id : ids) {
            BasqueName result = repository.findById(id);
            
            // Then - Cada resultado debe ser correcto
            assertNotNull(result);
            assertEquals(id, result.getId());
            assertEquals(Data.NAMES.get(id), result.getName());
        }
    }

    @Test
    void testCacheableAnnotation() throws NoSuchMethodException {
        // Given - La clase BasqueNamesRepository
        
        // When - Obtenemos el método findById
        Method findByIdMethod = BasqueNamesRepository.class.getMethod("findById", int.class);
        
        // Then - Debe tener la anotación @Cacheable
        assertNotNull(findByIdMethod.getAnnotation(org.springframework.cache.annotation.Cacheable.class));
    }

    @Test
    void testCacheConfigAnnotation() {
        // Given - La clase BasqueNamesRepository
        
        // Then - Debe tener la anotación @CacheConfig con el cache name correcto
        assertNotNull(BasqueNamesRepository.class.getAnnotation(org.springframework.cache.annotation.CacheConfig.class));
        assertEquals(Data.BASQUE_NAMES_CACHE, 
            BasqueNamesRepository.class.getAnnotation(org.springframework.cache.annotation.CacheConfig.class).cacheNames()[0]);
    }

    @Test
    void testComponentAnnotation() {
        // Given - La clase BasqueNamesRepository
        
        // Then - Debe tener la anotación @Component
        assertNotNull(BasqueNamesRepository.class.getAnnotation(org.springframework.stereotype.Component.class));
    }

    @Test
    void testBasqueNameCreation() {
        // Given - Un ID específico
    	Integer id = 3;
        String expectedName = Data.NAMES.get(id);

        // When - Creamos un BasqueName a través del repositorio
        BasqueName result = repository.findById(id);

        // Then - Debe crear un objeto BasqueName válido
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(expectedName, result.getName());
        
        // Verificamos que equals y hashCode funcionen correctamente
        BasqueName expected = new BasqueName(id, expectedName);
        assertEquals(expected, result);
        assertEquals(expected.hashCode(), result.hashCode());
    }

    @Test
    void testAllNamesAreAccessible() {
        // Given - Todos los IDs válidos
        int totalNames = Data.NAMES.size();

        // When & Then - Debe poder acceder a todos los nombres por su ID
        for (Integer id = 0; id < totalNames; id++) {
            BasqueName result = repository.findById(id);
            assertNotNull(result);
            assertEquals(id, result.getId());
            assertEquals(Data.NAMES.get(id), result.getName());
        }
    }

    @Test
    void testNamesListConsistency() {
        // Given - La lista de nombres del repositorio Data
        
        // When - Verificamos la consistencia de la lista
        List<String> names = Data.NAMES;
        
        // Then - La lista debe tener el tamaño esperado y contener nombres específicos
        assertEquals(35, names.size());
        assertTrue(names.contains("Aitor"));
        assertTrue(names.contains("Amaia"));
        assertTrue(names.contains("Maite"));
        assertTrue(names.contains("Nerea"));
    }

    @Test
    void testRepositoryReturnsNewInstanceEachTime() {
        // Given - Un ID específico
        int id = 7;

        // When - Llamamos al método dos veces
        BasqueName result1 = repository.findById(id);
        BasqueName result2 = repository.findById(id);

        // Then - Deben ser objetos diferentes pero con el mismo contenido
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(result1, result2); // Mismo contenido
        assertNotSame(result1, result2); // Diferentes instancias
    }

    @Test
    void testCacheConfigUsesCorrectCacheName() {
        // Given - La anotación @CacheConfig
        
        // When - Obtenemos la configuración de cache
        CacheConfig cacheConfig = BasqueNamesRepository.class.getAnnotation(CacheConfig.class);
        
        // Then - Debe usar el cache name correcto
        assertNotNull(cacheConfig);
        assertEquals(1, cacheConfig.cacheNames().length);
        assertEquals(Data.BASQUE_NAMES_CACHE, cacheConfig.cacheNames()[0]);
    }

    // Métodos de assertion auxiliares
    private void assertNotNull(Object object) {
        if (object == null) {
            throw new AssertionError("Expected not null");
        }
    }
}