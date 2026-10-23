package com.example.data.local

import com.example.data.model.*

object SampleData {

    val denominations = listOf(
        Denomination(
            id = "den_citam",
            name = "Christ Is The Answer Ministries (CITAM)",
            code = "CITAM",
            doctrineSummary = "Pentecostal evangelical fellowship focused on knowing God and making Him known.",
            totalChurches = 28
        ),
        Denomination(
            id = "den_ack",
            name = "Anglican Church of Kenya (ACK)",
            code = "ACK",
            doctrineSummary = "Historic Anglican communion, Book of Common Prayer liturgy, and evangelical witness.",
            totalChurches = 42
        ),
        Denomination(
            id = "den_catholic",
            name = "Catholic Church (Archdiocese)",
            code = "RC",
            doctrineSummary = "Universal Catholic apostolic faith, liturgical calendar, holy sacraments, and catechesis.",
            totalChurches = 65
        ),
        Denomination(
            id = "den_aic",
            name = "Africa Inland Church (AIC)",
            code = "AIC",
            doctrineSummary = "Evangelical bible-believing mission-founded church emphasizing holy living and rural outreach.",
            totalChurches = 54
        ),
        Denomination(
            id = "den_pcea",
            name = "Presbyterian Church of East Africa (PCEA)",
            code = "PCEA",
            doctrineSummary = "Reformed Presbyterian governance, elder leadership, and community education.",
            totalChurches = 39
        )
    )

    val churches = listOf(
        Church(
            id = "ch_citam_vr",
            denominationId = "den_citam",
            denominationName = "CITAM",
            name = "CITAM Valley Road",
            branch = "Main Sanctuary",
            city = "Nairobi",
            address = "Valley Road, Opposite Integrity Centre",
            phone = "+254 709 891 000",
            paybillNumber = "510800",
            accountName = "CITAMVR",
            memberCount = 6800,
            services = listOf(
                ChurchService("srv_1", "Early Bird English Service", "Sunday", "07:30 AM", "Main Auditorium"),
                ChurchService("srv_2", "Second Main Worship Service", "Sunday", "09:30 AM", "Main Auditorium"),
                ChurchService("srv_3", "Youth Extreme Celebration", "Sunday", "11:45 AM", "Chapel Hall"),
                ChurchService("srv_4", "Midweek Prayer & Holy Communion", "Wednesday", "05:30 PM", "Main Auditorium")
            )
        ),
        Church(
            id = "ch_ack_asc",
            denominationId = "den_ack",
            denominationName = "ACK",
            name = "All Saints' Cathedral",
            branch = "Kenyatta Avenue",
            city = "Nairobi",
            address = "Kenyatta Avenue / Processional Way",
            phone = "+254 702 645 069",
            paybillNumber = "303030",
            accountName = "ALLSAINTS",
            memberCount = 4200,
            services = listOf(
                ChurchService("srv_5", "Traditional Matins & Holy Communion", "Sunday", "07:00 AM", "Cathedral"),
                ChurchService("srv_6", "Swahili Morning Service (Kiswahili)", "Sunday", "08:30 AM", "Cathedral"),
                ChurchService("srv_7", "Contemporary Family Choral Service", "Sunday", "10:30 AM", "Cathedral")
            )
        ),
        Church(
            id = "ch_catholic_basilica",
            denominationId = "den_catholic",
            denominationName = "RC",
            name = "Holy Family Minor Basilica",
            branch = "CBD City Centre",
            city = "Nairobi",
            address = "Parliament Road & City Hall Way",
            phone = "+254 20 222 3202",
            paybillNumber = "247247",
            accountName = "BASILICA",
            memberCount = 5500,
            services = listOf(
                ChurchService("srv_8", "English Mass", "Sunday", "07:00 AM", "Basilica Sanctuary"),
                ChurchService("srv_9", "Misa Takatifu ya Kiswahili", "Sunday", "09:00 AM", "Basilica Sanctuary"),
                ChurchService("srv_10", "Solemn High Mass with Choir", "Sunday", "11:00 AM", "Basilica Sanctuary")
            )
        ),
        Church(
            id = "ch_aic_milimani",
            denominationId = "den_aic",
            denominationName = "AIC",
            name = "AIC Milimani Sanctuary",
            branch = "Milimani Lakeside",
            city = "Kisumu",
            address = "Tom Mboya Highway, Milimani",
            phone = "+254 722 901 443",
            paybillNumber = "888222",
            accountName = "AICMILIMANI",
            memberCount = 2100,
            services = listOf(
                ChurchService("srv_11", "Swahili Morning Worship", "Sunday", "08:00 AM", "Lakeside Sanctuary"),
                ChurchService("srv_12", "English Celebration Service", "Sunday", "10:30 AM", "Lakeside Sanctuary")
            )
        )
    )

