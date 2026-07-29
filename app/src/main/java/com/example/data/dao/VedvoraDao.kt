package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.ActivityLogEntity
import com.example.data.entity.BookingEntity
import com.example.data.entity.InvoiceEntity
import com.example.data.entity.NoticeEntity
import com.example.data.entity.VisitorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VedvoraDao {
    @Query("SELECT * FROM visitors WHERE isHistory = 0 ORDER BY id DESC")
    fun getScheduledVisitors(): Flow<List<VisitorEntity>>

    @Query("SELECT * FROM visitors WHERE isHistory = 1 ORDER BY id DESC")
    fun getRecentEncounters(): Flow<List<VisitorEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisitor(visitor: VisitorEntity): Long

    @Update
    suspend fun updateVisitor(visitor: VisitorEntity)

    @Query("SELECT * FROM invoices ORDER BY id ASC")
    fun getInvoices(): Flow<List<InvoiceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvoice(invoice: InvoiceEntity): Long

    @Query("UPDATE invoices SET status = 'Paid' WHERE id = :id")
    suspend fun markInvoicePaid(id: Long)

    @Query("UPDATE invoices SET status = 'Paid' WHERE status = 'Pending'")
    suspend fun payAllPendingInvoices()

    @Query("SELECT * FROM bookings ORDER BY id DESC")
    fun getBookings(): Flow<List<BookingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity): Long

    @Query("DELETE FROM bookings WHERE id = :id")
    suspend fun deleteBooking(id: Long)

    @Query("UPDATE bookings SET startTimeStr = :newTime, subtitle = :newSubtitle WHERE id = :id")
    suspend fun rescheduleBooking(id: Long, newTime: String, newSubtitle: String)

    @Query("UPDATE bookings SET status = :status WHERE id = :id")
    suspend fun updateBookingStatus(id: Long, status: String)

    @Query("UPDATE bookings SET rating = :rating, feedbackText = :feedbackText, feedbackTags = :feedbackTags WHERE id = :id")
    suspend fun updateBookingRating(id: Long, rating: Int, feedbackText: String, feedbackTags: String)

    @Query("SELECT * FROM notices ORDER BY id ASC")
    fun getNotices(): Flow<List<NoticeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotice(notice: NoticeEntity): Long

    @Query("SELECT * FROM activity_logs ORDER BY id DESC")
    fun getActivityLogs(): Flow<List<ActivityLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityLog(log: ActivityLogEntity): Long
}
