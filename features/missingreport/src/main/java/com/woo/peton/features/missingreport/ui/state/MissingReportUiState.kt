package com.woo.peton.features.missingreport.ui.state

import com.woo.peton.domain.model.MissingPet
import com.woo.peton.domain.model.ReportType

// 3. UI 상태 (State)
data class MissingReportUiState(
    // 전체 데이터 리스트 (서버에서 받아온 원본)
    val allPets: List<MissingPet> = emptyList(),

    // 현재 활성화된 필터 (Key: 타입, Value: 활성 여부)
    // 초기값: 모든 타입 true (전체 보기)
    val filters: Map<ReportType, Boolean> = ReportType.entries.associateWith { true },

    // 로딩 상태
    val isLoading: Boolean = false
) {
    // 🟢 필터링된 리스트를 반환하는 헬퍼 프로퍼티
    // UI에서는 이 리스트를 관찰해서 그리면 됩니다.
    val currentPets: List<MissingPet>
        get() = allPets.filter { pet ->
            filters[pet.reportType] == true
        }
}