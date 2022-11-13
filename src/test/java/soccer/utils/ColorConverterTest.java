package soccer.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorConverterTest {

    private static Stream<Arguments> provideParamsForTests() {
        return Stream.of(
                Arguments.of("#00ffff", Color.CYAN),
                Arguments.of("#000000", Color.BLACK),
                Arguments.of("#00ff00", Color.GREEN),
                Arguments.of("#ffff00", Color.YELLOW),
                Arguments.of("#808080", Color.GRAY),
                Arguments.of("#ff00ff", Color.MAGENTA),
                Arguments.of("#0000ff", Color.BLUE)
        );
    }
    
    @ParameterizedTest
    @MethodSource("provideParamsForTests")
    void shouldConvertHexStringToColor(String hexString, Color expectedColor) {
        Color actualColor = ColorConverter.convertHexStringToColor(hexString);
        assertEquals(expectedColor, actualColor);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForTests")
    void shouldConvertColorToHexString(String expectedHexString, Color color) {
        String actualHexString = ColorConverter.convertColorToHexString(color);
        assertEquals(expectedHexString, actualHexString);
    }
}