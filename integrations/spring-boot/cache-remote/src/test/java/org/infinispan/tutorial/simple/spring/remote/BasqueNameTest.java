package org.infinispan.tutorial.simple.spring.remote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BasqueNameTest {

    private BasqueName basqueName;
    private BasqueName sameBasqueName;
    private BasqueName differentBasqueName;

    @BeforeEach
    void setUp() {
        // Given - Configuración inicial para los tests
        basqueName = new BasqueName(1, "Aitor");
        sameBasqueName = new BasqueName(1, "Aitor");
        differentBasqueName = new BasqueName(2, "Maite");
    }

    @Test
    void testDefaultConstructor() {
        // When - Creamos un objeto con el constructor por defecto
        BasqueName emptyBasqueName = new BasqueName(null, null);
        
        // Then - Verificamos que se crea correctamente con valores null
        assertNotNull(emptyBasqueName);
        assertNull(emptyBasqueName.id());
        assertNull(emptyBasqueName.name());
    }

    @Test
    void testParameterizedConstructor() {
        // When - Creamos un objeto con el constructor parametrizado
        BasqueName testBasqueName = new BasqueName(3, "Jon");
        
        // Then - Verificamos que los valores se asignan correctamente
        assertEquals(3, testBasqueName.id());
        assertEquals("Jon", testBasqueName.name());
    }

    @Test
    void testEqualsSameObject() {
        // When & Then - Un objeto debe ser igual a sí mismo
    	assertEquals(basqueName, basqueName);
    }

    @Test
    void testEqualsNull() {
        // When & Then - Un objeto no debe ser igual a null
    	assertNotEquals(null, basqueName);
    }

    @Test
    void testEqualsDifferentClass() {
        // When & Then - Un objeto no debe ser igual a un objeto de otra clase
    	assertNotEquals("Not a BasqueName", basqueName);
    }

    @Test
    void testEqualsSameValues() {
        // When & Then - Objetos con los mismos valores deben ser iguales
    	assertEquals(basqueName, sameBasqueName);
    	assertEquals(sameBasqueName, basqueName);
    }

    @Test
    void testEqualsDifferentValues() {
        // When & Then - Objetos con diferentes valores no deben ser iguales
    	assertNotEquals(basqueName, differentBasqueName);
        assertNotEquals(differentBasqueName, basqueName);
    }

    @Test
    void testEqualsWithNullId() {
        // Given - Objetos con ID nulo
        BasqueName basqueName1 = new BasqueName(null, "Aitor");
        BasqueName basqueName2 = new BasqueName(1, "Aitor");
        
        // When & Then - No deben ser iguales
        assertNotEquals(basqueName1, basqueName2);
        assertNotEquals(basqueName2, basqueName1);
    }

    @Test
    void testEqualsBothNullId() {
        // Given - Objetos con ID nulo
        BasqueName basqueName1 = new BasqueName(null, "Aitor");
        BasqueName basqueName2 = new BasqueName(null, "Aitor");
        
        // When & Then - Deben ser iguales si todos los campos son iguales
        assertEquals(basqueName1, basqueName2);
        assertEquals(basqueName2, basqueName1);
    }

    @Test
    void testEqualsWithNullName() {
        // Given - Objetos con nombre nulo
        BasqueName basqueName1 = new BasqueName(1, null);
        BasqueName basqueName2 = new BasqueName(1, "Aitor");
        
        // When & Then - No deben ser iguales
        assertNotEquals(basqueName1, basqueName2);
        assertNotEquals(basqueName2, basqueName1);
    }

    @Test
    void testEqualsBothNullName() {
        // Given - Objetos con nombre nulo
        BasqueName basqueName1 = new BasqueName(1, null);
        BasqueName basqueName2 = new BasqueName(1, null);
        
        // When & Then - Deben ser iguales si todos los campos son iguales
        assertEquals(basqueName1, basqueName2);
        assertEquals(basqueName2, basqueName1);
    }

    @Test
    void testHashCodeSameValues() {
        // When & Then - Objetos iguales deben tener el mismo hashCode
        assertEquals(basqueName.hashCode(), sameBasqueName.hashCode());
    }

    @Test
    void testHashCodeDifferentValues() {
        // When & Then - Objetos diferentes deben tener diferente hashCode
        assertNotEquals(basqueName.hashCode(), differentBasqueName.hashCode());
    }

    @Test
    void testHashCodeWithNullValues() {
        // Given - Objetos con valores nulos
        BasqueName basqueName1 = new BasqueName(null, null);
        BasqueName basqueName2 = new BasqueName(null, null);
        
        // When & Then - Deben tener el mismo hashCode
        assertEquals(basqueName1.hashCode(), basqueName2.hashCode());
    }

    @Test
    void testToString() {
        // When - Obtenemos la representación String del objeto
        String toStringResult = basqueName.toString();
        
        // Then - Debe contener información relevante del objeto
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("1"));
        assertTrue(toStringResult.contains("Aitor"));
    }

    @Test
    void testToStringWithNullValues() {
        // Given - Un objeto con valores nulos
        BasqueName emptyBasqueName = new BasqueName(null, null);
        
        // When - Obtenemos la representación String del objeto
        String toStringResult = emptyBasqueName.toString();
        
        // Then - Debe ser una cadena no nula
        assertNotNull(toStringResult);
    }

    @Test
    void testLombokFunctionality() {
        // Given - Un objeto con valores
        BasqueName original = new BasqueName(5, "Gorka");
        
        // When - Creamos un nuevo objeto con los mismos valores
        BasqueName copy = new BasqueName(5, "Gorka");
        
        // Then - Deben ser iguales y tener el mismo hashCode
        assertEquals(original, copy);
        assertEquals(original.hashCode(), copy.hashCode());
        
        // And - Verificamos los getters
        assertEquals(5, original.id());
        assertEquals("Gorka", original.name());
        
        // And - Verificamos que toString funciona
        assertNotNull(original.toString());
    }
}