강시뮬레이터/GangSimulator - PC게임 메이플스토리 스타포스 강화 계산기

Covery를 통하여 Java를 이용한 안드로이드 앱 개발을 완료하여
Kotlin 언어를 통하여 앱 개발을 해보기 위하여 프로젝트 시작

특징
-강화 시뮬레이션 계산기를 통하여 상황별 평균값 및 상위구간의 평균값 검색가능

-액티비티 간 값을 넘기는 과정
값이 큰값을 액티비티간 넘기기 위하여 Parcelable 인터페이스를 구현한 클래스 사
특히 array값을 포함하기 때문에 ArrayList<Int>를 선언하고 이를 클래스 로더로 값을 넘김
이를 통해 액티비티간 여러개의 데이터를 데이터 클래스 형태로 변환하여 원활하게 전송 가능

-각 EditText에 number 및 자릿수의 제한을 두기
xml 페이지를 통하여  EditText의 자릿수 제한을 두고
activity 코드를 통하여 예외 처리를 함으로써 -입력 or 0 or 100이상 값과 같은 예외 상황에 대한 대처

-과도한 계산이 일어나 생각보다 오래걸릴때를 대비해 LodingView 제작

-동적할당 배열안에 시도횟수별 소비금액을 추가하여 검색기능 제공
Parceable 인터페이스를 통해 넘긴 data를 특정 구간에 따라 

-스피너의 요소별 text및 value값 매치

-MaterialButton 및 MPAndroidChart를 통해 토글버튼 및 그래프 구현 및 수정
MaterialButton 및 MPAndroidChart를 통하여 원하는 모양의 button을 직접 xml을 통하여 구현 가능하고 barchart 또한 커스터마이징 하여
average 값, value 값을 표시하여 직접적인 수치를 확인할 수 있게 만듦
특히, 위와같은 편차가 큰 data에서 하위 1퍼센트를 barchart에서 제외함으로써 chart가 너무 길어지지 않게 조정



![접속시화면](https://github.com/OhYunTaek123/GangSimulator/assets/128479666/dfbfd6ef-2d80-4932-aeed-3f8fb560a4cd)

접속시 화면
-원하는 데이터를 입력하여 결과값을 알 수 있음

![입력창](https://github.com/OhYunTaek123/GangSimulator/assets/128479666/d7ba897a-19e9-45cf-9149-6794b6884174)

시작 입력차

![토글버튼 및 스피너](https://github.com/OhYunTaek123/GangSimulator/assets/128479666/26bf3823-cb80-4a02-8f8f-e4b10b183132)

토글 버튼 및 스피너
-토글버튼과 스피너를 통하여 강화에서 일어날 수 있는 변수들을 조작하기 쉽게 만듦

![결과창](https://github.com/OhYunTaek123/GangSimulator/assets/128479666/b0c14d83-1b0e-48aa-84d9-5dd94dccead1)

결과창
-직관적인 인터페이스를 목적으로 제작

![결과창 상위 몇퍼센트 검색](https://github.com/OhYunTaek123/GangSimulator/assets/128479666/f340a443-ddd2-400e-ac82-2945367692c1)

결과창 상위% 검색 기능
-제일 원하던 기능인 운빨 검색기
-mainactivity에서 arraylist를 만들고 넘김으로써 이를 resultactivity에서도 조작 가능
