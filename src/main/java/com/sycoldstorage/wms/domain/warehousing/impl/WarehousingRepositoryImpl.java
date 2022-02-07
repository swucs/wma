package com.sycoldstorage.wms.domain.warehousing.impl;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sycoldstorage.wms.adapter.presentation.web.statistics.CustomerItemTermDto;
import com.sycoldstorage.wms.adapter.presentation.web.statistics.SearchCustomerItemTermCondition;
import com.sycoldstorage.wms.adapter.presentation.web.warehousing.SearchWarehousingCondition;
import com.sycoldstorage.wms.adapter.presentation.web.warehousing.WarehousingDetailDto;
import com.sycoldstorage.wms.adapter.presentation.web.warehousing.WarehousingDto;
import com.sycoldstorage.wms.domain.warehousing.WarehousingCustom;
import com.sycoldstorage.wms.domain.warehousing.WarehousingType;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static com.sycoldstorage.wms.domain.customer.QCustomer.customer;
import static com.sycoldstorage.wms.domain.item.QItem.item;
import static com.sycoldstorage.wms.domain.warehousing.QWarehousing.warehousing;
import static com.sycoldstorage.wms.domain.warehousing.QWarehousingDetail.warehousingDetail;


public class WarehousingRepositoryImpl implements WarehousingCustom {

    private final JPAQueryFactory queryFactory;

    public WarehousingRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 입출고 목록
     * @param condition
     * @return
     */
    @Override
    public List<WarehousingDto> searchWarehousings(SearchWarehousingCondition condition) {

        return queryFactory
                .select(this.selectWarehousingDto())
                .from(warehousing)
                .join(warehousing.customer, customer)
                .where(
                        betweenBaseDate(condition.getBaseDateFrom(), condition.getBaseDateTo())
                        , likeCustomerName(condition.getCustomerName())
                        , likeItemName(condition.getItemName())
                        , equalWarehousingTypeText(condition.getWarehousingType())
                )
                .orderBy(warehousing.baseDate.desc(), warehousing.id.asc())
                .fetch();
    }

    /**
     * WarehousingDto의 Select 절
     * @return
     */
    private ConstructorExpression<WarehousingDto> selectWarehousingDto() {
        return Projections.constructor(WarehousingDto.class
                , warehousing.id
                , warehousing.baseDate
                , warehousing.customer.id
                , warehousing.customer.name
                , warehousing.name
                , new CaseBuilder().when(warehousing.warehousingType.eq(WarehousingType.INCOMING)).then("입고").otherwise("출고")
                , warehousing.warehousingType.stringValue().as("warehousingType")
                , new CaseBuilder().when(warehousing.quickFrozen.eq(true)).then("Y").otherwise("N")
        );
    }


    /**
     *
     * @param baseDateTo
     * @return
     */
    private BooleanExpression betweenBaseDate(LocalDate baseDateFrom, LocalDate baseDateTo) {
        return baseDateFrom != null && baseDateTo != null ? warehousing.baseDate.between(baseDateFrom, baseDateTo) : null;
    }


    /**
     *
     * @param customerName
     * @return
     */
    private BooleanExpression likeCustomerName(String customerName) {
        return StringUtils.isNotEmpty(customerName) ? customer.name.like("%" + customerName + "%") : null;
    }

    /**
     *
     * @param itemName
     * @return
     */
    private BooleanExpression likeItemName(String itemName) {

        if (StringUtils.isEmpty(itemName)) {
            return null;
        }

        return JPAExpressions
                .select(warehousingDetail.item)
                .from(warehousingDetail)
                .join(warehousingDetail.item, item)
                .where(
                        warehousingDetail.warehousing.eq(warehousing)
                        , item.name.like("%" + itemName + "%")
                )
                .exists()
                ;
    }

    /**
     * 입고/출고 검색
     * @param warehousingType
     * @return
     */
    private BooleanExpression equalWarehousingTypeText(WarehousingType warehousingType) {
        return warehousingType != null ? warehousing.warehousingType.eq(warehousingType) : null;
    }


    /**
     * 해당 입출고ID의 입출고정보
     * @param id
     * @return
     */
    @Override
    public WarehousingDto findWarehousingById(long id) {
        return queryFactory
                .select(this.selectWarehousingDto())
                .from(warehousing)
                .join(warehousing.customer, customer)
                .where(warehousing.id.eq(id))
                .fetchOne();
    }


