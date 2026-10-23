package com.example.data.model

enum class UserRole(val label: String, val badgeColorHex: Long) {
    SUPER_ADMIN("Super Super Admin", 0xFF7C3AED),
    DENOMINATION_ADMIN("Denomination Admin", 0xFF2563EB),
    CHURCH_ADMIN("Church Admin", 0xFF0D9488),
    LEADER("Ministry Leader", 0xFFD97706),
    MEMBER("Church Member", 0xFF475569)
}

enum class GivingType(val label: String, val description: String) {
    TITHE("Tithe (Fungu la Kumi)", "10% of harvest/income for God's storehouse"),
    OFFERING("General Offering (Sadaka)", "Sunday regular worship offering"),
    SEED("Seed Offering (Sadaka ya Mbegu)", "Specific prayer point or covenant seed"),
    BUILDING_FUND("Sanctuary Project (Ujenzi)", "Church construction & expansion fund"),
    SPECIAL_THANKSGIVING("Thanksgiving (Shukrani)", "Celebrating breakthroughs & blessings"),
    BENEVOLENCE("Benevolence (Wajane na Yatima)", "Support for widows, orphans & needy")
}

enum class PaymentStatus {
    COMPLETED,
    QUEUED_OFFLINE,
    PENDING_STK,
    FAILED
}

enum class CheckInMethod {
    QR_SCAN,
    PHONE_NUMBER,
    MANUAL
}

data class Denomination(
    val id: String,
    val name: String,
    val code: String,
    val doctrineSummary: String,
    val totalChurches: Int
)

data class ChurchService(
    val id: String,
    val name: String,
    val dayOfWeek: String,
    val time: String,
    val venue: String
)

data class Church(
    val id: String,
    val denominationId: String,
    val denominationName: String,
    val name: String,
    val branch: String,
    val city: String,
    val address: String,
    val phone: String,
    val paybillNumber: String,
    val accountName: String,
    val memberCount: Int,
    val services: List<ChurchService>
)

data class Member(
    val id: String,
    val name: String,
    val phone: String,
    val email: String,
    val gender: String,
    val churchId: String,
    val churchName: String,
    val branch: String,
    val denominationId: String,
    val baptismStatus: String, // "Baptized by Immersion", "Baptized / Confirmed", "Preparing"
    val membershipNumber: String,
    val role: UserRole,
    val groupsJoined: List<String>,
    val joinYear: String
)

data class GivingTransaction(
    val id: String,
    val memberId: String,
    val memberName: String,
    val phone: String,
    val amountKes: Double,
    val givingType: GivingType,
    val churchId: String,
    val churchName: String,
    val mpesaReceipt: String,
    val status: PaymentStatus,
    val timestamp: String,
    val isOfflineQueued: Boolean = false
)

data class Hymn(
    val id: String,
    val number: Int,
    val titleEn: String,
    val titleSw: String,
    val category: String, // Praise, Worship, Holy Communion, Morning Prayer, Evening
    val denomination: String,
    val keySignature: String,
    val versesEn: List<String>,
    val chorusEn: String? = null,
    val versesSw: List<String>,
    val chorusSw: String? = null,
    val isFavorite: Boolean = false
)

data class Prayer(
    val id: String,
    val title: String,
    val category: String, // The Lord's Prayer, Morning Collect, Rosary / Angelus, Pastoral Benediction, Healing
    val denomination: String,
    val scriptureRef: String,
    val text: String
)

data class Sermon(
    val id: String,
    val title: String,
    val preacher: String,
    val date: String,
    val scripture: String,
    val series: String,
    val durationMinutes: Int,
    val notesSummary: String,
    val isDownloaded: Boolean = false
)

data class AttendanceRecord(
    val id: String,
    val serviceId: String,
    val serviceName: String,
    val memberId: String,
    val memberName: String,
    val memberPhone: String,
    val timestamp: String,
    val checkInMethod: CheckInMethod,
    val isSyncedOffline: Boolean = true
)

data class MinistryGroup(
    val id: String,
    val name: String,
    val category: String,
    val leaderName: String,
    val meetingSchedule: String,
    val memberCount: Int,
    val description: String,
    val isJoined: Boolean = false
)

data class MarketplaceItem(
    val id: String,
    val title: String,
    val category: String,
    val sellerName: String,
    val sellerPhone: String,
    val churchBranch: String,
    val priceKes: Double,
    val description: String
)

data class Announcement(
    val id: String,
    val title: String,
    val message: String,
    val date: String,
    val isUrgent: Boolean,
    val targetGroup: String
)
