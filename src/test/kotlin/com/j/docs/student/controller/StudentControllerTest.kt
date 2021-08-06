package com.j.docs.student.controller

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.epages.restdocs.apispec.ResourceDocumentation
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.j.docs.common.createRequest
import com.j.docs.common.getDocumentRequest
import com.j.docs.common.getDocumentResponse
import com.j.docs.common.response.CommonResponse
import com.j.docs.common.toObject
import com.j.docs.student.controller.response.StudentAllSearchResponse
import com.j.docs.student.dto.StudentSearchDto
import com.j.docs.student.service.StudentCreationService
import com.j.docs.student.service.StudentSearchService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpMethod
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(StudentController::class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
internal class StudentControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var studentCreationService: StudentCreationService

    @MockBean
    private lateinit var studentSearchService: StudentSearchService

    private val savedStudents = listOf(
        StudentSearchDto(1, "진혁 이", "3417"),
        StudentSearchDto(2, "진혁 리", "3517"),
    )

    @Test
    fun `모든 학생 조회하기 - OK`() {
        given(studentSearchService.searchAll())
            .willReturn(savedStudents)

        val result = mockMvc.perform(
            createRequest<Nothing>(
                httpMethod = HttpMethod.GET,
                url = "/students",
            ))
            .andExpect(status().isOk)
            .andDo(
                document(
                    "testA",
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
                )
            )
            .andReturn()
            .response
            .contentAsString
            .toObject<CommonResponse<StudentAllSearchResponse>>()

        println("result: $result")
    }

//    @Test
//    fun `학생 등록하기 - CREATED`() {
//        willDoNothing().given(
//            studentCreationService.create(
//                studentFirstName = "진혁",
//                studentLastName = "이",
//                studentGrade = 3,
//                studentClassroom = 4,
//                studentNumber = 17,
//            )
//        )
//
//        val requestBody = StudentCreationRequest(
//            student = StudentCreationRequest.StudentInfo(
//                firstName = "진혁",
//                lastName = "이",
//                grade = 3,
//                classroom = 4,
//                number = 17,
//            )
//        )
//
//        val result = mockMvc.perform(
//            createRequest(
//                httpMethod = HttpMethod.POST,
//                url = "/students",
//                requestBody = requestBody,
//            ))
//            .andExpect(status().isCreated)
//            .andDo(
//                document(
//                    "학생 등록하기 - CREATED",
//                    "",
//                    false,
//                    ResourceDocumentation.resource(
//                        ResourceSnippetParameters.builder()
//                            .requestFields(
//                                fieldWithPath("student.firstName")
//                                    .type(SimpleType.STRING)
//                                    .description("학생의 이름(first name)"),
//                                fieldWithPath("student.lastName")
//                                    .type(SimpleType.STRING)
//                                    .description("학생의 성(last name)"),
//                                fieldWithPath("student.grade")
//                                    .type(SimpleType.INTEGER)
//                                    .description("학생의 학년"),
//                                fieldWithPath("student.classroom")
//                                    .type(SimpleType.INTEGER)
//                                    .description("학생의 반"),
//                                fieldWithPath("student.number")
//                                    .type(SimpleType.INTEGER)
//                                    .description("학생의 번호"),
//                            ).responseFields(
//                                fieldWithPath("response")
//                                    .type(JsonFieldType.NULL)
//                                    .description("응답 데이터 없음"),
//                                fieldWithPath("errorCode")
//                                    .type(SimpleType.STRING)
//                                    .description("에러 코드"),
//                                fieldWithPath("errorMessage")
//                                    .type(SimpleType.STRING)
//                                    .description("에러 메시지"),
//                            ).build()
//                    )
//                )
//            )
//            .andReturn()
//            .response
//            .contentAsString
//            .toObject<CommonResponse<Nothing>>()
//
////        verify(studentCreationService).create(
////            studentFirstName = "진혁",
////            studentLastName = "이",
////            studentGrade = 3,
////            studentClassroom = 4,
////            studentNumber = 17,
////        )
//        then(studentCreationService)
//            .should()
//            .create(
//                studentFirstName = "진혁",
//                studentLastName = "이",
//                studentGrade = 3,
//                studentClassroom = 4,
//                studentNumber = 17,
//            )
//
//        assertThat(result.errorCode).isNull()
//        assertThat(result.errorMessage).isNull()
//    }
}