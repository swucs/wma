package com.sycoldstorage.wms.adapter.presentation.web.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sycoldstorage.wms.TestUtil;
import com.sycoldstorage.wms.annotation.EnableMockMvc;
import com.sycoldstorage.wms.domain.item.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;

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
class ItemControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("품목목록")
    void getItems() throws Exception {

        final String prefix = "_embedded.itemDtoList[].";

        this.mockMvc.perform(MockMvcRequestBuilders.get("/items")
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.BEARER_TOKEN)
                        .param("name", "")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.itemDtoList[0]._links.self.href").exists())
                .andExpect(jsonPath("_links.self.href").exists())
                .andExpect(jsonPath("_links.profile.href").exists())
                //rest docs
                .andDo(document("items-list"
                                , links(
                                        linkWithRel("self").description("link to self")
                                        , linkWithRel("profile").description("프로필로 이동하는 링크")
                                )
                                , requestHeaders(
                                        headerWithName(HttpHeaders.ACCEPT).description("accept header 명시")
                                        , headerWithName(HttpHeaders.CONTENT_TYPE).description("content type 명시")
                                )
                                , requestParameters(
                                        parameterWithName("name").description("검색하려는 품목명(like 검색)")
                                )

                                , responseHeaders(
//                                headerWithName(HttpHeaders.LOCATION).description("location header")
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type : Hal Json Type")
                                )

                                , responseFields(
                                        fieldWithPath(prefix + "id").type(JsonFieldType.NUMBER).description("품목ID")
                                        , fieldWithPath(prefix + "name").type(JsonFieldType.STRING).description("품목명")
                                        , fieldWithPath(prefix + "unitName").type(JsonFieldType.STRING).description("단위명")
                                        , fieldWithPath(prefix + "unitWeight").type(JsonFieldType.NUMBER).description("단위무게")
                                        , fieldWithPath(prefix + "remarks").type(JsonFieldType.STRING).description("비고").optional()
                                        , fieldWithPath(prefix + "registeredDate").type(JsonFieldType.STRING).description("최초등록일자")
                                        , fieldWithPath(prefix + "_links.self.href").description("해당 데이터의 상세정보 링크")
                                        , fieldWithPath("_links.self.href").description("현재 화면 링크정보")
                                        , fieldWithPath("_links.profile.href").description("현재 화면 링크정보")
                                )
                        )
                )
        ;
    }


    @Test
    @DisplayName("품목정보 생성")
    @Transactional
    @Rollback
    void createItem() throws Exception {

        ItemDto createRequest = ItemDto.builder()
                .name("신규 품목")
                .unitName("상자")
                .unitWeight(15.0)
                .remarks("비고")
                .build();


        this.mockMvc.perform(MockMvcRequestBuilders.post("/item")
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(createRequest))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name").value(createRequest.getName()))
                .andExpect(jsonPath("unitName").value(createRequest.getUnitName()))
                .andExpect(jsonPath("unitWeight").value(createRequest.getUnitWeight()))
                .andExpect(jsonPath("remarks").value(createRequest.getRemarks()))
                //rest docs
                .andDo(document("item-create"
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
                                        fieldWithPath("id").description("품목ID")
                                        , fieldWithPath("name").description("품목명")
                                        , fieldWithPath("unitName").description("단위명")
                                        , fieldWithPath("unitWeight").description("단위무게")
                                        , fieldWithPath("remarks").description("비고").optional()
                                        , fieldWithPath("registeredDate").description("최초등록일자")
                                )
                                , responseHeaders(
//                                headerWithName(HttpHeaders.LOCATION).description("location header")
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type : Hal Json Type")
                                )

                                , responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("품목ID")
                                        , fieldWithPath("name").type(JsonFieldType.STRING).description("품목명")
                                        , fieldWithPath("unitName").type(JsonFieldType.STRING).description("단위명")
                                        , fieldWithPath("unitWeight").type(JsonFieldType.NUMBER).description("단위무게")
                                        , fieldWithPath("remarks").type(JsonFieldType.STRING).description("비고").optional()
                                        , fieldWithPath("registeredDate").type(JsonFieldType.STRING).description("최초등록일자")
                                        , fieldWithPath("_links.self.href").description("생성된 데이터의 상세정보 조회 링크")
                                        , fieldWithPath("_links.list.href").description("리스트 조회 링크정보")
                                        , fieldWithPath("_links.profile.href").description("품목 등록 profile 링크정보")
                                )
                        )
                )
        ;


    }

    @Test
    @DisplayName("품목정보생성_입력값에러")
    void createEventBadRequestWrongInput() throws Exception {

        ItemDto createRequest = ItemDto.builder()
                .name("신규 품목")
                .unitWeight(255.9)
                .unitName("asdfjkalsdfjl;asdjfl;askjdflaksjdflkajsdkfluqerwiouqwopruqioewruoiwejfaklsfhajkdgvczvnzxmcvnzxmhjkadfhjdaklhfkasljdhflaskdjfhaslkdjfhaskldjrhioeuryqoiwuerhaskjfhas;ldkfjals;dkfjl;asdruqoieruioqwperujaklsdjfals;dkjfla;sdkjfl;asdkjfl;askdjfl;askdjfl;asd")
                .remarks("123123123123123123761823sahfaksjdfhlkasjdfh lasjfsalfhksajdhfklasdhfklashjfdklsajhfksajhfkashdfjkashdfkasjhdfkljsahdflkjahslkwqeruiyworeiuyqweriadsjkfahsklfam,vnzx.,cvnzxm,cvhjkalsdhfkjlasyhiouqyreiouhfjklahsfkjhasdjkflhaskdlfhjasdlfkjashdfluyqrwiouyqweroiuyqweiruyqewi")
                .build();


        this.mockMvc.perform(MockMvcRequestBuilders.post("/item")
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.BEARER_TOKEN)
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
    @DisplayName("품목정보 수정")
    void updateItem() throws Exception {

        ItemDto itemDto = itemRepository.findById(1l).get().toItemDto();
        itemDto.setName("원래 이름");
        itemDto.setUnitName("수정단위명");
        itemDto.setUnitWeight(100.0);
        itemDto.setRemarks("수정된 비고");


        this.mockMvc.perform(MockMvcRequestBuilders.put("/item/{id}", itemDto.getId())
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(itemDto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(itemDto.getName()))
                .andExpect(jsonPath("unitName").value(itemDto.getUnitName()))
                .andExpect(jsonPath("unitWeight").value(itemDto.getUnitWeight()))
                .andExpect(jsonPath("remarks").value(itemDto.getRemarks()))
                //rest docs
                .andDo(document("item-update"
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
                                        fieldWithPath("id").description("품목ID")
                                        , fieldWithPath("name").description("품목명")
                                        , fieldWithPath("unitName").description("단위명")
                                        , fieldWithPath("unitWeight").description("단위무게")
                                        , fieldWithPath("remarks").description("비고").optional()
                                        , fieldWithPath("registeredDate").description("최초등록일자")
                                )
                                , responseHeaders(
//                                headerWithName(HttpHeaders.LOCATION).description("location header")
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type : Hal Json Type")
                                )

                                , responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("품목ID")
                                        , fieldWithPath("name").type(JsonFieldType.STRING).description("품목명")
                                        , fieldWithPath("unitName").type(JsonFieldType.STRING).description("단위명")
                                        , fieldWithPath("unitWeight").type(JsonFieldType.NUMBER).description("단위무게")
                                        , fieldWithPath("remarks").type(JsonFieldType.STRING).description("비고").optional()
                                        , fieldWithPath("registeredDate").type(JsonFieldType.STRING).description("최초등록일자")
                                        , fieldWithPath("_links.self.href").description("생성된 데이터의 상세정보 조회 링크")
                                        , fieldWithPath("_links.list.href").description("리스트 조회 링크정보")
                                        , fieldWithPath("_links.profile.href").description("품목 등록 profile 링크정보")
                                )
                        )
                )
        ;

    }
}