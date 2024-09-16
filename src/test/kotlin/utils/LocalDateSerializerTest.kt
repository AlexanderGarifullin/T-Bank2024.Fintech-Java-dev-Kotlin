package utils

import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.modules.SerializersModule
import org.assertj.core.api.Assertions.assertThat
import org.example.utils.LocalDateSerializer
import org.junit.jupiter.api.Test
import java.time.LocalDate

class LocalDateSerializerTest {
    private val json = Json { serializersModule = SerializersModule { contextual(LocalDate::class, LocalDateSerializer) } }

    @Test
    fun `should serialize LocalDate to epoch seconds`() {
        val date = LocalDate.of(2024, 9, 15)
        val serialized = json.encodeToString(date)

        // Convert the expected epoch seconds to a JSON string for comparison
        val expectedSerialized = "1726340400" // epoch seconds for 2024-09-15

        assertThat(serialized).isEqualTo(expectedSerialized)
    }

    @Test
    fun `should deserialize epoch seconds to LocalDate`() {
        val serialized = "1726340400" // epoch seconds for 2024-09-15
        val deserialized = json.decodeFromString<LocalDate>(serialized)

        val expectedDate = LocalDate.of(2024, 9, 15)

        assertThat(deserialized).isEqualTo(expectedDate)
    }
}