    val currentMember = Member(
        id = "mem_001",
        name = "Trevol MacCoy",
        phone = "+254 712 345 678",
        email = "trevolmaccoy14@gmail.com",
        gender = "Male",
        churchId = "ch_citam_vr",
        churchName = "CITAM Valley Road",
        branch = "Main Sanctuary",
        denominationId = "den_citam",
        baptismStatus = "Baptized by Immersion",
        membershipNumber = "KAN-VR-2024-884",
        role = UserRole.SUPER_ADMIN,
        groupsJoined = listOf("Praise & Worship Choir", "Youth Ministry"),
        joinYear = "2021"
    )

    val allMembers = listOf(
        currentMember,
        Member(
            id = "mem_002",
            name = "Pastor David Mwangi",
            phone = "+254 722 110 220",
            email = "pastor.david@citam.org",
            gender = "Male",
            churchId = "ch_citam_vr",
            churchName = "CITAM Valley Road",
            branch = "Main Sanctuary",
            denominationId = "den_citam",
            baptismStatus = "Ordained Minister",
            membershipNumber = "KAN-VR-2015-012",
            role = UserRole.CHURCH_ADMIN,
            groupsJoined = listOf("Pastoral Team", "Men of Purpose"),
            joinYear = "2015"
        ),
        Member(
            id = "mem_003",
            name = "Grace Wanjiku",
            phone = "+254 733 445 566",
            email = "grace.wanjiku@gmail.com",
            gender = "Female",
            churchId = "ch_citam_vr",
            churchName = "CITAM Valley Road",
            branch = "Main Sanctuary",
            denominationId = "den_citam",
            baptismStatus = "Baptized by Immersion",
            membershipNumber = "KAN-VR-2019-304",
            role = UserRole.LEADER,
            groupsJoined = listOf("Praise & Worship Choir", "Daughters of Zion"),
            joinYear = "2019"
        ),
        Member(
            id = "mem_004",
            name = "Bishop Joseph Ochieng",
            phone = "+254 721 889 900",
            email = "bishop.ochieng@ack.org",
            gender = "Male",
            churchId = "ch_ack_asc",
            churchName = "All Saints' Cathedral",
            branch = "Kenyatta Avenue",
            denominationId = "den_ack",
            baptismStatus = "Confirmed & Consecrated",
            membershipNumber = "KAN-ACK-2010-001",
            role = UserRole.DENOMINATION_ADMIN,
            groupsJoined = listOf("House of Bishops", "Synod Council"),
            joinYear = "2010"
        ),
        Member(
            id = "mem_005",
            name = "Faith Chebet",
            phone = "+254 718 223 344",
            email = "faith.chebet@yahoo.com",
            gender = "Female",
            churchId = "ch_citam_vr",
            churchName = "CITAM Valley Road",
            branch = "Main Sanctuary",
            denominationId = "den_citam",
            baptismStatus = "Baptized by Immersion",
            membershipNumber = "KAN-VR-2023-712",
            role = UserRole.MEMBER,
            groupsJoined = listOf("Youth Ministry", "Hospitality"),
            joinYear = "2023"
        )
    )

