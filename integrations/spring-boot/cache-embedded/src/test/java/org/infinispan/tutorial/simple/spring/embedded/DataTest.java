package org.infinispan.tutorial.simple.spring.embedded;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;

import org.junit.jupiter.api.Test;

class DataTest {

    @Test
    void testBasqueNamesCacheConstant() {
        // Given - La constante definida en la clase Data
        
        // When - Accedemos a la constante
        String cacheName = Data.BASQUE_NAMES_CACHE;
        
        // Then - Debe tener el valor esperado
        assertEquals("basque-names", cacheName);
    }

    @Test
    void testNamesListConstant() {
        // Given - La lista de nombres definida en la clase Data
        
        // When - Accedemos a la lista de nombres (usando reflection ya que es protected)
        List<String> names = getNamesListViaReflection();
        
        // Then - Debe contener los nombres esperados
        assertNotNull(names);
        assertEquals(35, names.size()); // Verificamos el tamaño de la lista
        
        // Verificamos que contiene algunos nombres específicos
        assertTrue(names.contains("Aitor"));
        assertTrue(names.contains("Amaia"));
        assertTrue(names.contains("Maite"));
        assertTrue(names.contains("Xabier"));
        assertTrue(names.contains("Nerea"));
        
        // Verificamos el orden de algunos elementos
        assertEquals("Aitor", names.get(0));
        assertEquals("Ander", names.get(1));
        assertEquals("Gaizka", names.get(16)); // Primer nombre femenino en la lista
    }

    @Test
    void testNamesListContent() {
        // Given - La lista de nombres definida en la clase Data
        
        // When - Accedemos a la lista de nombres
        List<String> names = getNamesListViaReflection();
        
        // Then - Verificamos el contenido completo de la lista
        List<String> expectedNames = List.of(
            "Aitor", "Ander", "Andoni", "Asier", "Eneko", "Gorka", "Koldo", "Mattin", "Xabier",
            "Galder", "Iker", "Unai", "Jon", "Markel", "Hodei", "Kepa", "Gaizka", "Imanol",
            "Amaia", "Ane", "Arantxa", "Edurne", "Josune", "Maialen", "Maite", "Miren", "Leire",
            "Nekane", "Oihana", "Elaia", "Nahia", "Nerea", "Izaro", "Neskutz", "Itxaso"
        );
        
        assertEquals(expectedNames, names);
    }

    @Test
    void testPrivateConstructor() throws Exception {
        // Given - La clase Data con constructor privado
        
        // When - Intentamos acceder al constructor
        Constructor<Data> constructor = Data.class.getDeclaredConstructor();
        
        // Then - El constructor debe ser privado
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        
        // And - Debe ser accesible para testing
        constructor.setAccessible(true);
    }

    @Test
    void testClassIsFinal() {
        // Given - La clase Data
        
        // When - Verificamos los modificadores de la clase
        
        // Then - La clase debe ser final
        assertTrue(Modifier.isFinal(Data.class.getModifiers()));
    }

    @Test
    void testConstantsArePublic() throws Exception {
        // Given - Los campos de la clase Data
        
        // When - Verificamos los modificadores de los campos
        var basqueNamesCacheField = Data.class.getField("BASQUE_NAMES_CACHE");
        var namesField = Data.class.getDeclaredField("NAMES");
        
        // Then - BASQUE_NAMES_CACHE debe ser public
        assertTrue(Modifier.isPublic(basqueNamesCacheField.getModifiers()));
        assertTrue(Modifier.isStatic(basqueNamesCacheField.getModifiers()));
        assertTrue(Modifier.isFinal(basqueNamesCacheField.getModifiers()));
        
        // And - NAMES debe ser protected static final
        assertTrue(Modifier.isProtected(namesField.getModifiers()));
        assertTrue(Modifier.isStatic(namesField.getModifiers()));
        assertTrue(Modifier.isFinal(namesField.getModifiers()));
    }

    @Test
    void testNamesListImmutable() {
        // Given - La lista de nombres
        
        // When - Accedemos a la lista
        List<String> names = getNamesListViaReflection();
        
        // Then - Debe ser una lista inmutable (no modificable)
        assertThrows(UnsupportedOperationException.class, () -> names.add("NuevoNombre"));
        assertThrows(UnsupportedOperationException.class, () -> names.remove(0));
        assertThrows(UnsupportedOperationException.class, names::clear);
    }

    @Test
    void testCacheNameFormat() {
        // Given - El nombre del cache
        
        // When - Accedemos a la constante
        String cacheName = Data.BASQUE_NAMES_CACHE;
        
        // Then - Debe seguir un formato consistente (kebab-case)
        assertTrue(cacheName.matches("[a-z-]+"));
        assertEquals("basque-names", cacheName);
    }

    @Test
    void testNamesListContainsBothGenders() {
        // Given - La lista de nombres
        
        // When - Accedemos a la lista
        List<String> names = getNamesListViaReflection();
        
        // Then - Debe contener nombres tanto masculinos como femeninos
        List<String> maleNames = List.of("Aitor", "Ander", "Andoni", "Asier", "Eneko", "Gorka", 
                                       "Koldo", "Mattin", "Xabier", "Galder", "Iker", "Unai", 
                                       "Jon", "Markel", "Hodei", "Kepa", "Gaizka", "Imanol");
        
        List<String> femaleNames = List.of("Amaia", "Ane", "Arantxa", "Edurne", "Josune", 
                                         "Maialen", "Maite", "Miren", "Leire", "Nekane", 
                                         "Oihana", "Elaia", "Nahia", "Nerea", "Izaro", 
                                         "Neskutz", "Itxaso");
        
        // Verificamos que todos los nombres masculinos están en la lista
        for (String maleName : maleNames) {
            assertTrue(names.contains(maleName), "Falta nombre masculino: " + maleName);
        }
        
        // Verificamos que todos los nombres femeninos están en la lista
        for (String femaleName : femaleNames) {
            assertTrue(names.contains(femaleName), "Falta nombre femenino: " + femaleName);
        }
    }

    @Test
    void testNamesListOrder() {
        // Given - La lista de nombres
        
        // When - Accedemos a la lista
        List<String> names = getNamesListViaReflection();
        
        // Then - Los nombres deben estar en orden alfabético (aproximado)
        // Verificamos que los primeros nombres son los esperados
        assertEquals("Aitor", names.get(0));
        assertEquals("Ander", names.get(1));
        assertEquals("Andoni", names.get(2));
        
        // Verificamos que los últimos nombres son los esperados
        assertEquals("Neskutz", names.get(names.size() - 2));
        assertEquals("Itxaso", names.get(names.size() - 1));
    }

    // Método auxiliar para acceder a la lista NAMES mediante reflection
    private List<String> getNamesListViaReflection() {
        try {
            var namesField = Data.class.getDeclaredField("NAMES");
            namesField.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<String> names = (List<String>) namesField.get(null);
            return names;
        } catch (Exception e) {
            throw new RuntimeException("Error accediendo a la lista NAMES mediante reflection", e);
        }
    }
}