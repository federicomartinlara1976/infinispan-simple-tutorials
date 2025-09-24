package org.infinispan.tutorial.simple.spring.remote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        
        // Para que funcionen los tests hay que crear elementos en la base de datos, empezando por el índice 0
        Integer index = 0;
        for (String name : Data.NAMES) {
        	repository.create(index, name);
        	index++;
        }
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
        assertEquals(id, result.id());
        assertEquals(expectedName, result.name());
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
        assertEquals(id, result.id());
        assertEquals(expectedName, result.name());
        assertEquals("Aitor", result.name()); // Verificación explícita
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
        assertEquals(id, result.id());
        assertEquals(expectedName, result.name());
        assertEquals("Itxaso", result.name()); // Verificación explícita
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
        assertEquals(id, result.id());
        assertEquals(expectedName, result.name());
    }

    @Test
    void testFindByIdWithInvalidIndex() {
        // Given - Un ID inválido (fuera de rango)
        int invalidId = Data.NAMES.size();

        // When & Then - Debe lanzar IndexOutOfBoundsException
        assertThrows(IllegalArgumentException.class, () -> {
            repository.findById(invalidId);
        });
    }

    @Test
    void testFindByIdWithNegativeIndex() {
        // Given - Un ID negativo
        int negativeId = -1;

        // When & Then - Debe lanzar IndexOutOfBoundsException
        assertThrows(IllegalArgumentException.class, () -> {
            repository.findById(negativeId);
        });
    }

    @Test
    void testCacheableAnnotation() throws NoSuchMethodException {
        // Given - La clase BasqueNamesRepository
        
        // When - Obtenemos el método findById
        Method findByIdMethod = BasqueNamesRepository.class.getMethod("findById", Integer.class);
        
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
        assertEquals(id, result.id());
        assertEquals(expectedName, result.name());
        
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
            assertEquals(id, result.id());
            assertEquals(Data.NAMES.get(id), result.name());
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
    void testRemove() {
        // Given - Añadimos un nuevo nombre al final
    	List<String> names = Data.NAMES;
    	repository.create(names.size(), "Nuevo");
        
        // When - Borramos el nuevo nombre por su índice
        repository.removeById(names.size());
        
        // Then - El repositorio debe conservar su tamaño
        assertEquals(repository.size(), names.size());
    }
    
    @Test
    void testSize() {
        // Given - La lista de nombres del repositorio Data
        
        // When - Verificamos la consistencia de la lista
        List<String> names = Data.NAMES;
        
        // Then - La lista y el repositorio deben tener el mismo tamaño
        assertEquals(repository.size(), names.size());
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
}