    val hymns = listOf(
        Hymn(
            id = "hymn_1",
            number = 1,
            titleEn = "Rock of Ages, Cleft for Me",
            titleSw = "Mwamba Wenye Imara",
            category = "Praise & Grace",
            denomination = "Universal",
            keySignature = "B♭ Major",
            versesEn = listOf(
                "Rock of Ages, cleft for me, let me hide myself in Thee;\nLet the water and the blood, from Thy wounded side which flowed,\nBe of sin the double cure, save from wrath and make me pure.",
                "Not the labors of my hands can fulfill Thy law's demands;\nCould my zeal no respite know, could my tears forever flow,\nAll for sin could not atone; Thou must save, and Thou alone.",
                "While I draw this fleeting breath, when mine eyes shall close in death,\nWhen I soar through tracts unknown, see Thee on Thy judgment throne,\nRock of Ages, cleft for me, let me hide myself in Thee."
            ),
            versesSw = listOf(
                "Mwamba wenye imara, kwako nitajificha;\nMaji hayo na damu, yaliyotoka humu;\nKwenye moyo wa Yesu, yatanisafisha tu.",
                "Kazi zangu zote pia, haziwezi kuokoa;\nNijapolia sana, na kufanya bidii pia;\nDhambi haitafutika, ni Wewe wa kuokoa.",
                "Sina cha mkononi mwangu, naja tu msalabani;\nUchi naja kwako Nguo, mnyonge nisaidie;\nMuchafu naja chemchemi, nioshe nisife mimi."
            ),
            isFavorite = true
        ),
        Hymn(
            id = "hymn_2",
            number = 12,
            titleEn = "What a Friend We Have in Jesus",
            titleSw = "Yesu Kwetu ni Rafiki",
            category = "Prayer & Fellowship",
            denomination = "Universal",
            keySignature = "F Major",
            versesEn = listOf(
                "What a friend we have in Jesus, all our sins and griefs to bear!\nWhat a privilege to carry everything to God in prayer!\nO what peace we often forfeit, O what needless pain we bear,\nAll because we do not carry everything to God in prayer.",
                "Have we trials and temptations? Is there trouble anywhere?\nWe should never be discouraged; take it to the Lord in prayer.\nCan we find a friend so faithful who will all our sorrows share?\nJesus knows our every weakness; take it to the Lord in prayer."
            ),
            versesSw = listOf(
                "Yesu kwetu ni Rafiki, hwambiwa haja pia;\nTukiomba kwa Babaye, maombi asikia;\nLakini twakosa mara, amani masikitiko;\nBure twajitwika shida, tusipoomba Mungu.",
                "Je, una mateso mengi, unajaribiwa pia?\nUsiache kufurahi, omba Yesu sikia;\nJe, una rafiki hapa, mwaminifu kama Yeye?\nYesu ajua udhaifu, omba upate nguvu."
            ),
            isFavorite = true
        ),
        Hymn(
            id = "hymn_3",
            number = 45,
            titleEn = "Amazing Grace",
            titleSw = "Neema ya Ajabu",
            category = "Salvation & Assurance",
            denomination = "Universal",
            keySignature = "G Major",
            versesEn = listOf(
                "Amazing grace! How sweet the sound that saved a wretch like me!\nI once was lost, but now am found; was blind, but now I see.",
                "'Twas grace that taught my heart to fear, and grace my fears relieved;\nHow precious did that grace appear the hour I first believed!",
                "Through many dangers, toils, and snares, I have already come;\n'Tis grace hath brought me safe thus far, and grace will lead me home."
            ),
            versesSw = listOf(
                "Neema ya ajabu sana, iliniokoa!\nNilipotea gizani, sasa naona njia.",
                "Neema hiyo ilinifunza, hofu yangu kuisha;\nJinsi ilivyo ya thamani, siku nilipoamini!",
                "Katika taabu na shida, nimepita salama;\nNeema imenilinda hapa, itanipeleka mbinguni."
            ),
            isFavorite = false
        ),
        Hymn(
            id = "hymn_4",
            number = 78,
            titleEn = "Blessed Assurance, Jesus Is Mine",
            titleSw = "Hakika ya Amani, Yesu Wangu",
            category = "Praise & Worship",
            denomination = "Universal",
            keySignature = "D Major",
            versesEn = listOf(
                "Blessed assurance, Jesus is mine! O what a foretaste of glory divine!\nHeir of salvation, purchase of God, born of His Spirit, washed in His blood.",
                "Perfect submission, all is at rest, I in my Savior am happy and blest,\nWatching and waiting, looking above, filled with His goodness, lost in His love."
            ),
            chorusEn = "This is my story, this is my song, praising my Savior all the day long;\nThis is my story, this is my song, praising my Savior all the day long.",
            versesSw = listOf(
                "Hakika ya amani, Yesu ni wangu! Nionje utukufu wa Mbinguni;\nMrithi wa wokovu, ununuzi wa Mungu, aliyeoshwa kwa Damu Yake.",
                "Kujisalimisha, raha kamili, ndani ya Mwokozi nimebarikiwa,\nNikingoja huku nikitazama juu, nimejaa wema Wake na pendo."
            ),
            chorusSw = "Huu ndio wimbo wangu, habari yangu; nikimsifu Mwokozi mchana kutwa;\nHuu ndio wimbo wangu, habari yangu; nikimsifu Mwokozi milele.",
            isFavorite = true
        ),
        Hymn(
            id = "hymn_5",
            number = 104,
            titleEn = "How Great Thou Art",
            titleSw = "Ee Bwana Mungu Nashangaa Kabisa",
            category = "Creation & Adoration",
            denomination = "Universal",
            keySignature = "B♭ Major",
            versesEn = listOf(
                "O Lord my God, when I in awesome wonder, consider all the worlds Thy hands have made;\nI see the stars, I hear the rolling thunder, Thy power throughout the universe displayed."
            ),
            chorusEn = "Then sings my soul, my Savior God, to Thee: How great Thou art! How great Thou art!\nThen sings my soul, my Savior God, to Thee: How great Thou art! How great Thou art!",
            versesSw = listOf(
                "Ee Bwana Mungu, nashangaa kabisa, nikitazama ulimwengu wote;\nNazo nyota, radi zikisikika, uwezo Wako unadhihirika."
            ),
            chorusSw = "Roho yangu hukuimbia Wewe: Jinsi ulivyo Mkuu, Mungu wangu!\nRoho yangu hukuimbia Wewe: Jinsi ulivyo Mkuu, Mungu wangu!",
            isFavorite = true
        )
    )

