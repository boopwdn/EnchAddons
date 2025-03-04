package net.skymoe.enchaddons.util

import net.skymoe.enchaddons.feature.config.Color
import net.skymoe.enchaddons.util.math.Vec3D
import org.lwjgl.opengl.GL11.*

inline fun <R> glMatrixScope(function: () -> R) {
    glPushMatrix()
    try {
        function()
    } finally {
        glPopMatrix()
    }
}

inline fun <R> glAttribScope(function: () -> R) {
    glPushAttrib(GL_ALL_ATTRIB_BITS)
    try {
        function()
    } finally {
        glPopAttrib()
    }
}

inline fun <R> glStateScope(function: () -> R) {
    glPushAttrib(GL_ALL_ATTRIB_BITS)
    glPushMatrix()
    try {
        function()
    } finally {
        glPopMatrix()
        glPopAttrib()
    }
}

fun Vec3D.glTranslate() {
    glTranslated(x, y, z)
}

fun Color.glColor() {
    glColor4d(r, g, b, a)
}
