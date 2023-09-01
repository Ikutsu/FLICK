package com.ikuzMirel.flick.data.serializer

import com.ikuzMirel.flick.data.remote.websocket.LastReadMessageSet
import com.ikuzMirel.flick.data.remote.websocket.WebSocketMessage
import com.ikuzMirel.flick.data.requests.SendMessageRequest
import com.ikuzMirel.flick.data.response.MessageResponse
import com.ikuzMirel.flick.domain.entities.FriendRequestEntity
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.serializer

object WebSocketMsgSerializer : KSerializer<WebSocketMessage> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("WebSocketMessage") {
        element("type", serialDescriptor<String>())
        element("data", buildClassSerialDescriptor("Any"))
    }

    @Suppress("UNCHECKED_CAST")
    private val typeSerializer: Map<String, KSerializer<Any>> = mapOf(
        "chatMessage" to serializer<MessageResponse>(),
        "chatMessageRequest" to serializer<SendMessageRequest>(),
        "friendRequest" to serializer<FriendRequestEntity>(),
        "lastReadMessage" to serializer<LastReadMessageSet>()
    ).mapValues { (_, v) -> v as KSerializer<Any> }

    private fun getDataSerializer(type: String): KSerializer<Any> {
        return typeSerializer[type] ?: throw SerializationException("Unknown type $type")
    }

    override fun serialize(encoder: Encoder, value: WebSocketMessage) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.type)
            encodeSerializableElement(descriptor, 1, getDataSerializer(value.type), value.data)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): WebSocketMessage = decoder.decodeStructure(descriptor) {
        if (decodeSequentially()) {
            val type = decodeStringElement(descriptor, 0)
            val data = decodeSerializableElement(descriptor, 1, getDataSerializer(type))
            WebSocketMessage(type, data)
        } else {
            require(decodeElementIndex(descriptor) == 0) { "Type field should be precede data field" }
            val type = decodeStringElement(descriptor, 0)
            val data = when (val index = decodeElementIndex(descriptor)) {
                1 -> decodeSerializableElement(descriptor, 1, getDataSerializer(type))
                CompositeDecoder.DECODE_DONE -> throw SerializationException("Data is missing")
                else -> error("Unexpected index $index")
            }
            WebSocketMessage(type, data)
        }
    }
}