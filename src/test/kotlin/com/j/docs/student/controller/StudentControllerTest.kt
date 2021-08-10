package com.j.docs.student.controller

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.j.docs.common.createRequest
import com.j.docs.common.getDocumentRequest
import com.j.docs.common.getDocumentResponse
import com.j.docs.common.response.CommonResponse
import com.j.docs.common.toObject
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
        StudentSearchDto(1, "jinhyeok lee", "3417"),
        StudentSearchDto(2, "jinhyuk lee", "3517"),
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
        willDoNothing()
            .given(studentSearchService)
            .search(anyLong())

        val result = mockMvc.perform(
            createRequest<Nothing>(
                httpMethod = HttpMethod.GET,
                url = "/students/{studentId}",
                pathVariables = listOf(1),
            ))
            .andExpect(status().isOk)
            .andDo(
                document(
                    "특정 학생 조회하기 - OK",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    responseFields(
                        fieldWithPath("response.student.id")
                            .type(JsonFieldType.ARRAY)
                            .description("학생 아이디"),
                        fieldWithPath("response.student.name")
                            .type(JsonFieldType.ARRAY)
                            .description("학생 이름"),
                        fieldWithPath("response.student.gradeClassNumber")
                            .type(JsonFieldType.ARRAY)
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

        verify(studentSearchService).search(anyLong())

        assertThat(result.errorCode).isNull()
        assertThat(result.errorMessage).isNull()
        assertThat(result.response).isNotNull
        assertThat(result.response!!.student).isNotNull
        assertThat(result.response!!.student.id).isEqualTo(1)
        assertThat(result.response!!.student.name).isEqualTo("jinhyeok lee")
        assertThat(result.response!!.student.gradeClassNumber).isEqualTo("3417")
    }
}