    val prayers = listOf(
        Prayer(
            id = "pr_1",
            title = "Sala ya Bwana (The Lord's Prayer)",
            category = "Foundation",
            denomination = "Universal",
            scriptureRef = "Mathayo 6:9-13",
            text = "Baba yetu uliye mbinguni, Jina lako litukuzwe;\nUfalme wako uje, Mapenzi yako yatimizwe hapa duniani kama huko mbinguni.\nUtupe leo riziki yetu ya kila siku;\nUtusamehe makosa yetu, kama nasi tunavyowasamehe waliotukosea.\nNa usitutie majaribuni, lakini utuokoe na yule mwovu;\nKwa kuwa ufalme ni wako, na nguvu, na utukufu, hata milele. Amina."
        ),
        Prayer(
            id = "pr_2",
            title = "Morning Collect for Grace & Provision",
            category = "Morning Liturgy",
            denomination = "Anglican (ACK) / General",
            scriptureRef = "Zaburi 143:8",
            text = "O Lord, our heavenly Father, almighty and everlasting God, who hast safely brought us to the beginning of this day: Defend us in the same with Thy mighty power; and grant that this day we fall into no sin, neither run into any kind of danger; but that all our doings, being ordered by Thy governance, may be righteous in Thy sight; through Jesus Christ our Lord. Amen."
        ),
        Prayer(
            id = "pr_3",
            title = "Ombi la Kulinda Jamii na Mazao (Family & Harvest)",
            category = "African Family Blessing",
            denomination = "Universal",
            scriptureRef = "Kumbukumbu la Torati 28:1-8",
            text = "Ee Mwenyezi Mungu, Mlinzi wa vizazi vyetu: Ubariki nyumba zetu, mashamba yetu na kazi za mikono yetu. Ulinde watoto wetu dhidi ya mitego ya giza, na ulete amani katika nchi yetu na kanisa lako takatifu. Fungua madirisha ya mbinguni na kumwaga baraka ya uaminifu. Katika Jina la Yesu Kristo Mwokozi wetu. Amina."
        ),
        Prayer(
            id = "pr_4",
            title = "Sala ya Malaika wa Bwana (The Angelus)",
            category = "Liturgy & Sacraments",
            denomination = "Catholic",
            scriptureRef = "Luka 1:26-38",
            text = "V. Malaika wa Bwana alimletea Maria habari:\nR. Naye akapata mimba kwa uwezo wa Roho Mtakatifu. (Salamu Maria...)\nV. Tazama, mimi ni mjakazi wa Bwana:\nR. Na ifanyike kwangu sawasawa na neno lako.\nV. Naye Neno akafanyika mwili:\nR. Akakaa kati yetu. Amina."
        )
    )

