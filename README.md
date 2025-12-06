# YouRun_Android
YouRun Android Development Team
![스크린샷 2025-02-24 162102](https://github.com/user-attachments/assets/42a86d07-1760-4fd2-a055-282f5da9d0cb)
![스크린샷 2025-02-24 162129](https://github.com/user-attachments/assets/383e2aef-b5f3-4c13-9a56-ebbde7062d94)
![스크린샷 2025-02-24 162149](https://github.com/user-attachments/assets/cea5a5de-ee0d-46ef-af83-35ef9cff0305)



# YouRun
> **Android Running & Social Activity Platform**
>
> ユーザーのランニング体験を記録し、仲間と共有することでモチベーションを高めるAndroidアプリケーションです。
> An Android application that tracks running experiences and boosts motivation by sharing them with friends.

---

## 📱 Project Overview
이 프로젝트는 단순한 러닝 기록을 넘어, '함께 달리는 즐거움'을 제공하기 위해 개발되었습니다. 개인의 러닝 경로를 실시간으로 추적하고, 친구 또는 크루와 함께 챌린지에 도전하며 성취감을 공유할 수 있습니다.

This project was developed to provide the **"joy of running together"** beyond simple tracking. It tracks the user's running path in real-time and allows sharing achievements by challenging friends or crews.

---

## ✨ Key Features
* **📍 Real-time Tracking:** 러닝 중 사용자의 위치를 추적하여 경로, 거리, 속도를 시각적으로 표시 (Kakao Map SDK 활용)
* **🤝 Social & Crew:** 친구 추가 및 관리, 개인/크루 단위의 챌린지 경쟁 시스템
* **📅 Running Calendar:** 캘린더를 통해 날짜별 러닝 기록 및 통계 확인
* **🏆 Challenge System:** 목표 달성 시 배지 획득 및 랭킹 시스템 (Gamification)

---

## 🛠 Tech Stack

### Environment
| Category | Version / Detail |
| :--- | :--- |
| **IDE** | Android Studio Meerkat \| 2024.3.1 |
| **Language** | Kotlin 2.1.0 |
| **Min / Target SDK** | API 31 / API 34 |

### Libraries & Frameworks
* **UI:** Jetpack Compose (BOM 2025.03), Material 3
* **Architecture:** MVVM Pattern, Multi-Module Architecture
* **DI (Dependency Injection):** Hilt
* **Network:** Retrofit2, OkHttp
* **Concurrency:** Coroutines, Flow
* **Navigation:** Jetpack Navigation (Compose)
* **Local Data:** SharedPreference
* **Map/Location:** Kakao Map SDK, Lifecycle Runtime KTX (2.8.7)
* **Testing:** Android JUnit 5 (5.12.0)
* **Utils:** Core KTX (1.9.0)

---

## 🏗 Architecture
이 프로젝트는 **MVVM (Model-View-ViewModel)** 패턴을 기반으로 관심사를 분리하여, 코드의 가독성과 유지보수성을 높였습니다.
This project is based on the **MVVM (Model-View-ViewModel)** pattern, separating concerns to enhance code readability and maintainability.

### 📂 Package Structure
* **view:** Jetpack Compose로 구현된 UI 화면 (Activity, Screen Composable)
* **viewModel:** UI 상태(State) 관리 및 비즈니스 로직 처리 (`HiltViewModel` 활용)
* **model:** 데이터 클래스(Data Class), Repository 패턴을 통한 데이터 소스 관리
* **utils:** 확장 함수(Extension Functions) 및 공통 헬퍼 클래스

```mermaid
graph LR
    View["View (UI)"] --> ViewModel
    ViewModel --> Model["Model (Repository)"]
    Model --> Remote["Remote Data (Retrofit)"]
    Model --> Local["Local Data (SharedPrefs)"]
