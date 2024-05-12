package com.github.shiraji.namesticker.enums

import com.intellij.ui.JBColor
import java.awt.Color

enum class ValidForegroundColors(val value: Color) {
    RED(JBColor.RED),
    BLUE(JBColor.BLUE),
    WHITE(JBColor(Color.WHITE, Color.WHITE)), // I really don't like JBColor.WHITE. It's not white. It's gray in dark theme.
    BLACK(JBColor(Color.BLACK, Color.BLACK)),
    GRAY(JBColor.GRAY),
    LIGHT_GRAY(JBColor.LIGHT_GRAY),
    DARK_GRAY(JBColor.DARK_GRAY),
    PINK(JBColor.PINK),
    ORANGE(JBColor.ORANGE),
    YELLOW(JBColor.YELLOW),
    GREEN(JBColor.GREEN),
    MAGENTA(JBColor.MAGENTA),
    CYAN(JBColor.CYAN)
}