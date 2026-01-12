package main.secondary.archer;

/**
 * 명사수 패시브 목록
 */
public enum MasterArcherPassive {
    /**
     * 적용된 패시브 없음
     */
    NONE,

    /**
     * 파워샷 : 이번 턴의 기본 공격 데미지가 D6은 D8로, 2D8은 2D12로 변경됩니다. (스태미나 1 소모)
     */
    POWER_SHOT,

    /**
     * 분열 화살 : 이번 턴의 기본 공격 데미지가 D6은 2D4로, 2D8은 4D6으로 변경됩니다. (스태미나 1 소모)
     */
    SPLIT_ARROW,

    /**
     * 관통 화살 : 이번 턴의 기본 공격 데미지가 D6은 2D12로, 2D8은 D20으로 변경됩니다. (스태미나 0 소모)
     */
    PENETRATING_ARROW,

    /**
     * 폭탄 화살 : 이번 턴의 기본 공격 데미지가 2배로 증가합니다. (스태미나 2 소모)
     */
    EXPLOSIVE_ARROW,

    /**
     * 더블 샷 : 기본 공격을 2회 사용합니다. 순환에 영향을 받지 않습니다. (스태미나 3 소모)
     */
    DOUBLE_SHOT
}
