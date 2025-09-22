package org.infinispan.tutorial.simple.spring.embedded;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@ExtendWith(MockitoExtension.class)
class BasqueNamesCachingAppTest {

    private MockedStatic<SpringApplication> springApplicationMock;

    @AfterEach
    void tearDown() {
        if (springApplicationMock != null) {
            springApplicationMock.close();
        }
    }

    @Test
    void testSpringBootApplicationAnnotation() {
        // Given - La clase BasqueNamesCachingApp
        
        // Then - Debe tener la anotación @SpringBootApplication
        assertNotNull(BasqueNamesCachingApp.class.getAnnotation(
            org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }

    @Test
    void testEnableCachingAnnotation() {
        // Given - La clase BasqueNamesCachingApp
        
        // Then - Debe tener la anotación @EnableCaching
        assertNotNull(BasqueNamesCachingApp.class.getAnnotation(
            org.springframework.cache.annotation.EnableCaching.class));
    }

    @Test
    void testEnableSchedulingAnnotation() {
        // Given - La clase BasqueNamesCachingApp
        
        // Then - Debe tener la anotación @EnableScheduling
        assertNotNull(BasqueNamesCachingApp.class.getAnnotation(
            org.springframework.scheduling.annotation.EnableScheduling.class));
    }

    @Test
    void testMainMethodExists() throws NoSuchMethodException {
        // Given - La clase BasqueNamesCachingApp
        
        // When - Obtenemos el método main
        Method mainMethod = BasqueNamesCachingApp.class.getMethod("main", String[].class);
        
        // Then - Debe ser público, estático y void
        assertTrue(Modifier.isPublic(mainMethod.getModifiers()));
        assertTrue(Modifier.isStatic(mainMethod.getModifiers()));
        assertEquals(void.class, mainMethod.getReturnType());
    }

    @Test
    void testMainMethodCallsSpringApplicationRun() {
        // Given - Mock de SpringApplication
        springApplicationMock = mockStatic(SpringApplication.class);
        ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);
        
        springApplicationMock.when(() -> SpringApplication.run(BasqueNamesCachingApp.class))
                           .thenReturn(mockContext);

        String[] args = {"test-arg1", "test-arg2"};

        // When - Llamamos al método main
        BasqueNamesCachingApp.main(args);

        // Then - Debe llamar a SpringApplication.run con los parámetros correctos
        springApplicationMock.verify(() -> 
            SpringApplication.run(eq(BasqueNamesCachingApp.class)));
    }

    @Test
    void testMainMethodWithNullArgs() {
        // Given - Mock de SpringApplication
        springApplicationMock = mockStatic(SpringApplication.class);
        ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);
        
        springApplicationMock.when(() -> SpringApplication.run(BasqueNamesCachingApp.class))
                           .thenReturn(mockContext);

        // When - Llamamos al método main con args nulo
        BasqueNamesCachingApp.main(null);

        // Then - Debe llamar a SpringApplication.run con args nulo
        springApplicationMock.verify(() -> 
            SpringApplication.run(eq(BasqueNamesCachingApp.class)));
    }

    @Test
    void testMainMethodWithEmptyArgs() {
        // Given - Mock de SpringApplication
        springApplicationMock = mockStatic(SpringApplication.class);
        ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);
        
        springApplicationMock.when(() -> SpringApplication.run(any(Class.class), any(String[].class)))
                           .thenReturn(mockContext);

        String[] args = {};

        // When - Llamamos al método main con args vacío
        BasqueNamesCachingApp.main(args);

