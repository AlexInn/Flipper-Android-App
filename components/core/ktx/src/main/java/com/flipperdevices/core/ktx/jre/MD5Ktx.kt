package com.flipperdevices.core.ktx.jre

import java.io.InputStream
import java.math.BigInteger
import java.security.MessageDigest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val MD5_NAME = "MD5"
private const val MD5_LENGTH = 32
private const val MD5_RADIX = 16
private const val BIG_INTEGER_POSITIVE_NUMBER = 1

/**
 * Calculate md5 and _close_ stream
 */
@Suppress("BlockingMethodInNonBlockingContext")
suspend fun InputStream.md5(): String = withContext(Dispatchers.IO) {
    use { stream ->
        val encoder = MessageDigest.getInstance(MD5_NAME)
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var bytes = stream.read(buffer)
        while (bytes >= 0) {
            encoder.update(buffer, 0, bytes)
            bytes = stream.read(buffer)
        }
        return@use BigInteger(BIG_INTEGER_POSITIVE_NUMBER, encoder.digest())
            .toString(MD5_RADIX)
            .padStart(MD5_LENGTH, '0')
    }
}
