package com.sycoldstorage.wms.domain.warehousing.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sycoldstorage.wms.adapter.presentation.web.warehousing.SearchWarehousingCondition;
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

    @Override
    public List<WarehousingDto> searchWarehousings(SearchWarehousingCondition condition) {

        return queryFactory
                .select(Projections.constructor(WarehousingDto.class
                        , warehousing.id
                        , warehousing.baseDate
                        , warehousing.customer.id
                        , warehousing.customer.name
                        , warehousing.name
                        , new CaseBuilder().when(warehousing.warehousingType.eq(WarehousingType.INCOMING)).then("입고").otherwise("출고")
                        , warehousing.warehousingType
                        , new CaseBuilder().when(warehousing.quickFrozen.eq(true)).then("Y").otherwise("N")
                ))
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
}
