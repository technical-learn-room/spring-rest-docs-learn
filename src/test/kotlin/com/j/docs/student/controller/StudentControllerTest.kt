package com.j.docs.student.controller

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.j.docs.common.*
import com.j.docs.common.response.CommonResponse
import com.j.docs.student.controller.request.StudentCreationRequest
import com.j.docs.student.controller.response.StudentAllSearchResponse
import com.j.docs.student.controller.response.StudentSearchResponse
import com.j.docs.student.dto.StudentSearchDto
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
import org.springframework.restdocs.payload.PayloadDocumentation.*
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

    @Test
    fun `특정 학생 조회하기 - OK`() {
        val returnValue = StudentSearchDto(
            studentId = 1,
            studentName = "jinhyeok lee",
            studentGradeClassNumber = "3417",
        )

        given(studentSearchService.search(
            studentId = anyLong(),
        )).willReturn(returnValue)

        val result = mockMvc.perform(
            createRequest<Nothing>(
                httpMethod = HttpMethod.GET,
                url = "/students/{studentId}",
                pathVariables = listOf("studentId" to 1),
            ))
            .andExpect(status().isOk)
            .andDo(
                document(
                    "특정 학생 조회하기 - OK",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    responseFields(
                        fieldWithPath("response.student.id")
                            .type(JsonFieldType.NUMBER)
                            .description("학생 아이디"),
                        fieldWithPath("response.student.name")
                            .type(JsonFieldType.STRING)
                            .description("학생 이름"),
                        fieldWithPath("response.student.gradeClassNumber")
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
            .toObject<CommonResponse<StudentSearchResponse>>()

        verify(studentSearchService).search(
            studentId = anyLong(),
        )

        assertThat(result.errorCode).isNull()
        assertThat(result.errorMessage).isNull()
        assertThat(result.response).isNotNull
        assertThat(result.response!!.student).isNotNull
        assertThat(result.response!!.student.id).isEqualTo(1)
        assertThat(result.response!!.student.name).isEqualTo("jinhyeok lee")
        assertThat(result.response!!.student.gradeClassNumber).isEqualTo("3417")
    }

    @Test
    fun `학생 등록하기 - Created`() {
        willDoNothing()
            .given(studentCreationService)
            .create(
                studentFirstName = anyString(),
                studentLastName = anyString(),
                studentGrade = anyInt(),
                studentClassroom = anyInt(),
                studentNumber = anyInt(),
            )

        val request = StudentCreationRequest(
            student = StudentCreationRequest.StudentInfo(
                firstName = "jinhyeok",
                lastName = "lee",
                grade = 3,
                classroom = 4,
                number = 17,
            )
        )

        val result = mockMvc.perform(
            createRequest(
                httpMethod = HttpMethod.POST,
                url = "/students",
                requestBody = request,
            ))
            .andExpect(status().isCreated)
            .andDo(
                document(
                    "학생 등록하기 - Created",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    requestFields(
                        fieldWithPath("student.firstName")
                            .type(JsonFieldType.STRING)
                            .description("학생 이름"),
                        fieldWithPath("student.lastName")
                            .type(JsonFieldType.STRING)
                            .description("학생 성"),
                        fieldWithPath("student.grade")
                            .type(JsonFieldType.NUMBER)
                            .description("학생 학년"),
                        fieldWithPath("student.classroom")
                            .type(JsonFieldType.NUMBER)
                            .description("학생 반"),
                        fieldWithPath("student.number")
                            .type(JsonFieldType.NUMBER)
                            .description("학생 번호"),
                    ),
                    responseFields(
                        fieldWithPath("response")
                            .type(JsonFieldType.NULL)
                            .description("응답 내용"),
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
            .toObject<CommonResponse<StudentSearchResponse>>()

        verify(studentCreationService)
            .create(
                studentFirstName = anyString(),
                studentLastName = anyString(),
                studentGrade = anyInt(),
                studentClassroom = anyInt(),
                studentNumber = anyInt(),
            )

        assertThat(result.response).isNull()
        assertThat(result.errorCode).isNull()
        assertThat(result.errorMessage).isNull()
    }
}