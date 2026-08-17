package com.example.gestionturnosapp.data.remote

import com.example.gestionturnosapp.data.model.*
import com.example.gestionturnosapp.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/auth/fcm-token")
    suspend fun updateFcmToken(@Body request: Map<String, String>): Response<Unit>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("api/auth/verify")
    suspend fun verify(@Body request: VerifyRequest): Response<Unit>

    @POST("api/auth/resend-code")
    suspend fun resendCode(@Body request: Map<String, String>): Response<Unit>

    @GET("api/turnos")
    suspend fun getTurnos(): Response<List<Turno>>

    @GET("api/turnos/check-availability")
    suspend fun checkAvailability(
        @Query("fecha") fecha: String,
        @Query("hora") hora: String
    ): Response<Map<String, Boolean>>

    @POST("api/turnos")
    suspend fun crearTurno(@Body request: NuevoTurnoRequest): Response<Turno>

    @DELETE("api/turnos/{id}")
    suspend fun eliminarTurno(@Path("id") id: String): Response<Unit>

    // Perfil de Usuario
    @PUT("api/auth/profile")
    suspend fun updateProfile(@Body usuario: Usuario): Response<Usuario>

    // NUEVO: Servicios Profesionales de Medicación
    @GET("api/medicamentos")
    suspend fun getMedicamentos(): Response<List<Medicamento>>

    @POST("api/medicamentos")
    suspend fun agregarMedicamento(@Body request: NuevoMedicamentoRequest): Response<Medicamento>

    @PUT("api/medicamentos/{id}")
    suspend fun updateMedicamento(@Path("id") id: String, @Body med: Medicamento): Response<Medicamento>

    @DELETE("api/medicamentos/{id}")
    suspend fun eliminarMedicamento(@Path("id") id: String): Response<Unit>

    // NUEVO: Estudios Médicos
    @GET("api/estudios")
    suspend fun getEstudios(): Response<List<EstudioMedico>>

    @POST("api/estudios")
    suspend fun agregarEstudio(@Body estudio: EstudioMedico): Response<EstudioMedico>

    @DELETE("api/estudios/{id}")
    suspend fun eliminarEstudio(@Path("id") id: String): Response<Unit>

    // Chat Médico
    @GET("api/chat")
    suspend fun getMensajes(): Response<List<Mensaje>>

    @POST("api/chat")
    suspend fun enviarMensaje(@Body request: Map<String, String>): Response<Mensaje>
}
