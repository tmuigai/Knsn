package com.example.data.repository

import com.example.data.local.SampleData
import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class KanisaniRepository {

    private val _currentUser = MutableStateFlow(SampleData.currentMember)
    val currentUser: StateFlow<Member> = _currentUser.asStateFlow()

    private val _isOfflineMode = MutableStateFlow(false)
    val isOfflineMode: StateFlow<Boolean> = _isOfflineMode.asStateFlow()

    private val _denominations = MutableStateFlow(SampleData.denominations)
    val denominations: StateFlow<List<Denomination>> = _denominations.asStateFlow()

    private val _churches = MutableStateFlow(SampleData.churches)
    val churches: StateFlow<List<Church>> = _churches.asStateFlow()

    private val _members = MutableStateFlow(SampleData.allMembers)
    val members: StateFlow<List<Member>> = _members.asStateFlow()

    private val _hymns = MutableStateFlow(SampleData.hymns)
    val hymns: StateFlow<List<Hymn>> = _hymns.asStateFlow()

    private val _prayers = MutableStateFlow(SampleData.prayers)
    val prayers: StateFlow<List<Prayer>> = _prayers.asStateFlow()

    private val _sermons = MutableStateFlow(SampleData.sermons)
    val sermons: StateFlow<List<Sermon>> = _sermons.asStateFlow()

    private val _transactions = MutableStateFlow(SampleData.initialGivingTransactions)
    val transactions: StateFlow<List<GivingTransaction>> = _transactions.asStateFlow()

    private val _attendance = MutableStateFlow(SampleData.initialAttendanceRecords)
    val attendance: StateFlow<List<AttendanceRecord>> = _attendance.asStateFlow()

    private val _ministryGroups = MutableStateFlow(SampleData.ministryGroups)
    val ministryGroups: StateFlow<List<MinistryGroup>> = _ministryGroups.asStateFlow()

    private val _marketplaceItems = MutableStateFlow(SampleData.marketplaceItems)
    val marketplaceItems: StateFlow<List<MarketplaceItem>> = _marketplaceItems.asStateFlow()

    private val _announcements = MutableStateFlow(SampleData.announcements)
    val announcements: StateFlow<List<Announcement>> = _announcements.asStateFlow()

    fun setOfflineMode(offline: Boolean) {
        _isOfflineMode.value = offline
    }

    fun switchRole(role: UserRole) {
        val current = _currentUser.value
        _currentUser.value = current.copy(role = role)
    }

    fun switchUser(member: Member) {
        _currentUser.value = member
    }

    fun toggleHymnFavorite(hymnId: String) {
        _hymns.value = _hymns.value.map {
            if (it.id == hymnId) it.copy(isFavorite = !it.isFavorite) else it
        }
    }

    fun toggleSermonDownload(sermonId: String) {
        _sermons.value = _sermons.value.map {
            if (it.id == sermonId) it.copy(isDownloaded = !it.isDownloaded) else it
        }
    }

    fun toggleGroupJoin(groupId: String) {
        _ministryGroups.value = _ministryGroups.value.map {
            if (it.id == groupId) {
                val newStatus = !it.isJoined
                it.copy(
                    isJoined = newStatus,
                    memberCount = if (newStatus) it.memberCount + 1 else it.memberCount - 1
                )
            } else it
        }
    }

    fun recordAttendance(
        serviceId: String,
        serviceName: String,
        memberName: String,
        phone: String,
        method: CheckInMethod
    ): AttendanceRecord {
        val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
        val isOffline = _isOfflineMode.value
        val record = AttendanceRecord(
            id = "att_" + UUID.randomUUID().toString().take(6),
            serviceId = serviceId,
            serviceName = serviceName,
            memberId = _currentUser.value.id,
            memberName = memberName.ifBlank { _currentUser.value.name },
            memberPhone = phone.ifBlank { _currentUser.value.phone },
            timestamp = dateStr,
            checkInMethod = method,
            isSyncedOffline = !isOffline
        )
        _attendance.value = listOf(record) + _attendance.value
        return record
    }

    fun syncOfflineAttendance() {
        _attendance.value = _attendance.value.map { it.copy(isSyncedOffline = true) }
    }

    fun initiateGiving(
        amountKes: Double,
        givingType: GivingType,
        phone: String,
        church: Church
    ): GivingTransaction {
        val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
        val isOffline = _isOfflineMode.value

        val txId = "tx_" + UUID.randomUUID().toString().take(6)
        val mpesaCode = generateMpesaReceipt()

        val tx = GivingTransaction(
            id = txId,
            memberId = _currentUser.value.id,
            memberName = _currentUser.value.name,
            phone = phone,
            amountKes = amountKes,
            givingType = givingType,
            churchId = church.id,
            churchName = church.name,
            mpesaReceipt = if (isOffline) "QUEUED_OFFLINE" else mpesaCode,
            status = if (isOffline) PaymentStatus.QUEUED_OFFLINE else PaymentStatus.COMPLETED,
            timestamp = dateStr,
            isOfflineQueued = isOffline
        )

        _transactions.value = listOf(tx) + _transactions.value
        return tx
    }

    fun syncQueuedGiving() {
        _transactions.value = _transactions.value.map { tx ->
            if (tx.isOfflineQueued && tx.status == PaymentStatus.QUEUED_OFFLINE) {
                tx.copy(
                    mpesaReceipt = generateMpesaReceipt(),
                    status = PaymentStatus.COMPLETED,
                    isOfflineQueued = false
                )
            } else tx
        }
    }

    fun addMarketplaceItem(item: MarketplaceItem) {
        _marketplaceItems.value = listOf(item) + _marketplaceItems.value
    }

    fun registerMember(
        name: String,
        phone: String,
        email: String,
        gender: String,
        churchId: String,
        baptismStatus: String,
        role: UserRole
    ): Member {
        val church = _churches.value.find { it.id == churchId } ?: _churches.value.first()
        val num = "KAN-${church.denominationName}-${Random.nextInt(100, 999)}"
        val newMem = Member(
            id = "mem_" + UUID.randomUUID().toString().take(6),
            name = name,
            phone = phone,
            email = email,
            gender = gender,
            churchId = church.id,
            churchName = church.name,
            branch = church.branch,
            denominationId = church.denominationId,
            baptismStatus = baptismStatus,
            membershipNumber = num,
            role = role,
            groupsJoined = emptyList(),
            joinYear = "2026"
        )
        _members.value = listOf(newMem) + _members.value
        return newMem
    }

    fun addChurch(
        denominationId: String,
        name: String,
        branch: String,
        city: String,
        address: String,
        phone: String,
        paybill: String
    ): Church {
        val denom = _denominations.value.find { it.id == denominationId } ?: _denominations.value.first()
        val newCh = Church(
            id = "ch_" + UUID.randomUUID().toString().take(6),
            denominationId = denom.id,
            denominationName = denom.code,
            name = name,
            branch = branch,
            city = city,
            address = address,
            phone = phone,
            paybillNumber = paybill,
            accountName = name.replace(" ", "").uppercase().take(8),
            memberCount = 1,
            services = listOf(
                ChurchService("srv_" + UUID.randomUUID().toString().take(4), "Main Worship Service", "Sunday", "10:00 AM", "Main Hall")
            )
        )
        _churches.value = listOf(newCh) + _churches.value
        return newCh
    }

    fun addDenomination(name: String, code: String, doctrine: String): Denomination {
        val newDen = Denomination(
            id = "den_" + UUID.randomUUID().toString().take(6),
            name = name,
            code = code,
            doctrineSummary = doctrine,
            totalChurches = 0
        )
        _denominations.value = listOf(newDen) + _denominations.value
        return newDen
    }

    private fun generateMpesaReceipt(): String {
        val letters = ('A'..'Z').toList()
        val digits = ('0'..'9').toList()
        val prefix = "SK" + (1..2).map { letters.random() }.joinToString("")
        val mid = (1..4).map { digits.random() }.joinToString("")
        val suffix = (1..2).map { letters.random() }.joinToString("")
        return "$prefix$mid$suffix"
    }

    companion object {
        val instance = KanisaniRepository()
    }
}
