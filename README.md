# 새모임 프로젝트🕊️
> 오프라인에서 취미활동을 함께 하거나, 혼자서는 갈 수 없는 곳에 함께 갈 파티원을 모집할 수 있는 서비스 

___
#### 팀원소개
``리더`` 이송언 <br> 
``부리더`` 장성준 <br>
``팀원`` 이지섭,  김현중
___ 
### 프로젝트 설명 </br>
새로운(New) 모임, 새(Bird)처럼 자유로운 모임 <br>
무엇이든지 함께 할 사람들을 구할 수 있는 새모임 프로젝트. <br>
오프라인 파티원 모집 플랫폼으로 모임을 생성하고 참여할 수 있으며, <br>
각 모임의 참여자들은 게시판을 이용해 모임 구성원들과 소통할 수 있습니다.
___
### 기술 스택</br>
[![My Skills](https://skillicons.dev/icons?i=java,spring,gradle)](https://skillicons.dev)
[![My Skills](https://skillicons.dev/icons?i=html,css,js,jquery)](https://skillicons.dev)

[![My Skills](https://skillicons.dev/icons?i=idea,git,github,postman)](https://skillicons.dev)

[![My Skills](https://skillicons.dev/icons?i=mysql,redis)](https://skillicons.dev)

<div>
  <img src="https://img.shields.io/badge/spring boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white"> <br>
  <img src="https://img.shields.io/badge/JSON_Web_Token-EF2D5E?style=for-the-badge&logo=JSON Web Tokens&logoColor=white">
</div>

___
### 목차 
<!-- TOC -->
* [새모임 프로젝트🕊️](#-)
   * [팀원소개](#)
    * [프로젝트 설명 </br>](#--br)
    * [기술 스택</br>](#--br)
    * [목차](#)
    * [프로젝트 개발 환경](#--)
    * [와이어 프레임](#-)
    * [서비스 아키텍처](#-)
    * [Class UML](#class-uml)
    * [ERD](#erd)
<!-- TOC -->
___
### 프로젝트 개발 환경
- spring-boot 3.0.2
- JDK 17
- build.gradle
<details><summary> 의존 주입
</summary>
<blockquote>

    dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.springframework.boot:spring-boot-starter-security'

    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'

    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.springframework.security:spring-security-test'

    testCompileOnly 'org.projectlombok:lombok'
    testAnnotationProcessor 'org.projectlombok:lombok'

    compileOnly group: 'io.jsonwebtoken', name: 'jjwt-api', version: '0.11.2'
    runtimeOnly group: 'io.jsonwebtoken', name: 'jjwt-impl', version: '0.11.2'
    runtimeOnly group: 'io.jsonwebtoken', name: 'jjwt-jackson', version: '0.11.2'

    implementation 'mysql:mysql-connector-java'
    implementation 'com.google.code.gson:gson:2.9.0'

    implementation 'org.springframework.boot:spring-boot-starter-data-redis'

    implementation 'org.springframework.boot:spring-boot-starter-mail'

    developmentOnly 'org.springframework.boot:spring-boot-devtools'
}
</blockquote>
</details>

___
### 와이어 프레임
<details><summary> wireFrame
</summary>
<img src="src/main/documents/wireFrame.png">
<img src="src/main/documents/wireFrame_02.png">
</details>

___

### 서비스 아키텍처
<details><summary>현재
</summary>
<img src="src/main/documents/serviceArchitecturecurr.png">
</details>  

<details><summary>완성 구상도
</summary>
<img src="src/main/documents/serviceArchitectureCompl.png">
</details>

___

### Class UML

<details><summary>UML
</summary>
<img src="src/main/documents/classUML.png">
</details>

### ERD
<details><summary> ERD
</summary><img src="src/main/documents/ERD.png">
</details>

---