package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.GivingType
import com.example.data.model.PaymentStatus
import com.example.data.repository.KanisaniRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read app name from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Kanisani", appName)
    }

    @Test
    fun `test offline giving queue and sync`() {
        val repository = KanisaniRepository()
        val church = repository.churches.value.first()

        // Enable offline mode
        repository.setOfflineMode(true)
        val offlineTx = repository.initiateGiving(
            amountKes = 2500.0,
            givingType = GivingType.OFFERING,
            phone = "+254 712 345 678",
            church = church
        )

        assertEquals(PaymentStatus.QUEUED_OFFLINE, offlineTx.status)
        assertEquals(true, offlineTx.isOfflineQueued)

        // Sync when online returns
        repository.syncQueuedGiving()
        val updatedTx = repository.transactions.value.find { it.id == offlineTx.id }
        assertNotNull(updatedTx)
        assertEquals(PaymentStatus.COMPLETED, updatedTx?.status)
    }
}