    val sermons = listOf(
        Sermon(
            id = "serm_1",
            title = "Walking in Divine Dominion & Covenant",
            preacher = "Rev. Bishop David Mwangi",
            date = "Oct 12, 2026",
            scripture = "Genesis 1:26-28; Romans 8:31-37",
            series = "The Victorious Church",
            durationMinutes = 48,
            notesSummary = "1. God designed the African church for leadership and light.\n2. Covenant loyalty breaks generational cycles of poverty.\n3. The Holy Spirit empowers believers in the marketplace and family.",
            isDownloaded = true
        ),
        Sermon(
            id = "serm_2",
            title = "Kuwa na Subira Katika Nyakati za Majaribu",
            preacher = "Pastor Mercy Ndwiga",
            date = "Oct 05, 2026",
            scripture = "Yakobo 1:2-4; Isaya 40:29-31",
            series = "Imani Yenye Matunda",
            durationMinutes = 42,
            notesSummary = "Mungu hajatukataza katika dhoruba. Subira huleta ukamilifu wa tabia ya Kristo na kuandaa kanisa kwa uvunaji mkuu.",
            isDownloaded = false
        ),
        Sermon(
            id = "serm_3",
            title = "Kingdom Stewardship & Financial Integrity",
            preacher = "Dr. Emmanuel Kariuki",
            date = "Sep 28, 2026",
            scripture = "Malachi 3:8-12; Luke 16:10-13",
            series = "Kingdom Economics",
            durationMinutes = 55,
            notesSummary = "Understanding tithes and offerings as acts of worship and communal empowerment. Transparency in church governance establishes trust.",
            isDownloaded = true
        ),
        Sermon(
            id = "serm_4",
            title = "The African Pulpit & The Next Generation",
            preacher = "Rev. Silas Kibet",
            date = "Sep 21, 2026",
            scripture = "Psalm 78:4-7; 1 Timothy 4:12",
            series = "Youth Rising",
            durationMinutes = 39,
            notesSummary = "Engaging modern youth with digital tools while holding fast to ancient biblical truth and holy living.",
            isDownloaded = false
        )
    )

