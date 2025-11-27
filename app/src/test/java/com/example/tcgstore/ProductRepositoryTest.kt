package com.example.tcgstore

import android.content.Context
import com.example.tcgstore.data.network.ApiService
import com.example.tcgstore.data.network.models.ProductResponse
import com.example.tcgstore.data.repository.ApiResult
import com.example.tcgstore.data.repository.ProductRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class ProductRepositoryTest {


    private lateinit var mockContext: Context
    private lateinit var mockApiService: ApiService

    private lateinit var productRepository: ProductRepository

    @Before
    fun setup() {

        mockContext = mockk(relaxed = true)
        mockApiService = mockk()

        productRepository = ProductRepository(mockContext)

        val apiServiceField = ProductRepository::class.java.getDeclaredField("apiService")
        apiServiceField.isAccessible = true
        apiServiceField.set(productRepository, mockApiService)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `test obtener todos los productos retorna lista correctamente`() = runTest {
        val expectedProducts = listOf(
            ProductResponse(
                id = 1L,
                nombre = "Magic The Gathering: Murders at Karlov Manor",
                descripcion = "Bundle con cartas de Magic",
                precio = 78000,
                imagen = "/uploads/magic-bundle.jpg",
                hover = "/uploads/magic-bundle-hover.jpg",
                oferta = "NUEVO"
            ),
            ProductResponse(
                id = 2L,
                nombre = "One Piece Card Game Booster Box",
                descripcion = "Caja de sobres de One Piece",
                precio = 60000,
                imagen = "/uploads/onepiece-box.jpg",
                hover = null,
                oferta = null
            ),
            ProductResponse(
                id = 3L,
                nombre = "Pokémon Paradox Rift Bundle",
                descripcion = "Bundle de cartas Pokémon",
                precio = 40000,
                imagen = "/uploads/pokemon-bundle.jpg",
                hover = null,
                oferta = "OFERTA"
            )
        )

        coEvery {
            mockApiService.getAllProducts()
        } returns Response.success(expectedProducts)

        val result = productRepository.getAllProducts()

        assertTrue(result is ApiResult.Success, "El resultado debería ser Success")
        val successResult = result as ApiResult.Success

        assertEquals(3, successResult.data.size, "Debería retornar 3 productos")

        val primerProducto = successResult.data[0]
        assertEquals(1L, primerProducto.id)
        assertEquals("Magic The Gathering: Murders at Karlov Manor", primerProducto.nombre)
        assertEquals(78000, primerProducto.precio)
        assertEquals("NUEVO", primerProducto.oferta)

        val segundoProducto = successResult.data[1]
        assertEquals(2L, segundoProducto.id)
        assertEquals("One Piece Card Game Booster Box", segundoProducto.nombre)
        assertEquals(60000, segundoProducto.precio)
        assertEquals(null, segundoProducto.oferta)

        coVerify(exactly = 1) { mockApiService.getAllProducts() }
    }

    @Test
    fun `test obtener producto por ID retorna producto correcto`() = runTest {
        val productId = 1L
        val expectedProduct = ProductResponse(
            id = productId,
            nombre = "Yu-Gi-Oh! Duelist Pack",
            descripcion = "Pack de duelista de Yu-Gi-Oh!",
            precio = 25000,
            imagen = "/uploads/yugioh-pack.jpg",
            hover = "/uploads/yugioh-pack-hover.jpg",
            oferta = "NUEVO"
        )

        coEvery {
            mockApiService.getProductById(productId)
        } returns Response.success(expectedProduct)

        val result = productRepository.getProductById(productId)

        assertTrue(result is ApiResult.Success)
        val successResult = result as ApiResult.Success

        assertEquals(productId, successResult.data.id)
        assertEquals("Yu-Gi-Oh! Duelist Pack", successResult.data.nombre)
        assertEquals(25000, successResult.data.precio)
        assertTrue(successResult.data.imagen.isNotEmpty())

        coVerify(exactly = 1) { mockApiService.getProductById(productId) }
    }
}