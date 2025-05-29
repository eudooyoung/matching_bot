<style>
pre code {
  font-size: 13px;
}
</style>

## 🧹 MatchingBot 로컬 초기화 가이드 (main 병합 이후)

main 브랜치 병합 이후, 빌드 오류 또는 실행 오류가 발생할 수 있습니다.  
MapStruct나 자동 생성 파일 문제로 인해 발생할 수 있는 문제들을 방지하기 위해,  
다음 단계를 **최초 1회 실행**해주세요.

---

### ✅ 초기화 단계 (터미널 명령)

1. ./gradlew clean
2. rm -rf build/ out/ src/main/generated/    
- *(powershell: Remove-Item -Recurse -Force build, out, src\main\generated)*
3. git clean -fd  # ⚠ 커밋되지 않은 파일/디렉토리는 모두 삭제됩니다!

⚠ git clean -fd 명령은 Git이 추적하지 않는 모든 파일과 폴더를 삭제합니다.  
커밋하지 않은 중요한 파일이 있다면 반드시 백업 후 실행하세요.

---

### 💡 IntelliJ 사용자 추가 조치

1. 메뉴 상단 → File > Invalidate Caches / Restart...
2. Restart 후, 프로젝트 다시 열기
3. 빌드 실행: 
   ./gradlew build

---

### 🎯 이 초기화가 필요한 이유

| 문제 상황                           | 원인                              | 해결 방법                 |
|------------------------------------|-----------------------------------|---------------------------|
| MapStruct / QueryDSL 에러          | 이전 자동 생성 파일이 남아 있음  | rm -rf build/ 로 제거     |
| 템플릿 에러 (TemplateInputException) | IntelliJ 캐시 꼬임                | 캐시 무효화 후 재시작     |
| Bean 충돌                          | src/main/generated 등 수동 폴더  | 해당 디렉토리 삭제        |

---

### 📌 참고 사항

- .gitignore 설정으로 인해 자동 생성 파일은 Git에 커밋되지 않지만,  
  로컬에는 남아 있어서 충돌을 일으킬 수 있습니다.
- 위 초기화 작업은 main 병합 이후 첫 빌드 전 단 1회만 실행하면 됩니다.