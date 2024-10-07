# ![image](https://github.com/user-attachments/assets/dc078301-aa66-4562-8ae6-91fc777598e5) Account 프로젝트

## 목표
계좌 시스템은 사용자와 계좌의 정보를 저장하고 있으며, 외부 시스템에서 거래를 요청할 경우 거래 정보를 받아서 계좌에서 잔액을 거래금액만큼 줄이거나(결제), 거래금액만큼 늘리는(결제 취소) 거래 관리 기능을 제공하는 시스템입니다.

## 각 API 명세서

### 계좌

#### 1) 계좌 생성 API
- **URL:** `POST /account`
- **파라미터:**
  - 사용자 아이디
  - 초기 잔액
- **정책:**
  - 사용자가 없는 경우, 계좌가 10개(사용자당 최대 보유 가능 계좌 수)인 경우 실패 응답
- **성공 응답:**
  - 사용자 아이디
  - 계좌번호
  - 등록일시

#### 2) 계좌 해지 API
- **URL:** `DELETE /account`
- **파라미터:**
  - 사용자 아이디
  - 계좌 번호
- **정책:**
  - 사용자 또는 계좌가 없는 경우
  - 사용자 아이디와 계좌 소유주가 다른 경우
  - 계좌가 이미 해지 상태인 경우
  - 잔액이 있는 경우 실패 응답
- **성공 응답:**
  - 사용자 아이디
  - 계좌번호
  - 해지일시

#### 3) 계좌 확인 API
- **URL:** `GET /account?user_id={userId}`
- **파라미터:**
  - 사용자 아이디
- **정책:**
  - 사용자가 없는 경우 실패 응답
- **성공 응답:**
  - List<계좌번호, 잔액> 구조로 응답

### 거래 정보

#### 4) 잔액 사용 API
- **URL:** `POST /transaction/use`
- **파라미터:**
  - 사용자 아이디
  - 계좌 번호
  - 거래 금액
- **정책:**
  - 사용자가 없는 경우
  - 사용자 아이디와 계좌 소유주가 다른 경우
  - 계좌가 이미 해지 상태인 경우
  - 거래금액이 잔액보다 큰 경우
  - 거래금액이 너무 작거나 큰 경우 실패 응답
  - 해당 계좌에서 거래(사용, 사용 취소)가 진행 중일 때 다른 거래 요청이 오는 경우 해당 거래가 동시에 잘못 처리되는 것을 방지해야 함
- **성공 응답:**
  - 계좌번호
  - 거래 결과 코드(성공/실패)
  - 거래 아이디
  - 거래금액
  - 거래 일시

#### 5) 잔액 사용 취소 API
- **URL:** `POST /transaction/cancel`
- **파라미터:**
  - 거래 아이디
  - 취소 요청 금액
- **정책:**
  - 거래 아이디에 해당하는 거래가 없는 경우
  - 거래금액과 거래 취소 금액이 다른 경우(부분 취소 불가능)
  - 1년이 넘은 거래는 사용 취소 불가능
- **성공 응답:**
  - 계좌번호
  - 거래 결과 코드(성공/실패)
  - 거래 아이디
  - 거래금액
  - 거래 일시

#### 6) 거래 확인 API
- **URL:** `GET /transaction/{transactionId}`
- **파라미터:**
  - 거래 아이디
- **정책:**
  - 해당 거래 아이디의 거래가 없는 경우 실패 응답
- **성공 응답:**
  - 계좌번호
  - 거래 종류(잔액 사용, 잔액 사용 취소)
  - 거래 결과 코드(성공/실패)
  - 거래 아이디
  - 거래금액
  - 거래 일시

## 개발 환경
<img src="https://img.shields.io/badge/windows-0078D6?style=for-the-badge&logo=windows&logoColor=white"><img src="https://img.shields.io/badge/IntelliJ%20IDEA-000000?style=for-the-badge&logo=intellijidea&logoColor=white"><img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"><img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"><img src="https://img.shields.io/badge/H2-1C3B1F?style=for-the-badge&logo=h2database&logoColor=white"><img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"><img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">

## Dependencies
- Spring Data JPA
- Spring Web
- Validation 
- H2 Database
- Lombok
- redisson
- embedded redis
```
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    // redis client
    implementation 'org.redisson:redisson:3.17.1'
    // embedded redis
    implementation('it.ozimov:embedded-redis:0.7.3') {
        exclude group: "org.slf4j", module: "slf4j-simple"
    }
    compileOnly 'org.projectlombok:lombok'
    runtimeOnly 'com.h2database:h2'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
