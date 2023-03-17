package com.warranty.warrantyproject.presenters

import com.warranty.warrantyproject.infrastructures.db.WarrantyEntity
import com.warranty.warrantyproject.presenters.viewmodel.WarrantyViewModel
import com.warranty.warrantyproject.presenters.views.CanCreateAddView
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*

class AddPresenterTest {

    @Mock
    private lateinit var mockView: CanCreateAddView

    @Mock
    private lateinit var mockViewModel: WarrantyViewModel

    private lateinit var presenter: AddPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = AddPresenter(mockView, mockViewModel)
    }

    @Test
    fun testSaveWarrantyWithNotification() {
        val title = "Test Warranty"
        val summary = "Test Summary"
        val shopName = "Test Shop"
        val price = 100.0
        val dateOfPurchase = Date()
        val dateOfExpiry = Date()
        val imageProofLink = "Test Image Proof Link"
        val imageCoverLink = "Test Image Cover Link"
        val requireNotification = true
        val notificationChoice = "1 week"
        val generatedId = 123L

        val warrantyEntity = WarrantyEntity(
            0,
            title,
            summary,
            shopName,
            price,
            dateOfPurchase,
            dateOfExpiry,
            imageProofLink,
            imageCoverLink
        )
        val deferred = CompletableDeferred<Long>()
        deferred.complete(generatedId)

        runBlocking {
            Mockito.`when`(mockViewModel.insertWarranty(warrantyEntity)).thenReturn(deferred)
            presenter.saveWarranty(
                title,
                summary,
                shopName,
                price,
                dateOfPurchase,
                dateOfExpiry,
                imageProofLink,
                imageCoverLink,
                requireNotification,
                notificationChoice
            )
            delay(2000)
            Mockito.verify(mockView).getCurrentContext()
            Mockito.verify(mockView).setPeriods(listOf("1 week", "2 weeks", "3 weeks", "1 month"))
            Mockito.verify(mockView, Mockito.times(1))
        }
    }

    @Test
    fun testSaveWarrantyWithoutNotification() {
        val title = "Test Warranty"
        val summary = "Test Summary"
        val shopName = "Test Shop"
        val price = 100.0
        val dateOfPurchase = Date()
        val dateOfExpiry = Date()
        val imageProofLink = "Test Image Proof Link"
        val imageCoverLink = "Test Image Cover Link"
        val requireNotification = false
        val notificationChoice = "1 week"
        val generatedId = 123L

        val warrantyEntity = WarrantyEntity(
            0,
            title,
            summary,
            shopName,
            price,
            dateOfPurchase,
            dateOfExpiry,
            imageProofLink,
            imageCoverLink
        )
        val deferred = CompletableDeferred<Long>()
        deferred.complete(generatedId)

        runBlocking {
            Mockito.`when`(mockViewModel.insertWarranty(warrantyEntity)).thenReturn(deferred)
            presenter.saveWarranty(
                title,
                summary,
                shopName,
                price,
                dateOfPurchase,
                dateOfExpiry,
                imageProofLink,
                imageCoverLink,
                requireNotification,
                notificationChoice
            )
            delay(2000)
            Mockito.verify(mockView).getCurrentContext()
            Mockito.verify(mockView).setPeriods(listOf("1 week", "2 weeks", "3 weeks", "1 month"))
        }
    }

    @Test
    fun testGetNotificationPeriodsWithValidDate() {
        val endOfWarrantyDate = "20/03/2023"
        presenter.getNotificationPeriods(endOfWarrantyDate)
        Mockito.verify(mockView).setPeriods(listOf("1 week", "2 weeks", "3 weeks", "1 month"))
    }
}
