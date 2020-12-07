package ux

import libgtk.GDK_PIXBUF_ERROR
import libgtk.GError
import libgtk.GdkPixbuf
import libgtk.gdk_pixbuf_new_from_file
import kotlinx.cinterop.*

public class Pixbuf(internal val cpointer: CPointer<GdkPixbuf>) {
    public companion object {
        @Throws(PixbufException::class)
        public fun from(file: String): Pixbuf = memScoped {
            val err = allocPointerTo<GError>().ptr
            val pix = gdk_pixbuf_new_from_file(file, err)
            err.unwrap()

            return@memScoped Pixbuf(pix!!.reinterpret())
        }
    }
}

public class PixbufException(
    cause: String,
    message: String? = null
) : Exception("($cause) ${message ?: "no error message"}") {

    public companion object {
        public fun fromCode(code: Int, message: String? = null): PixbufException {
            val cause = when (code) {
                0 -> "Corrupt image"
                1 -> "Insufficient memory"
                2 -> "Bad option"
                3 -> "Unknown image type"
                4 -> "Unsupported operation"
                5 -> "Failed"
                6 -> "Incomplete animation"
                else -> "Unknown"
            }

            return PixbufException(cause, message)
        }
    }
}

internal fun CPointer<CPointerVar<GError>>.unwrap(throwException: Boolean = true): Exception? {
    val err = pointed.pointed ?: return null
    val exception = when (err.domain) {
        GDK_PIXBUF_ERROR -> {
            PixbufException.fromCode(err.code, err.message?.toKString())
        }
        else -> Exception("GError code ${err.code} (unknown domain id ${err.domain}) ${err.message?.toKString()}")
    }

    if (throwException)
        throw exception

    return exception
}