        // Then - Debe llamar a SpringApplication.run con args vacío
        springApplicationMock.verify(() -> 
            SpringApplication.run(eq(BasqueNamesCachingApp.class), eq(args)));
    }

    @Test
    void testClassIsPublic() {
        // Given - La clase BasqueNamesCachingApp
        
        // Then - Debe ser pública
        assertTrue(Modifier.isPublic(BasqueNamesCachingApp.class.getModifiers()));
    }

    @Test
    void testClassHasNoArgsConstructor() {
        // Given - La clase BasqueNamesCachingApp
        
        // When - Intentamos crear una instancia
        BasqueNamesCachingApp app = new BasqueNamesCachingApp();
        
        // Then - Debe crearse correctamente (constructor por defecto)
        assertNotNull(app);
    }

    @Test
    void testAnnotationsConfiguration() {
        // Given - Las anotaciones de la clase
        
        // When - Obtenemos las anotaciones
        SpringBootApplication springBootAnnotation = BasqueNamesCachingApp.class
            .getAnnotation(org.springframework.boot.autoconfigure.SpringBootApplication.class);
        
        EnableCaching enableCachingAnnotation = BasqueNamesCachingApp.class
            .getAnnotation(org.springframework.cache.annotation.EnableCaching.class);
        
        EnableScheduling enableSchedulingAnnotation = BasqueNamesCachingApp.class
            .getAnnotation(org.springframework.scheduling.annotation.EnableScheduling.class);

        // Then - Todas las anotaciones deben estar presentes
        assertNotNull(springBootAnnotation);
        assertNotNull(enableCachingAnnotation);
        assertNotNull(enableSchedulingAnnotation);
    }

    @Test
    void testMainMethodParameters() throws NoSuchMethodException {
        // Given - El método main
        
        // When - Obtenemos los parámetros del método
        Method mainMethod = BasqueNamesCachingApp.class.getMethod("main", String[].class);
        Class<?>[] parameterTypes = mainMethod.getParameterTypes();
        
        // Then - Debe tener un solo parámetro de tipo String[]
        assertEquals(1, parameterTypes.length);
        assertEquals(String[].class, parameterTypes[0]);
    }

    @Test
    void testApplicationContextCreation() {
        // Given - Mock de SpringApplication
        springApplicationMock = mockStatic(SpringApplication.class);
        ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);
        
        springApplicationMock.when(() -> SpringApplication.run(BasqueNamesCachingApp.class))
                           .thenReturn(mockContext);

        String[] args = {"--spring.profiles.active=test"};

        // When - Llamamos al método main
        BasqueNamesCachingApp.main(args);

        // Then - Debe retornar el contexto de la aplicación
        springApplicationMock.verify(() -> 
            SpringApplication.run(eq(BasqueNamesCachingApp.class)));
    }

    @Test
    void testClassStructure() {
        // Given - La clase BasqueNamesCachingApp
        
        // Then - Debe ser una clase pública y no abstracta
        int modifiers = BasqueNamesCachingApp.class.getModifiers();
        assertTrue(Modifier.isPublic(modifiers));
        assertFalse(Modifier.isAbstract(modifiers));
        assertFalse(Modifier.isInterface(modifiers));
    }

    @Test
    void testPackageAndClassName() {
        // Given - La clase BasqueNamesCachingApp
        
        // Then - Debe estar en el paquete correcto y tener el nombre correcto
        assertEquals("org.infinispan.tutorial.simple.spring.embedded", 
                    BasqueNamesCachingApp.class.getPackageName());
        assertEquals("BasqueNamesCachingApp", BasqueNamesCachingApp.class.getSimpleName());
    }

    // Métodos de assertion auxiliares
    private void assertNotNull(Object object) {
        if (object == null) {
            throw new AssertionError("Expected not null");
        }
    }

    private void assertEquals(Object expected, Object actual) {
        if (expected == null && actual == null) return;
        if (expected == null || !expected.equals(actual)) {
            throw new AssertionError("Expected: " + expected + ", but was: " + actual);
        }
    }

    private void assertEquals(int expected, int actual) {
        if (expected != actual) {
            throw new AssertionError("Expected: " + expected + ", but was: " + actual);
        }
    }

    private void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected true");
        }
    }

    private void assertFalse(boolean condition) {
        if (condition) {
            throw new AssertionError("Expected false");
        }
    }
}