    /**
     * 해당 입출고ID의 입출고 내역 목록
     * @param warehousingId
     * @return
     */
    @Override
    public List<WarehousingDetailDto> findWarehousingDetails(Long warehousingId) {

        return queryFactory
                .select(Projections.constructor(WarehousingDetailDto.class
                        , warehousingDetail.id
                        , warehousingDetail.item.id.as("item_id")
                        , warehousingDetail.item.name.as("item_name")
                        , warehousingDetail.item.unitWeight.as("item_unit_weight")
                        , warehousingDetail.item.unitName.as("item_unit_name")
                        , warehousingDetail.totalWeight.divide(warehousingDetail.item.unitWeight).floor().as("count")
                        , warehousingDetail.totalWeight.mod(warehousingDetail.item.unitWeight).as("remaining_weight")
                        , warehousingDetail.totalWeight
                        , warehousingDetail.remarks
                        , new CaseBuilder().when(warehousingDetail.calculation.eq(true))
                                .then("Y").otherwise("N").as("calculation_yn")
                ))
                .from(warehousingDetail)
                .join(warehousingDetail.item, item)
                .where(warehousingDetail.warehousing.id.eq(warehousingId))
                .orderBy(warehousingDetail.id.asc())
                .fetch();

    }


    /**
     * 고객 품목별 기간별 통계
     * @param condition
     * @return
     */
    @Override
    public List<CustomerItemTermDto> searchCustomerItemTermStatistics(SearchCustomerItemTermCondition condition) {
        return queryFactory
                .select(Projections.constructor(CustomerItemTermDto.class
                        , new CaseBuilder().when(item.remarks.isNotEmpty())
                                .then(item.name.concat("(").concat(item.remarks).concat(")"))
                                .otherwise(item.name.concat("(").concat(item.unitWeight.stringValue()).concat(")"))
                                .as("itemName")

                        , new CaseBuilder()
                                .when(
                                        warehousing.warehousingType.eq(WarehousingType.INCOMING)
                                        .and(warehousing.baseDate.between(condition.getBaseDateFrom(), condition.getBaseDateTo()))
                                )
                                .then(warehousingDetail.totalWeight)
                                .otherwise(0d)
                                .sum()
                                .as("incomingUnitQty")

                        , new CaseBuilder()
                                .when(
                                        warehousing.warehousingType.eq(WarehousingType.INCOMING)
                                        .and(warehousing.baseDate.between(condition.getBaseDateFrom(), condition.getBaseDateTo()))
                                )
                                .then(warehousingDetail.totalWeight)
                                .otherwise(0d)
                                .sum()
                                .as("incomingWeightQty")

                        , new CaseBuilder()
                                .when(
                                        warehousing.warehousingType.eq(WarehousingType.OUTGOING)
                                        .and(warehousing.baseDate.between(condition.getBaseDateFrom(), condition.getBaseDateTo()))
                                )
                                .then(warehousingDetail.totalWeight)
                                .otherwise(0d)
                                .sum()
                                .as("outgoingUnitQty")

                        , new CaseBuilder()
                                .when(
                                        warehousing.warehousingType.eq(WarehousingType.OUTGOING)
                                        .and(warehousing.baseDate.between(condition.getBaseDateFrom(), condition.getBaseDateTo()))
                                )
                                .then(warehousingDetail.totalWeight)
                                .otherwise(0d)
                                .sum()
                                .as("outgoingWeightQty")


                        , new CaseBuilder()
                                .when(warehousing.warehousingType.eq(WarehousingType.INCOMING))
                                .then(warehousingDetail.totalWeight)
                                .otherwise(0d)
                                .sum()
                                .as("incomingStockWeightQty")

                        , new CaseBuilder()
                                .when(warehousing.warehousingType.eq(WarehousingType.OUTGOING))
                                .then(warehousingDetail.totalWeight)
                                .otherwise(0d)
                                .sum()
                                .as("outgoingStockWeightQty")

                        , item.unitName
                        , item.unitWeight
                        , warehousing.baseDate.max().as("recentBaseDate")
                ))
                .from(warehousingDetail)
                .join(warehousingDetail.item, item)
                .join(warehousingDetail.warehousing, warehousing)
                .join(warehousing.customer, customer)
                .on(
                        customer.id.eq(condition.getCustomerId())
                        .and(warehousing.baseDate.loe(condition.getBaseDateTo()))
                )
                .groupBy(item.id, item.name, item.remarks, item.unitWeight, item.unitName)
                .orderBy(item.id.asc())
                .fetch()
                ;

    }


}
