package com.sycoldstorage.wms.adapter.presentation.web.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sycoldstorage.wms.TestUtil;
import com.sycoldstorage.wms.annotation.EnableMockMvc;
import com.sycoldstorage.wms.domain.customer.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@EnableMockMvc  //응답내용 Encoding을 위한
@ActiveProfiles("dev") //application.properties 파일을 공용으로 사용하고 application-test.properties에서 Override한다.
@AutoConfigureRestDocs
@Transactional
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    Environment env;

    @Test
    @DisplayName("거래처목록")
    void getCustomers() throws Exception {

        final String prefix = "_embedded.customerDtoList[].";

        this.mockMvc.perform(MockMvcRequestBuilders.get("/customers")
                                .header(HttpHeaders.AUTHORIZATION, TestUtil.createBearerToken(env))
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
                .andDo(document("customer-list"
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

                                , responseFields(
                                        fieldWithPath(prefix + "id").type(JsonFieldType.NUMBER).description("업체ID")
                                        , fieldWithPath(prefix + "name").type(JsonFieldType.STRING).description("업체명")
                                        , fieldWithPath(prefix + "businessNumber").type(JsonFieldType.STRING).description("사업자등록번호")
                                        , fieldWithPath(prefix + "representativeName").type(JsonFieldType.STRING).description("대표자명")
                                        , fieldWithPath(prefix + "businessConditions").type(JsonFieldType.STRING).description("업태")
                                        , fieldWithPath(prefix + "typeOfBusiness").type(JsonFieldType.STRING).description("업종")
                                        , fieldWithPath(prefix + "address").type(JsonFieldType.STRING).description("주소").optional()
                                        , fieldWithPath(prefix + "phoneNumber").type(JsonFieldType.STRING).description("전화번호").optional()
                                        , fieldWithPath(prefix + "faxNumber").type(JsonFieldType.STRING).description("fax 번호").optional()
                                        , fieldWithPath(prefix + "useYn").type(JsonFieldType.STRING).description("사용여부")
                                        , fieldWithPath(prefix + "_links.self.href").description("해당 데이터의 상세정보 링크")
                                        , fieldWithPath("_links.self.href").description("현재 화면 링크정보")
                                        , fieldWithPath("_links.profile.href").description("현재 화면 링크정보")
                                )
                        )
                )
        ;
    }


    @Test
    @DisplayName("거래처정보 생성")
    @Transactional
    @Rollback
    void createCustomer() throws Exception {

        CustomerDto createRequest = CustomerDto.builder()
                .name("신규 거래처")
                .address("주소")
                .businessConditions("test")
                .faxNumber("02-1111-1111")
                .phoneNumber("010-1234-1234")
                .representativeName("대표자명")
                .businessNumber("000-00-12345")
                .useYn("Y")
                .typeOfBusiness("test")
                .build();


        this.mockMvc.perform(MockMvcRequestBuilders.post("/customer")
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.createBearerToken(env))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(createRequest))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name").value(createRequest.getName()))
                .andExpect(jsonPath("address").value(createRequest.getAddress()))
                .andExpect(jsonPath("businessConditions").value(createRequest.getBusinessConditions()))
                .andExpect(jsonPath("faxNumber").value(createRequest.getFaxNumber()))
                .andExpect(jsonPath("phoneNumber").value(createRequest.getPhoneNumber()))
                //rest docs
                .andDo(document("customer-create"
                                , links(
                                        linkWithRel("self").description("link to self")
                                        , linkWithRel("list").description("리스트 조회 링크")
                                        , linkWithRel("profile").description("프로필로 이동하는 링크")
                                )
                                , requestHeaders(
                                        headerWithName(HttpHeaders.ACCEPT).description("accept header 명시")
                                        , headerWithName(HttpHeaders.CONTENT_TYPE).description("content type 명시")
                                )
                                , requestFields(
                                        fieldWithPath("id").description("업체ID")
                                        , fieldWithPath("name").description("업체명")
                                        , fieldWithPath("businessNumber").description("사업자등록번호")
                                        , fieldWithPath("representativeName").description("대표자명")
                                        , fieldWithPath("typeOfBusiness").description("업종")
                                        , fieldWithPath("address").description("주소")
                                        , fieldWithPath("phoneNumber").description("전화번호")
                                        , fieldWithPath("faxNumber").description("fax번호")
                                        , fieldWithPath("useYn").description("사용여부")
                                        , fieldWithPath("businessConditions").description("업태")
                                )
                                , responseHeaders(
//                                headerWithName(HttpHeaders.LOCATION).description("location header")
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type : Hal Json Type")
                                )

                                , responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("업체ID")
                                        , fieldWithPath("name").type(JsonFieldType.STRING).description("업체명")
                                        , fieldWithPath("businessNumber").type(JsonFieldType.STRING).description("사업자등록번호")
                                        , fieldWithPath("representativeName").type(JsonFieldType.STRING).description("대표자명")
                                        , fieldWithPath("businessConditions").type(JsonFieldType.STRING).description("업태")
                                        , fieldWithPath("typeOfBusiness").type(JsonFieldType.STRING).description("업종")
                                        , fieldWithPath("address").type(JsonFieldType.STRING).description("주소").optional()
                                        , fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("전화번호").optional()
                                        , fieldWithPath("faxNumber").type(JsonFieldType.STRING).description("fax 번호").optional()
                                        , fieldWithPath("useYn").type(JsonFieldType.STRING).description("사용여부")
                                        , fieldWithPath("_links.self.href").description("생성된 데이터의 상세정보 조회 링크")
                                        , fieldWithPath("_links.list.href").description("리스트 조회 링크정보")
                                        , fieldWithPath("_links.profile.href").description("거래처 등록 profile 링크정보")
                                )
                        )
                )
        ;


    }

    @Test
    @DisplayName("거래처정보생성_입력값에러")
    void createEventBadRequestWrongInput() throws Exception {

        CustomerDto createRequest = CustomerDto.builder()
                .name("신규 거래처")
                .address("주소")
                .businessConditions("testtesttesttesttesttesttest")
                .faxNumber("02-732-1381")
                .phoneNumber("000-0000-0000")
                .representativeName("대표자명대표자명대표자명대표자명대표자명대표자명대표자명")
                .businessNumber("123-45-67890")
                .useYn("L")
                .typeOfBusiness("test")
                .build();


        this.mockMvc.perform(MockMvcRequestBuilders.post("/customer")
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.createBearerToken(env))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(createRequest))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].objectName").exists())
                .andExpect(jsonPath("errors[0].field").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("errors[0].code").exists())
                .andExpect(jsonPath("errors[0].rejectedValue").exists())
                .andExpect(jsonPath("_links.list").exists())   //에러인 경우에 index에 대한 link를 기대한다.
        ;

    }

    @Test
    @DisplayName("거래처정보 수정")
    void updateCustomer() throws Exception {

        CustomerDto customerDto = customerRepository.findById(1l).get().toCustomerDto();

        customerDto.setAddress("원래 주소");
        customerDto.setName("원래 이름");


        this.mockMvc.perform(MockMvcRequestBuilders.put("/customer/{id}", customerDto.getId())
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.createBearerToken(env))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(customerDto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("address").value(customerDto.getAddress()))
                .andExpect(jsonPath("name").value(customerDto.getName()))
                //rest docs
                .andDo(document("customer-update"
                                , links(
                                        linkWithRel("self").description("link to self")
                                        , linkWithRel("list").description("리스트 조회 링크")
                                        , linkWithRel("profile").description("프로필로 이동하는 링크")
                                )
                                , requestHeaders(
                                        headerWithName(HttpHeaders.ACCEPT).description("accept header 명시")
                                        , headerWithName(HttpHeaders.CONTENT_TYPE).description("content type 명시")
                                )
                                , requestFields(
                                        fieldWithPath("id").description("업체ID")
                                        , fieldWithPath("name").description("업체명")
                                        , fieldWithPath("businessNumber").description("사업자등록번호")
                                        , fieldWithPath("representativeName").description("대표자명")
                                        , fieldWithPath("typeOfBusiness").description("업종")
                                        , fieldWithPath("address").description("주소")
                                        , fieldWithPath("phoneNumber").description("전화번호")
                                        , fieldWithPath("faxNumber").description("fax번호")
                                        , fieldWithPath("useYn").description("사용여부")
                                        , fieldWithPath("businessConditions").description("업태")
                                )
                                , responseHeaders(
//                                headerWithName(HttpHeaders.LOCATION).description("location header")
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type : Hal Json Type")
                                )

                                , responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("업체ID")
                                        , fieldWithPath("name").type(JsonFieldType.STRING).description("업체명")
                                        , fieldWithPath("businessNumber").type(JsonFieldType.STRING).description("사업자등록번호")
                                        , fieldWithPath("representativeName").type(JsonFieldType.STRING).description("대표자명")
                                        , fieldWithPath("businessConditions").type(JsonFieldType.STRING).description("업태")
                                        , fieldWithPath("typeOfBusiness").type(JsonFieldType.STRING).description("업종")
                                        , fieldWithPath("address").type(JsonFieldType.STRING).description("주소").optional()
                                        , fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("전화번호").optional()
                                        , fieldWithPath("faxNumber").type(JsonFieldType.STRING).description("fax 번호").optional()
                                        , fieldWithPath("useYn").type(JsonFieldType.STRING).description("사용여부")
                                        , fieldWithPath("_links.self.href").description("생성된 데이터의 상세정보 조회 링크")
                                        , fieldWithPath("_links.list.href").description("리스트 조회 링크정보")
                                        , fieldWithPath("_links.profile.href").description("거래처 등록 profile 링크정보")
                                )
                        )
                )
        ;

    }

}