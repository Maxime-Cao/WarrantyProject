package com.warranty.warrantyproject.presenters

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.warranty.warrantyproject.WarrantyAdapter
import com.warranty.warrantyproject.infrastructures.db.WarrantyEntity
import com.warranty.warrantyproject.presenters.viewmodel.WarrantyViewModel
import com.warranty.warrantyproject.presenters.views.CanCreateHomeView
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.util.*


class HomePresenterTest {

    // Dépendances nécessaires pour initialiser le HomePresenter
    private lateinit var mockView: CanCreateHomeView
    private lateinit var mockViewModel: WarrantyViewModel

    // HomePresenter à tester
    private lateinit var presenter: HomePresenter

    @Before
    fun setUp() {
        mockView = mock(CanCreateHomeView::class.java)
        mockViewModel = mock(WarrantyViewModel::class.java)

        presenter = HomePresenter(mockView, mockViewModel)
    }

    @Test
    fun `getWarranties returns the expected list`() {
        // given
        val expectedList = listOf(
            WarrantyEntity(1, "Warranty 1", "Summary", "Shop", 19.9, Date(), Date(), "im","im"),
            WarrantyEntity(2, "Warranty 2", "Summary", "Shop", 19.9, Date(), Date(), "im","im"),
            WarrantyEntity(3, "Warranty 3", "Summary", "Shop", 19.9, Date(), Date(), "im","im")
        )
        `when`(mockViewModel.warranties).thenReturn(MutableLiveData(expectedList))

        // when
        val actualList = presenter.getWarranties()

        // then
        assertEquals(expectedList, actualList)
    }

    @Test
    fun `setCurrentWarranty calls setCurrentWarranty on ViewModel with expected value`() {
        // given
        val warranty = 1

        // when
        presenter.setCurrentWarranty(warranty)

        // then
        verify(mockViewModel).setCurrentWarranty(warranty)
    }

    @Test
    fun `HomePresenter initializes properties view and viewModel correctly`() {
        // given
        val view = mock(CanCreateHomeView::class.java)
        val viewModel = mock(WarrantyViewModel::class.java)

        // when
        val presenter = HomePresenter(view, viewModel)

        // then
        assertEquals(view, presenter.getView())
        assertEquals(viewModel, presenter.getViewModel())
    }
}