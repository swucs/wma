package com.sycoldstorage.wms.adapter.presentation.web.storageFee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sycoldstorage.wms.TestUtil;
import com.sycoldstorage.wms.annotation.EnableMockMvc;
import com.sycoldstorage.wms.domain.storageFee.StorageFeeRepository;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
class StorageFeeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    StorageFeeRepository storageFeeRepository;

    @Autowired
    Environment env;

    @Test
    @DisplayName("보관료목록")
    void search() throws Exception {

        final String prefix = "_embedded.storageFeeDtoList[].";

        this.mockMvc.perform(MockMvcRequestBuilders.get("/storageFees")
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.createBearerToken(env))
                        .param("name", "군납")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.storageFeeDtoList[0]._links.self.href").exists())
                .andExpect(jsonPath("_links.self.href").exists())
                .andExpect(jsonPath("_links.profile.href").exists())
                //rest docs
                .andDo(document("storageFee-list"
                                , links(
                                        linkWithRel("self").description("link to self")
                                        , linkWithRel("profile").description("프로필로 이동하는 링크")
                                )
                                , requestHeaders(
                                        headerWithName(HttpHeaders.ACCEPT).description("accept header 명시")
                                        , headerWithName(HttpHeaders.CONTENT_TYPE).description("content type 명시")
                                )
                                , requestParameters(
                                        parameterWithName("name").description("검색하려는 보관료명")
                                )
                                , responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type : Hal Json Type")
                                )
                                , responseFields(
                                        fieldWithPath(prefix + "id").type(JsonFieldType.NUMBER).description("보관료ID")
                                        , fieldWithPath(prefix + "baseDate").type(JsonFieldType.STRING).description("기준일자")
                                        , fieldWithPath(prefix + "name").type(JsonFieldType.STRING).description("보관료명")
                                        , fieldWithPath(prefix + "storage").type(JsonFieldType.NUMBER).description("보관료")
                                        , fieldWithPath(prefix + "loading").type(JsonFieldType.NUMBER).description("상하차비")
                                        , fieldWithPath(prefix + "_links.self.href").description("해당 데이터의 상세정보 링크")
                                        , fieldWithPath("_links.self.href").description("현재 화면 링크정보")
                                        , fieldWithPath("_links.profile.href").description("현재 화면 링크정보")
                                )
                        )
                )
        ;
    }

    @Test
    @DisplayName("보관료 생성")
    @Transactional
    @Rollback
    void create() throws Exception {

        StorageFeeDto createRequest = StorageFeeDto.builder()
                .name("군납기본3")
                .baseDate(LocalDate.now())
                .storage(1.8)
                .loading(50d)
                .build();


        this.mockMvc.perform(MockMvcRequestBuilders.post("/storageFee")
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.createBearerToken(env))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(createRequest))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name").value(createRequest.getName()))
                .andExpect(jsonPath("baseDate").value(createRequest.getBaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("storage").value(createRequest.getStorage()))
                .andExpect(jsonPath("loading").value(createRequest.getLoading()))
                //rest docs
                .andDo(document("storageFee-create"
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
                                        fieldWithPath("id").description("보관료ID")
                                        , fieldWithPath("name").description("보관료명")
                                        , fieldWithPath("baseDate").description("기준일자")
                                        , fieldWithPath("storage").description("보관료")
                                        , fieldWithPath("loading").description("상하차비")
                                )
                                , responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type : Hal Json Type")
                                )

                                , responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("보관료ID")
                                        , fieldWithPath("name").type(JsonFieldType.STRING).description("보관료명")
                                        , fieldWithPath("baseDate").type(JsonFieldType.STRING).description("기준일자")
                                        , fieldWithPath("storage").type(JsonFieldType.NUMBER).description("부관료")
                                        , fieldWithPath("loading").type(JsonFieldType.NUMBER).description("상하차비")
                                        , fieldWithPath("_links.self.href").description("생성된 데이터의 상세정보 조회 링크")
                                        , fieldWithPath("_links.list.href").description("리스트 조회 링크정보")
                                        , fieldWithPath("_links.profile.href").description("보관료 등록 profile 링크정보")
                                )
                        )
                )
        ;


    }

    @Test
    @DisplayName("보관료생성_입력값에러")
    void createBadRequestWrongInput() throws Exception {

        StorageFeeDto createRequest = StorageFeeDto.builder()
                .name("asdfjkalsdfjl;asdjfl;askjdflaksjdflkajsdkfluqerwiouqwopruqioewruoiwejfaklsfhajkdgvczvnzxmcvnzxmhjkadfhjdaklhfkasljdhflaskdjfhaslkdjfhaskldjrhioeuryqoiwuerhaskjfhas;ldkfjals;dkfjl;asdruqoieruioqwperujaklsdjfals;dkjfla;sdkjfl;asdkjfl;askdjfl;askdjfl;asd")
                .baseDate(null)
                .storage(0d)
                .loading(null)
                .build();


        this.mockMvc.perform(MockMvcRequestBuilders.post("/storageFee")
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
//                .andExpect(jsonPath("errors[0].rejectedValue").exists())
                .andExpect(jsonPath("_links.list").exists())   //에러인 경우에 index에 대한 link를 기대한다.
        ;

    }

    @Test
    @DisplayName("보관료 수정")
    void update() throws Exception {

        StorageFeeDto storageFeeDto = storageFeeRepository.findById(1l).get().toStorageFeeDto();
        storageFeeDto.setName("보관료명 수정");
        storageFeeDto.setBaseDate(LocalDate.now());
        storageFeeDto.setStorage(30.5);
        storageFeeDto.setLoading(100d);


        this.mockMvc.perform(MockMvcRequestBuilders.put("/storageFee/{id}", storageFeeDto.getId())
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.createBearerToken(env))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(storageFeeDto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(storageFeeDto.getName()))
                .andExpect(jsonPath("baseDate").value(storageFeeDto.getBaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("storage").value(storageFeeDto.getStorage()))
                .andExpect(jsonPath("loading").value(storageFeeDto.getLoading()))
                //rest docs
                .andDo(document("storageFee-update"
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
                                        fieldWithPath("id").description("보관료ID")
                                        , fieldWithPath("name").description("보관료명")
                                        , fieldWithPath("baseDate").description("기준일자")
                                        , fieldWithPath("storage").description("보관료")
                                        , fieldWithPath("loading").description("상하차비")
                                )
                                , responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type : Hal Json Type")
                                )

                                , responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("보관료ID")
                                        , fieldWithPath("name").type(JsonFieldType.STRING).description("보관료명")
                                        , fieldWithPath("baseDate").type(JsonFieldType.STRING).description("기준일자")
                                        , fieldWithPath("storage").type(JsonFieldType.NUMBER).description("부관료")
                                        , fieldWithPath("loading").type(JsonFieldType.NUMBER).description("상하차비")
                                        , fieldWithPath("_links.self.href").description("생성된 데이터의 상세정보 조회 링크")
                                        , fieldWithPath("_links.list.href").description("리스트 조회 링크정보")
                                        , fieldWithPath("_links.profile.href").description("보관료 등록 profile 링크정보")
                                )
                        )
                )
        ;

    }

}