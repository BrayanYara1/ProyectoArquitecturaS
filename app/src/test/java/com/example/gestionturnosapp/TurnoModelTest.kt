package com.example.gestionturnosapp

import com.example.gestionturnosapp.data.model.Turno
import org.junit.Assert.assertEquals
import org.junit.Test

class TurnoModelTest {
    @Test
    fun `test turno creation and property access`() {
        val turno = Turno(
            id = "1",
            pacienteNombre = "Juan Perez",
            fecha = "2024-05-20",
            hora = "10:00 AM",
            motivo = "Consulta general",
            estado = "Pendiente",
            especialidad = "Cardiología",
            doctor = "Dr. Sanchez"
        )

        assertEquals("1", turno.id)
        assertEquals("Juan Perez", turno.pacienteNombre)
        assertEquals("Pendiente", turno.estado)
        assertEquals("Cardiología", turno.especialidad)
    }

    @Test
    fun `test turno default values`() {
        val turno = Turno(
            id = "2",
            pacienteNombre = "Ana Maria",
            fecha = "2024-05-21",
            hora = "11:00 AM",
            motivo = "Control"
        )

        assertEquals("Pendiente", turno.estado)
        assertEquals("General", turno.especialidad)
        assertEquals("Dr. Asignado", turno.doctor)
    }
}