    val initialGivingTransactions = listOf(
        GivingTransaction(
            id = "tx_001",
            memberId = "mem_001",
            memberName = "Trevol MacCoy",
            phone = "+254 712 345 678",
            amountKes = 12500.0,
            givingType = GivingType.TITHE,
            churchId = "ch_citam_vr",
            churchName = "CITAM Valley Road",
            mpesaReceipt = "SK92Q8H201",
            status = PaymentStatus.COMPLETED,
            timestamp = "14 Oct 2026, 10:14 AM"
        ),
        GivingTransaction(
            id = "tx_002",
            memberId = "mem_001",
            memberName = "Trevol MacCoy",
            phone = "+254 712 345 678",
            amountKes = 2000.0,
            givingType = GivingType.OFFERING,
            churchId = "ch_citam_vr",
            churchName = "CITAM Valley Road",
            mpesaReceipt = "SK71B9V114",
            status = PaymentStatus.COMPLETED,
            timestamp = "12 Oct 2026, 11:22 AM"
        ),
        GivingTransaction(
            id = "tx_003",
            memberId = "mem_001",
            memberName = "Trevol MacCoy",
            phone = "+254 712 345 678",
            amountKes = 5000.0,
            givingType = GivingType.BUILDING_FUND,
            churchId = "ch_citam_vr",
            churchName = "CITAM Valley Road",
            mpesaReceipt = "SK60L4M832",
            status = PaymentStatus.COMPLETED,
            timestamp = "05 Oct 2026, 09:45 AM"
        )
    )

    val initialAttendanceRecords = listOf(
        AttendanceRecord(
            id = "att_001",
            serviceId = "srv_2",
            serviceName = "Second Main Worship Service",
            memberId = "mem_001",
            memberName = "Trevol MacCoy",
            memberPhone = "+254 712 345 678",
            timestamp = "12 Oct 2026, 09:25 AM",
            checkInMethod = CheckInMethod.QR_SCAN,
            isSyncedOffline = true
        ),
        AttendanceRecord(
            id = "att_002",
            serviceId = "srv_2",
            serviceName = "Second Main Worship Service",
            memberName = "Grace Wanjiku",
            memberId = "mem_003",
            memberPhone = "+254 733 445 566",
            timestamp = "12 Oct 2026, 09:12 AM",
            checkInMethod = CheckInMethod.PHONE_NUMBER,
            isSyncedOffline = true
        ),
        AttendanceRecord(
            id = "att_003",
            serviceId = "srv_2",
            serviceName = "Second Main Worship Service",
            memberName = "Samuel Otieno",
            memberId = "mem_005",
            memberPhone = "+254 718 223 344",
            timestamp = "12 Oct 2026, 09:40 AM",
            checkInMethod = CheckInMethod.MANUAL,
            isSyncedOffline = true
        )
    )

    val ministryGroups = listOf(
        MinistryGroup(
            id = "grp_1",
            name = "Praise & Worship Choir (Sauti ya Ushindi)",
            category = "Worship & Music",
            leaderName = "Grace Wanjiku",
            meetingSchedule = "Thursdays 6:00 PM & Saturdays 3:00 PM",
            memberCount = 64,
            description = "Leading the congregation in anointed contemporary African praise, hymns, and spiritual songs.",
            isJoined = true
        ),
        MinistryGroup(
            id = "grp_2",
            name = "Youth & Young Adults (Firebrand Generation)",
            category = "Youth Ministry",
            leaderName = "Samuel Otieno",
            meetingSchedule = "Sundays 11:45 AM in Chapel Hall",
            memberCount = 185,
            description = "Discipleship, campus missions, career mentorship, and vibrant peer fellowship for ages 18-35.",
            isJoined = true
        ),
        MinistryGroup(
            id = "grp_3",
            name = "Men of Purpose (Wanaume wa Imani)",
            category = "Men's Fellowship",
            leaderName = "Elder Joshua Mutua",
            meetingSchedule = "1st Saturday of Month at 7:30 AM (Breakfast)",
            memberCount = 112,
            description = "Fostering godly character, family leadership, financial wisdom, and fatherhood mentorship.",
            isJoined = false
        ),
        MinistryGroup(
            id = "grp_4",
            name = "Daughters of Zion (Wanawake)",
            category = "Women's Fellowship",
            leaderName = "Mama Rachel Mwangi",
            meetingSchedule = "2nd Saturday of Month at 10:00 AM",
            memberCount = 140,
            description = "Intercessory prayer for families, benevolent visits to widows, and entrepreneurial training.",
            isJoined = false
        ),
        MinistryGroup(
            id = "grp_5",
            name = "Protocol & Hospitality Ushers",
            category = "Service & Welcome",
            leaderName = "Brother Kevin Koech",
            meetingSchedule = "Sundays 7:00 AM (Briefing)",
            memberCount = 45,
            description = "Warm welcoming, sanctuary order, seating guidance, and digital attendance recording.",
            isJoined = false
        )
    )

