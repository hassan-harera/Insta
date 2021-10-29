package com.harera.base.utils.time

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*


object InstaDateSerializer : KSerializer<Date> {

    override fun deserialize(decoder: Decoder): Date =
        Date(decoder.decodeLong())

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeLong(value.time)
    }

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Date", PrimitiveKind.LONG)
}