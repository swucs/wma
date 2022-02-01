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
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;

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
@EnableMockMvc  //응답내용 Encoding을 위한
@ActiveProfiles("dev") //application.properties 파일을 공용으로 사용하고 application-test.properties에서 Override한다.
@AutoConfigureRestDocs
@Transactional
class WarehousingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WarehousingRepository warehousingRepository;

    @Test
    @DisplayName("입출고목록")
    void searchWarehousings() throws Exception {

        final String prefix = "_embedded.warehousingDtoList[].";

        this.mockMvc.perform(MockMvcRequestBuilders.get("/warehousings")
                                .header(HttpHeaders.AUTHORIZATION, TestUtil.BEARER_TOKEN)
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
                .andDo(document("warehousings-list"
                                , links(
                                        linkWithRel("self").description("link to self")
                                        , linkWithRel("profile").description("프로필로 이동하는 링크")
                                )
                                , requestHeaders(
                                        headerWithName(HttpHeaders.ACCEPT).description("accept header 명시")
                                        , headerWithName(HttpHeaders.CONTENT_TYPE).description("content type 명시")
                                )
                                , requestParameters(
                                        parameterWithName("baseDateFrom").description("검색하려는 기준일자 시작일")
                                        , parameterWithName("baseDateTo").description("검색하려는 기준일자 종료일")
                                        , parameterWithName("warehousingTypeValue").description("검색하려는 입출고구분(입고/출고)")
                                        , parameterWithName("customerName").description("검색하려는 고객명")
                                        , parameterWithName("itemName").description("검색하려는 품목명")
                                )

                                , responseHeaders(
//                                headerWithName(HttpHeaders.LOCATION).description("location header")
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type : Hal Json Type")
                                )

                                , responseFields(
                                        fieldWithPath(prefix + "id").type(JsonFieldType.NUMBER).description("업체ID")
                                        , fieldWithPath(prefix + "baseDate").type(JsonFieldType.STRING).description("기준일자")
                                        , fieldWithPath(prefix + "customerId").type(JsonFieldType.NUMBER).description("거래처ID")
                                        , fieldWithPath(prefix + "customerName").type(JsonFieldType.STRING).description("거래처명")
                                        , fieldWithPath(prefix + "name").type(JsonFieldType.STRING).description("입고인")
                                        , fieldWithPath(prefix + "warehousingTypeName").type(JsonFieldType.STRING).description("입출고구분명")
                                        , fieldWithPath(prefix + "warehousingTypeValue").type(JsonFieldType.STRING).description("입출고구분")
                                        , fieldWithPath(prefix + "quickFrozenYn").type(JsonFieldType.STRING).description("동결입고여부").optional()
                                        , fieldWithPath(prefix + "_links.self.href").description("해당 데이터의 상세정보 링크")
                                        , fieldWithPath("_links.self.href").description("현재 화면 링크정보")
                                        , fieldWithPath("_links.profile.href").description("현재 화면 링크정보")
                                )
                        )
                )
        ;
    }

    @Test
    @DisplayName("입출고상세목록")
    void warehousingDetails() throws Exception {

        SearchWarehousingCondition condition = new SearchWarehousingCondition();
        condition.setBaseDateFrom(LocalDate.of(2022, 1, 24));
        condition.setBaseDateTo(LocalDate.of(2022, 1, 29));
        condition.setCustomerName("맛죤식품");
        condition.setItemName("돼지갈비양념");
        List<WarehousingDto> warehousingDtos = warehousingRepository.searchWarehousings(condition);

        Long id = warehousingDtos.get(0).getId();

        final String prefix = "_embedded.warehousingDetailDtoList[].";

        //PathVariable을 위한 pathParameters를 사용하기 위해서는 RestDocumentationRequestBuilders를 사용해야 함.
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/warehousing/{id}/details", id)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.BEARER_TOKEN)
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
                                        , linkWithRel("profile").description("프로필로 이동하는 링크")
                                )
                                , requestHeaders(
                                        headerWithName(HttpHeaders.ACCEPT).description("accept header 명시")
                                        , headerWithName(HttpHeaders.CONTENT_TYPE).description("content type 명시")
                                )
                                , pathParameters(
                                        parameterWithName("id").description("입출고ID")
                                )

                                , responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type : Hal Json Type")
                                )

                                , responseFields(
                                        fieldWithPath(prefix + "id").type(JsonFieldType.NUMBER).description("입출고상세ID")
                                        , fieldWithPath(prefix + "itemId").type(JsonFieldType.NUMBER).description("품목ID")
                                        , fieldWithPath(prefix + "itemName").type(JsonFieldType.STRING).description("품목명")
                                        , fieldWithPath(prefix + "itemUnitWeight").type(JsonFieldType.NUMBER).description("단위무게")
                                        , fieldWithPath(prefix + "itemUnitName").type(JsonFieldType.STRING).description("단위명")
                                        , fieldWithPath(prefix + "count").type(JsonFieldType.NUMBER).description("개수")
                                        , fieldWithPath(prefix + "remainingWeight").type(JsonFieldType.NUMBER).description("잔량")
                                        , fieldWithPath(prefix + "totalWeight").type(JsonFieldType.NUMBER).description("총중량")
                                        , fieldWithPath(prefix + "remarks").type(JsonFieldType.STRING).description("비고").optional()
                                        , fieldWithPath(prefix + "calculationYn").type(JsonFieldType.STRING).description("계산여부")
                                        , fieldWithPath("_links.self.href").description("현재 화면 링크정보")
                                        , fieldWithPath("_links.profile.href").description("현재 화면 링크정보")
                                )
                        )
                )
        ;
    }

}