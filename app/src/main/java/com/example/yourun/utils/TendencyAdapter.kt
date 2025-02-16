package com.example.yourun.utils

import com.example.yourun.model.data.Tendency
import com.google.gson.*
import java.lang.reflect.Type

class TendencyAdapter : JsonDeserializer<Tendency>, JsonSerializer<Tendency> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Tendency {
        return json?.asString?.let { Tendency.fromValue(it) } ?: Tendency.PACEMAKER
    }

    override fun serialize(src: Tendency?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.value)
    }
}