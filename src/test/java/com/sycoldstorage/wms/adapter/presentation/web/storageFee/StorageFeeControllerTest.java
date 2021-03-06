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
@EnableMockMvc  //???????????? Encoding??? ??????
@ActiveProfiles("dev") //application.properties ????????? ???????????? ???????????? application-test.properties?????? Override??????.
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
    @DisplayName("???????????????")
    void search() throws Exception {

        final String prefix = "_embedded.storageFeeDtoList[].";

        this.mockMvc.perform(MockMvcRequestBuilders.get("/storageFees")
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.createBearerToken(env))
                        .param("name", "??????")
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
                                        , linkWithRel("profile").description("???????????? ???????????? ??????")
                                )
                                , requestHeaders(
                                        headerWithName(HttpHeaders.ACCEPT).description("accept header ??????")
                                        , headerWithName(HttpHeaders.CONTENT_TYPE).description("content type ??????")
                                )
                                , requestParameters(
                                        parameterWithName("name").description("??????????????? ????????????")
                                )
                                , responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type : Hal Json Type")
                                )
                                , responseFields(
                                        fieldWithPath(prefix + "id").type(JsonFieldType.NUMBER).description("?????????ID")
                                        , fieldWithPath(prefix + "baseDate").type(JsonFieldType.STRING).description("????????????")
                                        , fieldWithPath(prefix + "name").type(JsonFieldType.STRING).description("????????????")
                                        , fieldWithPath(prefix + "storage").type(JsonFieldType.NUMBER).description("?????????")
                                        , fieldWithPath(prefix + "loading").type(JsonFieldType.NUMBER).description("????????????")
                                        , fieldWithPath(prefix + "_links.self.href").description("?????? ???????????? ???????????? ??????")
                                        , fieldWithPath("_links.self.href").description("?????? ?????? ????????????")
                                        , fieldWithPath("_links.profile.href").description("?????? ?????? ????????????")
                                )
                        )
                )
        ;
    }

    @Test
    @DisplayName("????????? ??????")
    @Transactional
    @Rollback
    void create() throws Exception {

        StorageFeeDto createRequest = StorageFeeDto.builder()
                .name("????????????3")
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
                                        , linkWithRel("list").description("????????? ?????? ??????")
                                        , linkWithRel("profile").description("???????????? ???????????? ??????")
                                )
                                , requestHeaders(
                                        headerWithName(HttpHeaders.ACCEPT).description("accept header ??????")
                                        , headerWithName(HttpHeaders.CONTENT_TYPE).description("content type ??????")
                                )
                                , requestFields(
                                        fieldWithPath("id").description("?????????ID")
                                        , fieldWithPath("name").description("????????????")
                                        , fieldWithPath("baseDate").description("????????????")
                                        , fieldWithPath("storage").description("?????????")
                                        , fieldWithPath("loading").description("????????????")
                                )
                                , responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type : Hal Json Type")
                                )

                                , responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????????ID")
                                        , fieldWithPath("name").type(JsonFieldType.STRING).description("????????????")
                                        , fieldWithPath("baseDate").type(JsonFieldType.STRING).description("????????????")
                                        , fieldWithPath("storage").type(JsonFieldType.NUMBER).description("?????????")
                                        , fieldWithPath("loading").type(JsonFieldType.NUMBER).description("????????????")
                                        , fieldWithPath("_links.self.href").description("????????? ???????????? ???????????? ?????? ??????")
                                        , fieldWithPath("_links.list.href").description("????????? ?????? ????????????")
                                        , fieldWithPath("_links.profile.href").description("????????? ?????? profile ????????????")
                                )
                        )
                )
        ;


    }

    @Test
    @DisplayName("???????????????_???????????????")
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
                .andExpect(jsonPath("_links.list").exists())   //????????? ????????? index??? ?????? link??? ????????????.
        ;

    }

    @Test
    @DisplayName("????????? ??????")
    void update() throws Exception {

        StorageFeeDto storageFeeDto = storageFeeRepository.findById(1l).get().toStorageFeeDto();
        storageFeeDto.setName("???????????? ??????");
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
                                        , linkWithRel("list").description("????????? ?????? ??????")
                                        , linkWithRel("profile").description("???????????? ???????????? ??????")
                                )
                                , requestHeaders(
                                        headerWithName(HttpHeaders.ACCEPT).description("accept header ??????")
                                        , headerWithName(HttpHeaders.CONTENT_TYPE).description("content type ??????")
                                )
                                , requestFields(
                                        fieldWithPath("id").description("?????????ID")
                                        , fieldWithPath("name").description("????????????")
                                        , fieldWithPath("baseDate").description("????????????")
                                        , fieldWithPath("storage").description("?????????")
                                        , fieldWithPath("loading").description("????????????")
                                )
                                , responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type : Hal Json Type")
                                )

                                , responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????????ID")
                                        , fieldWithPath("name").type(JsonFieldType.STRING).description("????????????")
                                        , fieldWithPath("baseDate").type(JsonFieldType.STRING).description("????????????")
                                        , fieldWithPath("storage").type(JsonFieldType.NUMBER).description("?????????")
                                        , fieldWithPath("loading").type(JsonFieldType.NUMBER).description("????????????")
                                        , fieldWithPath("_links.self.href").description("????????? ???????????? ???????????? ?????? ??????")
                                        , fieldWithPath("_links.list.href").description("????????? ?????? ????????????")
                                        , fieldWithPath("_links.profile.href").description("????????? ?????? profile ????????????")
                                )
                        )
                )
        ;

    }

}