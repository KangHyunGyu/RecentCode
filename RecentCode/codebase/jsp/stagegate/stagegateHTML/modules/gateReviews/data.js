const headers = {
  review1: "실시시기 : 프로젝트 수주 후 실시",
  review2: "실시시기 : 프로토 툴링 시작하기 전 실시",
  review3: "실시시기 : DV완료 후 양산 모델고정 및 양산 툴링 Kick off시 실시",
  review4: "실시시기 : 양산 툴링에서 제품 생산 후 실시",
  review5: "실시시기 : PPAP/ISIR승인 완료 후 실시 (판가도 확정되면 좋음)",
  review6: "실시시기 : 양산완료 후 3개월 후 (현대: 품질 100일 작전 완료 후) 실시"
};

const status = [
  { title: "", classname: "none" },
  { title: "G", classname: "signal_G" },
  { title: "Y", classname: "signal_Y" },
  { title: "R", classname: "signal_R" },
];

const rows = {
  review1: [
    {
      title:
        "경영진과 Core team (CFT)의 멤버 승인 : 팀원의 역량/ Workload등에 대한 검증 및 승인",
      manager: "PM",
    },
    {
      title: "일정 확인 및 프로젝트 목표설정/ Gate Review일정 셋팅",
      manager: "PM",
    },
    {
      title: "수익성 목표 확인 및 Target 설정",
      manager: "경영관리",
    },
    {
      title: "수주상태 및 조건 공유 및 Deviation확인",
      manager: "영업",
    },
    {
      title: "특별한 고객 요구사항에 대해 개발 타당성 확인 개발 목표 설정 ",
      manager: "개발",
    },
    {
      title: "품질 목표 설정 (내부/고객 PPM)",
      manager: "품질",
    }
  ],
  review2: [
    { title: "프로토타입 오더 수량 확인", manager: "영업" },
    { title: "DFMEA 진행 상황 평가 (Action plan 시작)", manager: "PM" },
    { title: "프로토 타입 설계 및 도면 고정 (사양, 공차 및 특별 특성 포함)", manager: "설계" },
    { title: "연구소 / 생기 / 제조 업무분장 재확인", manager: "PM" },
    { title: "공장의 기존 설비 이용 방안 재확인 (견적시의 조건과 확인)", manager: "제조" },
    { title: "설비 발주 계획 확인 및 프로젝트 개발 계획과 정합성 확인", manager: "생기" },
    { title: "외주 부품/금형 발주 계획 확인 및 프로젝트 개발 계획과 정합성 확인", manager: "개발" }
  ],
  review3: [
    { title: "양산 툴링 PO 접수 확인", manager: "영업" },
    { title: "DFMEA 진행 평가", manager: "설계" },
    { title: "양산 모델 고정 및 설계 및 도면 준비 여부 확인 (사양, 공차 및 특별 특성 포함)", manager: "설계" },
    { title: "프로토 타입의 검증 결과 평가 (DV 완료)", manager: "설계" },
    { title: "공장 CAPA재확인 (원활한 업무분장 진행 포함)", manager: "제조" },
    { title: "설비 발주 계획 진행 상황 확인 및 프로젝트 개발 계획과 정합성 재확인", manager: "생기" },
    { title: "프로토 제품 품질 및 고객 concern 개선 여부 확인", manager: "설계" },
    { title: "PFMEA 진행상황 확인", manager: "생기" },
    { title: "공장 설비 Line 준비 상황 등 재확인 (Lay out/ 효용성/ OEE /OLE/ 고객발주 system과 정합성여부 등)", manager: "생기" },
    { title: "DFM&A 확인 (Design for Manufacturing&Assembly) : PFD로 대체 가능 확인", manager: "제조" },
    { title: "외주부품/금형 준비 상황 확인 및 프로젝트 개발 계획과 정합성 확인", manager: "개발" },
    { title: "내부 단가협의 진행 상황 확인 및 발주 현황 확인", manager: "개발" }
  ],
  review4: [
    { title: "DFMEA 평가 ( 0 RED Risk : RPN < 100)  @GR4", manager: "설계" },
    { title: "Machine 공정능력 산출 (Cm-Cmk) : Target>50%", manager: "생기" },
    { title: "Machine 공정능력 평가 (Cm-Cmk) : Target>50%", manager: "생기" },
    { title: "초기 생산제품에 대한 설비 정합성 확인", manager: "생기" },
    { title: "공장 설비 Line 준비 상황등 업데이트 상황 확인", manager: "생기" },
    { title: "PFMEA 진행상황 평가", manager: "생기" },
    { title: "설비 Error-proofing설치 및 누락 여부 / 크리티칼 파라미터(온도,압력 등) 관리 방안 및 Action plan확인 ", manager: "생기" },
    { title: "프로젝트 계획에 맞는 양산 툴링 가용성 확인 (외주부품 툴링 포함)", manager: "구매" },
    { title: "내부 프로세스 기준으로 공장 생산설비 set up여부 확인", manager: "생기" },
    { title: "외주부품 포장 스펙 및 물류 검증", manager: "구매" },
    { title: "모든 외주부품들 PPAP 취합 (본 단계에서는 interim PPAP도 승인 가능)", manager: "구매" },
    { title: "모든 외주부품들 PPAP 검토 및 승인 (본 단계에서는 interim PPAP도 승인 가능)", manager: "품질" },
    { title: "PV 시작시 문제점 및 일정 확인", manager: "품질" }
  ],
  review5: [
    { title: "공장 이관 미팅 시작", manager: "PM" },
    { title: "일시불 툴링비용 회수 여부 확인 (견적제출여부 및 네고 진행상황확인)", manager: "영업"},
    { title: "사급품에대해 사급품 업체(혹은 고객)와 명확한 책임소재 확인", manager: "영업" },
    { title: "PV  및 인증 완료 여부 확인 ", manager: "품질" },
    { title: "프로세스 Capability 확인 (제품 산포가 타겟대비 80%달성필요)", manager: "제조" },
    { title: "관리계획서 업데이트 확인", manager: "생기" },
    { title: "공정감사 / PPAP_ISIR 승인 / 연속생산 상황 확인 및 개선내용 적용 확인", manager: "품질" },
    { title: "선행양산 품질 및 결과 확인", manager: "품질" },
    { title: "PFMEA 진행상황 평가  ( 0 RED Risk : RPN < 100)  @GR5", manager: "생기" },
    { title: "설비 Error-proofing설치 및 누락 여부 / 크리티칼 파라미터(온도,압력 등) 관리 방안 및 Action plan 최종 확인 ", manager: "생기" },
    { title: "외주부품 포장 및 물류 최종 확인", manager: "구매" },
    { title: "작업계획서 및 작업자 트레이닝 완료 여부 확인", manager: "제조" },
    { title: "외주부품 양산승인 완료 여부 및  선행양산 품질 최종 확인", manager: "품질" }
  ],
  review6: [
    { title: "공장 이관 완료 ( PPM, OEE, OLE에 대해 확인 후 이관)", manager: "PM"},
    { title: "Lesson learned(레슨런)에 대한 검증 및 등록", manager: "PM" },
    { title: "설변 진행 상황 혹은 예견된 설변 상황 여부 확인", manager: "설계" },
    { title: "최종 프로세스 Capability 확인 (제품 산포가 타겟대비 100%달성필요) : Cpk ≥ 1.67 (고객사 요청대비 높게 설정)", manager: "제조" },
    { title: "최종 관리계획서 업데이트 확인", manager: "생기" },
    { title: "초기유동관리 지수 확인 및 Action 진행 및 완료여부 확인", manager: "품질" },
  ],
};
