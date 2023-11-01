package it.lbsoftware.autoi18n.constants;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.quarkus.test.junit.QuarkusTest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@QuarkusTest
class ConstantsTests {

  @Test
  @DisplayName("Should not instantiate the class")
  void test1() throws NoSuchMethodException {
    // Given
    Constructor<Constants> constantsConstructor = Constants.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(constantsConstructor.getModifiers()));
    constantsConstructor.setAccessible(true);

    // When
    InvocationTargetException res =
        assertThrows(InvocationTargetException.class, constantsConstructor::newInstance);

    // Then
    assertNotNull(res);
    assertTrue(res.getCause() instanceof UnsupportedOperationException);
  }
}
