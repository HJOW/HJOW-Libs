# Maven Central 배포 방법 (Windows 기준)
* 기본 설정

gpg4win 설치 및 인증서 세팅이 완료되어야 함.

* GPG Agent 실행

    gpg-agent --daemon

* Maven 빌드

프로젝트 디렉토리에서 명령 수행 (중도 오류 발생 시 중단 !)

    mvn clean install
    mvn deploy
