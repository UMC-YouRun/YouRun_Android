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
| **IDE** | Android Studio |
| **Language** | Kotlin (JVM Target 11) |
| **Min / Target SDK** | API 31 / API 34 |

### Libraries & Frameworks
* **UI Architecture:** MVVM Pattern, Single Activity Architecture (Navigation Component)
* **UI Implementation:** XML Layouts, DataBinding, ViewBinding, RecyclerView, SwipeRefreshLayout
* **Design & Motion:** Material Components, Lottie (Animations), ConstraintLayout
* **Network:** Retrofit2, OkHttp, Gson Converter
* **Map & Location:** Kakao Map SDK, Google Fused Location Provider, Google Places API
* **Security & Auth:** Kakao SDK (Login), JJWT (JWT Handling)
* **Utils:** Jsoup (HTML Parsing), Firebase App Distribution

---

## 🏗 Architecture
이 프로젝트는 **Android View System**과 **MVVM 패턴**을 기반으로 안정적인 구조를 갖추고 있습니다. **Jetpack Navigation**을 활용하여 Single Activity 구조로 화면 전환을 관리합니다.
This project relies on the **Android View System** and **MVVM pattern** for a robust structure. It manages screen transitions using a Single Activity structure powered by **Jetpack Navigation**.

### 📂 Key Features of Architecture
* **ViewBinding & DataBinding:** XML 레이아웃과 비즈니스 로직을 효율적으로 연결하여 보일러플레이트 코드를 줄였습니다.
* **Separation of Concerns:** UI(Fragment)와 로직(ViewModel)을 분리하여 유지보수성을 높였습니다.
* **Secret Management:** API 키 등 민감한 정보는 `local.properties`와 `BuildConfig`를 통해 안전하게 관리합니다.

```mermaid
graph LR
    Fragment["Fragment (UI)"] --> ViewModel
    ViewModel --> Repository["Repository"]
    Repository --> Remote["Remote Data (Retrofit/Jsoup)"]
    Repository --> Local["Local Data (SharedPrefs)"]
