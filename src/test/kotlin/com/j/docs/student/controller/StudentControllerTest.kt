package com.j.docs.student.controller

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.epages.restdocs.apispec.ResourceDocumentation
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.SimpleType
import com.j.docs.common.createRequest
import com.j.docs.common.response.CommonResponse
import com.j.docs.common.toObject
import com.j.docs.student.controller.request.StudentCreationRequest
import com.j.docs.student.service.StudentCreationService
import com.j.docs.student.service.StudentSearchService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpMethod
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
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

    @Test
    fun `학생 등록하기 - CREATED`() {
        willDoNothing().given(
            studentCreationService.create(
                studentFirstName = "진혁",
                studentLastName = "이",
                studentGrade = 3,
                studentClassroom = 4,
                studentNumber = 17,
            )
        )

        val requestBody = StudentCreationRequest(
            student = StudentCreationRequest.StudentInfo(
                firstName = "진혁",
                lastName = "이",
                grade = 3,
                classroom = 4,
                number = 17,
            )
        )

        val result = mockMvc.perform(
            createRequest(
                httpMethod = HttpMethod.POST,
                url = "/students",
                requestBody = requestBody,
            ))
            .andExpect(status().isCreated)
            .andDo(
                document(
                    "학생 등록하기 - CREATED",
                    "",
                    false,
                    ResourceDocumentation.resource(
                        ResourceSnippetParameters.builder()
                            .requestFields(
                                fieldWithPath("student.firstName")
                                    .type(SimpleType.STRING)
                                    .description("학생의 이름(first name)"),
                                fieldWithPath("student.lastName")
                                    .type(SimpleType.STRING)
                                    .description("학생의 성(last name)"),
                                fieldWithPath("student.grade")
                                    .type(SimpleType.INTEGER)
                                    .description("학생의 학년"),
                                fieldWithPath("student.classroom")
                                    .type(SimpleType.INTEGER)
                                    .description("학생의 반"),
                                fieldWithPath("student.number")
                                    .type(SimpleType.INTEGER)
                                    .description("학생의 번호"),
                            ).responseFields(
                                fieldWithPath("response")
                                    .type(JsonFieldType.NULL)
                                    .description("응답 데이터 없음"),
                                fieldWithPath("errorCode")
                                    .type(SimpleType.STRING)
                                    .description("에러 코드"),
                                fieldWithPath("errorMessage")
                                    .type(SimpleType.STRING)
                                    .description("에러 메시지"),
                            ).build()
                    )
                )
            )
            .andReturn()
            .response
            .contentAsString
            .toObject<CommonResponse<Nothing>>()

//        verify(studentCreationService).create(
//            studentFirstName = "진혁",
//            studentLastName = "이",
//            studentGrade = 3,
//            studentClassroom = 4,
//            studentNumber = 17,
//        )
        then(studentCreationService)
            .should()
            .create(
                studentFirstName = "진혁",
                studentLastName = "이",
                studentGrade = 3,
                studentClassroom = 4,
                studentNumber = 17,
            )

        assertThat(result.errorCode).isNull()
        assertThat(result.errorMessage).isNull()
    }
}