    val marketplaceItems = listOf(
        MarketplaceItem(
            id = "mkt_1",
            title = "Handcrafted Olive Wood Sanctuary Crosses & Bibles",
            category = "Crafts & Books",
            sellerName = "Brother Peter Mwanza",
            sellerPhone = "+254 722 789 012",
            churchBranch = "CITAM Valley Road",
            priceKes = 1850.0,
            description = "Artisanal Kenyan olive wood crosses, inscribed family bibles, and desk scripture blocks made by local youth artisans."
        ),
        MarketplaceItem(
            id = "mkt_2",
            title = "Sound Engineering & Church Audio PA Setup",
            category = "Audio / Visual",
            sellerName = "Faith Sounds & Media",
            sellerPhone = "+254 701 456 789",
            churchBranch = "All Saints' Cathedral",
            priceKes = 8500.0,
            description = "Church event audio recording, livestream camera setup, choir microphone tuning, and acoustic consultation."
        ),
        MarketplaceItem(
            id = "mkt_3",
            title = "Fresh Organic Honey & Farm Produce (Church Co-op)",
            category = "Food & Farm",
            sellerName = "Elder Joshua Farm Produce",
            sellerPhone = "+254 733 901 234",
            churchBranch = "AIC Milimani Kisumu",
            priceKes = 950.0,
            description = "100% pure raw unprocessed acacia honey in 1kg jars, directly from rural parish farmers supporting rural churches."
        ),
        MarketplaceItem(
            id = "mkt_4",
            title = "Custom Choir Uniforms & African Kitenge Vestments",
            category = "Apparel",
            sellerName = "Sister Dorcas Couture",
            sellerPhone = "+254 712 678 901",
            churchBranch = "Holy Family Basilica",
            priceKes = 3200.0,
            description = "Tailor-made choir robes, pastoral stoles, and African print Sunday wear stitched with high-grade African cotton."
        ),
        MarketplaceItem(
            id = "mkt_5",
            title = "Certified Solar Power & Sanctuary Inverter Backup",
            category = "Technical Services",
            sellerName = "Eng. Daniel Ouma",
            sellerPhone = "+254 720 334 556",
            churchBranch = "CITAM Valley Road",
            priceKes = 15000.0,
            description = "Prevent service blackouts! Solar panels, lithium battery backup, and church generator transfer switch installation."
        )
    )

    val announcements = listOf(
        Announcement(
            id = "ann_1",
            title = "M-Pesa STK & Low-Data Church Portal Active",
            message = "All members can now give tithes and offerings directly via M-Pesa STK push even when offline! Draft requests sync as soon as network returns.",
            date = "Today",
            isUrgent = true,
            targetGroup = "All Members"
        ),
        Announcement(
            id = "ann_2",
            title = "Annual National Kesha & Revival Service",
            message = "Join all branches for our overnight prayer vigil this Friday from 9:00 PM. Theme: 'Revival in the Heartlands' (Habakkuk 3:2).",
            date = "Oct 24",
            isUrgent = true,
            targetGroup = "All Branches"
        ),
        Announcement(
            id = "ann_3",
            title = "Free Community Medical Camp & Screening",
            message = "In partnership with Christian Health Association, free dental, eye screening, and blood pressure checks after the 2nd service.",
            date = "Oct 26",
            isUrgent = false,
            targetGroup = "Community"
        )
    )
}
