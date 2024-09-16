package org.example.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/**
 * Serializer for [LocalDate] to be used with Kotlin serialization.
 */
@Serializer(forClass = LocalDate::class)
object LocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: LocalDate) {
        val epochSecond = value.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()
        encoder.encodeLong(epochSecond)
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        val epochSecond = decoder.decodeLong()
        return Instant.ofEpochSecond(epochSecond).atZone(ZoneId.systemDefault()).toLocalDate()
    }
}
