package com.woo.peton.domain.model

enum class ReportType(val label: String, val colorHex: Long) {
    MISSING("실종", 0xFFD35A3C),      // 주황색
    SPOTTED("목격", 0xFF2196F3),      // 파란색
    PROTECTION("임보", 0xFF865E3F)    // 갈색
}
