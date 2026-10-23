package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.KanisaniRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen(val title: String) {
    HOME("Kanisani"),
    GIVING("Giving & M-Pesa"),
    WORSHIP("Worship & Hymns"),
    ATTENDANCE("Check-In"),
    GROUPS("Ministries"),
    MARKETPLACE("Marketplace"),
    ADMIN("Admin & CMS"),
    PROFILE("My Member ID")
}

class KanisaniViewModel(
    private val repository: KanisaniRepository = KanisaniRepository.instance
) : ViewModel() {

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _screenBackStack = MutableStateFlow(listOf(AppScreen.HOME))
    val screenBackStack: StateFlow<List<AppScreen>> = _screenBackStack.asStateFlow()

    val currentUser = repository.currentUser
    val isOfflineMode = repository.isOfflineMode
    val denominations = repository.denominations
    val churches = repository.churches
    val members = repository.members
    val hymns = repository.hymns
    val prayers = repository.prayers
    val sermons = repository.sermons
    val transactions = repository.transactions
    val attendance = repository.attendance
    val ministryGroups = repository.ministryGroups
    val marketplaceItems = repository.marketplaceItems
    val announcements = repository.announcements

    // Selected items for detail modals or sheets
    private val _selectedHymn = MutableStateFlow<Hymn?>(null)
    val selectedHymn: StateFlow<Hymn?> = _selectedHymn.asStateFlow()

    private val _selectedSermon = MutableStateFlow<Sermon?>(null)
    val selectedSermon: StateFlow<Sermon?> = _selectedSermon.asStateFlow()

    private val _activeGivingChurch = MutableStateFlow<Church?>(null)
    val activeGivingChurch: StateFlow<Church?> = _activeGivingChurch.asStateFlow()

    private val _showRoleSwitcher = MutableStateFlow(false)
    val showRoleSwitcher: StateFlow<Boolean> = _showRoleSwitcher.asStateFlow()

    private val _showMpesaStkModal = MutableStateFlow(false)
    val showMpesaStkModal: StateFlow<Boolean> = _showMpesaStkModal.asStateFlow()

    private val _lastCompletedTx = MutableStateFlow<GivingTransaction?>(null)
    val lastCompletedTx: StateFlow<GivingTransaction?> = _lastCompletedTx.asStateFlow()

    // Generational Readability: 1.0f = Standard, 1.25f = Senior Comfort, 1.45f = Large Print
    private val _fontSizeScale = MutableStateFlow(1.0f)
    val fontSizeScale: StateFlow<Float> = _fontSizeScale.asStateFlow()

    fun cycleFontSize() {
        _fontSizeScale.value = when (_fontSizeScale.value) {
            1.0f -> 1.25f
            1.25f -> 1.45f
            else -> 1.0f
        }
    }

    init {
        // Default giving church to current member's church
        viewModelScope.launch {
            repository.churches.collect { list ->
                if (_activeGivingChurch.value == null && list.isNotEmpty()) {
                    _activeGivingChurch.value = list.first()
                }
            }
        }
    }

    fun navigateTo(screen: AppScreen) {
        if (_currentScreen.value != screen) {
            _screenBackStack.value = _screenBackStack.value + screen
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        val stack = _screenBackStack.value
        if (stack.size > 1) {
            val newStack = stack.dropLast(1)
            _screenBackStack.value = newStack
            _currentScreen.value = newStack.last()
            return true
        }
        return false
    }

    fun toggleOfflineMode() {
        repository.setOfflineMode(!repository.isOfflineMode.value)
    }

    fun toggleRoleSwitcher(show: Boolean) {
        _showRoleSwitcher.value = show
    }

    fun selectRole(role: UserRole) {
        repository.switchRole(role)
        _showRoleSwitcher.value = false
    }

    fun switchUser(member: Member) {
        repository.switchUser(member)
        _showRoleSwitcher.value = false
    }

    fun selectHymn(hymn: Hymn?) {
        _selectedHymn.value = hymn
    }

    fun selectSermon(sermon: Sermon?) {
        _selectedSermon.value = sermon
    }

    fun toggleHymnFavorite(hymnId: String) {
        repository.toggleHymnFavorite(hymnId)
        _selectedHymn.value?.let { current ->
            if (current.id == hymnId) {
                _selectedHymn.value = current.copy(isFavorite = !current.isFavorite)
            }
        }
    }

    fun toggleSermonDownload(sermonId: String) {
        repository.toggleSermonDownload(sermonId)
        _selectedSermon.value?.let { current ->
            if (current.id == sermonId) {
                _selectedSermon.value = current.copy(isDownloaded = !current.isDownloaded)
            }
        }
    }

    fun toggleGroupJoin(groupId: String) {
        repository.toggleGroupJoin(groupId)
    }

    fun recordAttendance(
        serviceId: String,
        serviceName: String,
        memberName: String,
        phone: String,
        method: CheckInMethod
    ): AttendanceRecord {
        return repository.recordAttendance(serviceId, serviceName, memberName, phone, method)
    }

    fun syncOfflineData() {
        repository.syncQueuedGiving()
        repository.syncOfflineAttendance()
    }

    fun openGivingModal(church: Church? = null) {
        if (church != null) {
            _activeGivingChurch.value = church
        }
        _showMpesaStkModal.value = true
    }

    fun closeGivingModal() {
        _showMpesaStkModal.value = false
    }

    fun processMpesaGiving(
        amountKes: Double,
        givingType: GivingType,
        phone: String,
        church: Church
    ) {
        val tx = repository.initiateGiving(amountKes, givingType, phone, church)
        _lastCompletedTx.value = tx
        _showMpesaStkModal.value = false
    }

    fun clearReceipt() {
        _lastCompletedTx.value = null
    }

    fun addMarketplaceItem(item: MarketplaceItem) {
        repository.addMarketplaceItem(item)
    }

    fun registerMember(
        name: String,
        phone: String,
        email: String,
        gender: String,
        churchId: String,
        baptismStatus: String,
        role: UserRole
    ) {
        repository.registerMember(name, phone, email, gender, churchId, baptismStatus, role)
    }

    fun addChurch(
        denominationId: String,
        name: String,
        branch: String,
        city: String,
        address: String,
        phone: String,
        paybill: String
    ) {
        repository.addChurch(denominationId, name, branch, city, address, phone, paybill)
    }

    fun addDenomination(name: String, code: String, doctrine: String) {
        repository.addDenomination(name, code, doctrine)
    }
}
