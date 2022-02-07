package com.sycoldstorage.wms.domain.item.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sycoldstorage.wms.adapter.presentation.web.item.ItemDto;
import com.sycoldstorage.wms.adapter.presentation.web.item.ItemSelectBoxDto;
import com.sycoldstorage.wms.adapter.presentation.web.item.SearchItemCondition;
import com.sycoldstorage.wms.domain.item.ItemRepositoryCustom;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.sycoldstorage.wms.domain.customerItem.QCustomerItem.customerItem;
import static com.sycoldstorage.wms.domain.item.QItem.item;

public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ItemRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ItemDto> searchItems(SearchItemCondition condition) {
        return queryFactory
                .select(Projections.constructor(ItemDto.class
                        , item.id
                        , item.name
                        , item.unitWeight
                        , item.unitName
                        , item.registeredDate
                        , item.remarks
                ))
                .from(item)
                .where(
                        likeName(condition.getName())
                )
                .orderBy(item.id.asc())
                .fetch()
                ;
    }

    private BooleanExpression likeName(String name) {
        return StringUtils.isNotEmpty(name) ? item.name.like("%" + name + "%") : null;
    }


    /**
     * 해당 거래처의 품목 목록 (selectbox용)
     * @return
     */
    @Override
    public List<ItemSelectBoxDto> findItemsByCustomerId(long customerId) {
        return queryFactory
                .select(Projections.constructor(ItemSelectBoxDto.class
                        , item.id
                        , item.name
                        , new CaseBuilder().when(item.remarks.isNotEmpty())
                                .then(item.name.concat("(").concat(item.remarks).concat(")"))
                                .otherwise(item.name.concat("(").concat(item.unitWeight.stringValue()).concat(")"))
                        , item.unitWeight
                        , item.unitName
                ))
                .from(customerItem)
                .join(customerItem.item, item)
                .where(customerItem.customer.id.eq(customerId))
                .orderBy(item.id.asc())
                .fetch()
                ;
    }
}
