package com.sycoldstorage.wms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sycoldstorage.wms.annotation.EnableMockMvc;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@EnableMockMvc  //응답내용 Encoding을 위한
@ActiveProfiles("dev") //application.properties 파일을 공용으로 사용하고 application-test.properties에서 Override한다.
@AutoConfigureRestDocs
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("거래처목록")
    void getCustomers() throws Exception {

        final String prefix = "_embedded.customerDtoList[].";

        this.mockMvc.perform(MockMvcRequestBuilders.get("/customer/api/customers")
                                .param("name", "")
                                .param("id", "")
                                .param("useYn", "Y")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.customerDtoList[0]._links.self.href").exists())
                .andExpect(jsonPath("_links.self.href").exists())
                .andExpect(jsonPath("_links.profile.href").exists())
                //rest docs
                .andDo(document("customers-list"
                                , links(
                                        linkWithRel("self").description("link to self")
                                        , linkWithRel("profile").description("프로필로 이동하는 링크")
                                )
                                , requestHeaders(
                                        headerWithName(HttpHeaders.ACCEPT).description("accept header 명시")
                                        , headerWithName(HttpHeaders.CONTENT_TYPE).description("content type 명시")
                                )
                                , requestParameters(
                                        parameterWithName("name").description("검색하려는 업체명(like 검색)")
                                        , parameterWithName("id").description("검색하려는 업체ID")
                                        , parameterWithName("useYn").description("검색하려는 사용여부")
                                )

                                , responseHeaders(
//                                headerWithName(HttpHeaders.LOCATION).description("location header")
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type : Hal Json Type")
                                )

                                , relaxedResponseFields(
                                        fieldWithPath(prefix + "id").type(JsonFieldType.NUMBER).description("업체ID")
                                        , fieldWithPath(prefix + "name").type(JsonFieldType.STRING).description("업체명")
                                        , fieldWithPath(prefix + "businessNumber").type(JsonFieldType.STRING).description("사업자등록번호")
                                        , fieldWithPath(prefix + "representativeName").type(JsonFieldType.STRING).description("대표자명")
                                        , fieldWithPath(prefix + "businessConditions").type(JsonFieldType.STRING).description("업태")
                                        , fieldWithPath(prefix + "typeOfBusiness").type(JsonFieldType.STRING).description("업종")
                                        , fieldWithPath(prefix + "address").type(JsonFieldType.STRING).description("주소").optional()
                                        , fieldWithPath(prefix + "phoneNumber").type(JsonFieldType.STRING).description("전화번호").optional()
                                        , fieldWithPath(prefix + "faxNumber").type(JsonFieldType.STRING).description("fax 번호").optional()
                                        , fieldWithPath(prefix + "use").type(JsonFieldType.BOOLEAN).description("사용여부")
                                        , fieldWithPath(prefix + "_links.self.href").description("해당 데이터의 상세정보 링크")
                                        , fieldWithPath("_links.self.href").description("현재 화면 링크정보")
                                        , fieldWithPath("_links.profile.href").description("현재 화면 링크정보")
                                )
                        )
                )

        ;
    }

}