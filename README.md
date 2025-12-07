# 🌲 SpringCafeProject

![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-232F3E?style=for-the-badge&logo=amazon-aws&logoColor=white)
![Nginx](https://img.shields.io/badge/Nginx-009639?style=for-the-badge&logo=nginx&logoColor=white)

Java Spring Boot 기반의 게시판 프로젝트입니다.
thymeleaf를 통해 View를 생성하고 게시판의 기본적인 CRUD 기능, Redis 캐싱, CI/CD + 무중단 배포를 중심으로 개발했습니다.

# 📖 개요

코드스쿼드 마스터즈 과정에서 진행했던 게시판 프로젝트에 다양한 시도를 해보고자 추가적으로 제가 고도화 시킨 프로젝트입니다.

# 💎 Main Features
- 기본적인 게시판 CRUD 기능 (게시글/댓글 CRUD, 게시글 임시저장, 로그인 및 인증/인가, 추천 등)
- 세션 기반 로그인
- github actions를 통한 CI/CD
- Nginx를 통한 무중단 배포
- 레디스 캐싱(게시물 단건 조회 시 적용)

  # ERD

  ![스프링카페 drawio - 복사본](https://github.com/user-attachments/assets/824300f0-69ef-42a2-aaab-32a3df6f55cc)

  # 🏛️ Depedency Used
  
```
spring-boot-starter-web
spring-boot-starter-validation
spring-boot-starter-data-jpa
spring-boot-starter-data-redis
com.h2database:h2
mysql:mysql-connector-java
spring-boot-starter-test
```
  
