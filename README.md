# Spring Rest Docs로 테스트 코드와 Open API 동시에 잡기
![Spring Rest Docs Logo](https://user-images.githubusercontent.com/48639421/127739359-3c4982f1-378e-4df0-b690-c4a0083ed3ad.png)  

## Spring Rest Docs가 뭐야
`Spring Rest Docs`는 `Swagger`와 마찬가지로 `Open API`를 생성할 수 있는 라이브러리입니다.  

`Swagger`는 `Controller`단에 어노테이션을 붙여 `API`에 대한 설명을 덧붙일 수 있지만  
`Spring Rest Docs`는 테스트 코드를 작성함으로써 `Open API`를 구체화할 수 있습니다.  

또한 `Swagger`는 어노테이션 기반이기 때문에 구체화한 정보에 대한 검증이 이루어지지 않고  
그에 따라 `API`가 변하면서 바뀌어야하는 조건에 대한 정확성과 무결성이 깨지게 됩니다.  
하지만 `Spring Rest Docs`는 테스트 코드에서 실제 `Request`가 오지 않았거나, `Response`에 대한 결과가 맞지 않거나,  
타입이 맞지 않는 등의 `실제와 다른 정보`를 검증이 가능합니다.  
따라서 `Open API`와 실제 `API`의 동작 방식간의 일관성을 유지할 수 있습니다.  

## Spring Rest Docs 사용해보기
```kotlin

@Test
fun `모든 학생 조회하기 - OK`() {
    val returnValue = listOf(
        StudentSearchDto(
            studentId = 1,
            studentName = "jinhyeok lee",
            studentGradeClassNumber = "3417",
        ),
        StudentSearchDto(
            studentId = 2,
            studentName = "jinhyuk lee",
            studentGradeClassNumber = "3517",
        ),
    )

    given(studentSearchService.searchAll())
        .willReturn(returnValue)

    val result = mockMvc.perform(
        createRequest<Nothing>(
            httpMethod = HttpMethod.GET,
            url = "/students",
        ))
        .andExpect(status().isOk)
        .andDo(
            document(
                "모든 학생 조회하기 - OK",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                    fieldWithPath("response.students")
                        .type(JsonFieldType.ARRAY)
                        .description("조회한 학생들"),
                    fieldWithPath("response.students[].id")
                        .type(JsonFieldType.NUMBER)
                        .description("학생 아이디"),
                    fieldWithPath("response.students[].name")
                        .type(JsonFieldType.STRING)
                        .description("학생 이름"),
                    fieldWithPath("response.students[].gradeClassNumber")
                        .type(JsonFieldType.STRING)
                        .description("학생 학년-반-번호"),
                    fieldWithPath("errorCode")
                        .type(JsonFieldType.NULL)
                        .description("에러 코드"),
                    fieldWithPath("errorMessage")
                        .type(JsonFieldType.NULL)
                        .description("에러 메시지"),
                ),
            ),
        )
        .andReturn()
        .response
        .contentAsString
        .toObject<CommonResponse<StudentAllSearchResponse>>()

    verify(studentSearchService).searchAll()

    assertThat(result.errorCode).isNull()
    assertThat(result.errorMessage).isNull()
    assertThat(result.response).isNotNull
    assertThat(result.response!!.students)
        .map<Long> { it.id }
        .contains(1, 2)
    assertThat(result.response!!.students)
        .map<String> { it.name }
        .contains("jinhyeok lee", "jinhyuk lee")
    assertThat(result.response!!.students)
        .map<String> { it.gradeClassNumber }
        .contains("3417", "3517")
}
```
테스트 프레임워크는 `BDD-Mockito`를 이용하였습니다.  
겉보기에는 컨트롤러를 테스트하는 코드로 보이지만 눈에 띄는 코드가 있습니다.  
`andDo` 메소드를 통해 `document`를 생성하는 코드입니다.  
여기서는 위에서 말했던 것처럼 `Request`와 `Response`를 검증하는 역할을 합니다.  

첫 번째 인자로는 `API`의 이름을 등록하고,  
두, 세 번째 인자로는 `Request`와 `Response`에 대한 설정을 등록하는데  
여기서는 나중에 테스트 코드를 빌드하여 생성되는 `asciidoc`에서  
`Request`와 `Response`를 예쁘게 찍기 위한 설정을 해두었습니다.  

제일 중요한 건 마지막 인자입니다.  
여기서 `Response`를 검증하기 위해서는 `responseFields()` 메소드를,
`Request`를 검증하기 위해서는 `requestFields()` 메소드를 사용합니다.
다른 내용은 한 눈에 봐도 무슨 뜻인지 유추하기 쉽습니다.  

이렇게 도큐먼트를 생성한 뒤에는 원래 테스트 하던 것처럼  
`verify` 해주고, 응답 `assertion` 하시면 됩니다.  
