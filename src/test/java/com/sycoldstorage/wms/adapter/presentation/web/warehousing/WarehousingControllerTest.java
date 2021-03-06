package com.sycoldstorage.wms.adapter.presentation.web.warehousing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sycoldstorage.wms.TestUtil;
import com.sycoldstorage.wms.annotation.EnableMockMvc;
import com.sycoldstorage.wms.domain.warehousing.WarehousingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@EnableMockMvc  //???????????? Encoding??? ??????
@ActiveProfiles("dev") //application.properties ????????? ???????????? ???????????? application-test.properties?????? Override??????.
@AutoConfigureRestDocs
@Transactional
class WarehousingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WarehousingRepository warehousingRepository;

    @Autowired
    Environment env;

    @Test
    @DisplayName("???????????????")
    void searchWarehousings() throws Exception {

        final String prefix = "_embedded.warehousingDtoList[].";

        this.mockMvc.perform(MockMvcRequestBuilders.get("/warehousings")
                                .header(HttpHeaders.AUTHORIZATION, TestUtil.createBearerToken(env))
                                .param("baseDateFrom", "2022-01-25")
                                .param("baseDateTo", "2022-02-01")
                                .param("warehousingTypeValue", "INCOMING")
                                .param("customerName", "")
                                .param("itemName", "")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.warehousingDtoList[0]._links.self.href").exists())
                .andExpect(jsonPath("_links.self.href").exists())
                .andExpect(jsonPath("_links.profile.href").exists())
                //rest docs
                .andDo(document("warehousing-list"
                                , links(
                                        linkWithRel("self").description("link to self")
                                        , linkWithRel("profile").description("???????????? ???????????? ??????")
                                )
                                , requestHeaders(
                                        headerWithName(HttpHeaders.ACCEPT).description("accept header ??????")
                                        , headerWithName(HttpHeaders.CONTENT_TYPE).description("content type ??????")
                                )
                                , requestParameters(
                                        parameterWithName("baseDateFrom").description("??????????????? ???????????? ?????????")
                                        , parameterWithName("baseDateTo").description("??????????????? ???????????? ?????????")
                                        , parameterWithName("warehousingTypeValue").description("??????????????? ???????????????(??????/??????)")
                                        , parameterWithName("customerName").description("??????????????? ?????????")
                                        , parameterWithName("itemName").description("??????????????? ?????????")
                                )

                                , responseHeaders(
//                                headerWithName(HttpHeaders.LOCATION).description("location header")
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type : Hal Json Type")
                                )

                                , responseFields(
                                        fieldWithPath(prefix + "id").type(JsonFieldType.NUMBER).description("??????ID")
                                        , fieldWithPath(prefix + "baseDate").type(JsonFieldType.STRING).description("????????????")
                                        , fieldWithPath(prefix + "customerId").type(JsonFieldType.NUMBER).description("?????????ID")
                                        , fieldWithPath(prefix + "customerName").type(JsonFieldType.STRING).description("????????????")
                                        , fieldWithPath(prefix + "name").type(JsonFieldType.STRING).description("?????????")
                                        , fieldWithPath(prefix + "warehousingTypeName").type(JsonFieldType.STRING).description("??????????????????")
                                        , fieldWithPath(prefix + "warehousingTypeValue").type(JsonFieldType.STRING).description("???????????????")
                                        , fieldWithPath(prefix + "quickFrozenYn").type(JsonFieldType.STRING).description("??????????????????").optional()
                                        , fieldWithPath(prefix + "_links.self.href").description("?????? ???????????? ???????????? ??????")
                                        , fieldWithPath("_links.self.href").description("?????? ?????? ????????????")
                                        , fieldWithPath("_links.profile.href").description("?????? ?????? ????????????")
                                )
                        )
                )
        ;
    }

    @Test
    @DisplayName("?????????????????????")
    void warehousingDetails() throws Exception {

        SearchWarehousingCondition condition = new SearchWarehousingCondition();
        condition.setBaseDateFrom(LocalDate.of(2022, 1, 24));
        condition.setBaseDateTo(LocalDate.of(2022, 1, 29));
        condition.setCustomerName("????????????");
        condition.setItemName("??????????????????");
        List<WarehousingDto> warehousingDtos = warehousingRepository.searchWarehousings(condition);

        Long id = warehousingDtos.get(0).getId();

        final String prefix = "_embedded.warehousingDetailDtoList[].";

        //PathVariable??? ?????? pathParameters??? ???????????? ???????????? RestDocumentationRequestBuilders??? ???????????? ???.
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/warehousing/{id}/details", id)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.createBearerToken(env))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.self.href").exists())
                .andExpect(jsonPath("_links.profile.href").exists())
                //rest docs
                .andDo(document("warehousing-detail-list"
                                , links(
                                        linkWithRel("self").description("link to self")
                                        , linkWithRel("profile").description("???????????? ???????????? ??????")
                                )
                                , requestHeaders(
                                        headerWithName(HttpHeaders.ACCEPT).description("accept header ??????")
                                        , headerWithName(HttpHeaders.CONTENT_TYPE).description("content type ??????")
                                )
                                , pathParameters(
                                        parameterWithName("id").description("?????????ID")
                                )

                                , responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type : Hal Json Type")
                                )

                                , responseFields(
                                        fieldWithPath(prefix + "id").type(JsonFieldType.NUMBER).description("???????????????ID")
                                        , fieldWithPath(prefix + "itemId").type(JsonFieldType.NUMBER).description("??????ID")
                                        , fieldWithPath(prefix + "itemName").type(JsonFieldType.STRING).description("?????????")
                                        , fieldWithPath(prefix + "itemUnitWeight").type(JsonFieldType.NUMBER).description("????????????")
                                        , fieldWithPath(prefix + "itemUnitName").type(JsonFieldType.STRING).description("?????????")
                                        , fieldWithPath(prefix + "count").type(JsonFieldType.NUMBER).description("??????")
                                        , fieldWithPath(prefix + "remainingWeight").type(JsonFieldType.NUMBER).description("??????")
                                        , fieldWithPath(prefix + "totalWeight").type(JsonFieldType.NUMBER).description("?????????")
                                        , fieldWithPath(prefix + "remarks").type(JsonFieldType.STRING).description("??????").optional()
                                        , fieldWithPath(prefix + "calculationYn").type(JsonFieldType.STRING).description("????????????")
                                        , fieldWithPath("_links.self.href").description("?????? ?????? ????????????")
                                        , fieldWithPath("_links.profile.href").description("?????? ?????? ????????????")
                                )
                        )
                )
        ;
    }

}