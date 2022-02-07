package com.sycoldstorage.wms.domain.customerItem.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sycoldstorage.wms.adapter.presentation.web.customerItem.CustomerItemDto;
import com.sycoldstorage.wms.adapter.presentation.web.customerItem.SearchCustomerItemCondition;
import com.sycoldstorage.wms.domain.customerItem.CustomerItemRepositoryCustom;

import javax.persistence.EntityManager;
import java.util.List;

import static com.sycoldstorage.wms.domain.customer.QCustomer.customer;
import static com.sycoldstorage.wms.domain.customerItem.QCustomerItem.customerItem;
import static com.sycoldstorage.wms.domain.item.QItem.item;
import static com.sycoldstorage.wms.domain.storageFee.QStorageFee.storageFee;

public class CustomerItemRepositoryImpl implements CustomerItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CustomerItemRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 거래처별 품목 조회
     * @param condition
     * @return
     */
    @Override
    public List<CustomerItemDto> searchCustomerItems(SearchCustomerItemCondition condition) {
        return selectFromCustomerItemDto()
                .where(customer.id.eq(condition.getCustomerId()))
                .orderBy(item.id.asc())
                .fetch()
                ;
    }


    /**
     * 해당 거래처별 품목 조회
     * @param id
     * @return
     */
    @Override
    public CustomerItemDto findDtoById(long id) {
        return selectFromCustomerItemDto()
                .where(customerItem.id.eq(id))
                .fetchOne()
                ;
    }

    /**
     * 거래처별 품목 조회 selectFrom절
     * @return
     */
    private JPAQuery<CustomerItemDto> selectFromCustomerItemDto() {
        return queryFactory
                .select(Projections.constructor(CustomerItemDto.class
                        , customerItem.id.as("id")
                        , customer.id.as("customerId")
                        , customer.name.as("customerName")
                        , item.id.as("itemId")
                        , item.name.as("itemName")
                        , item.unitWeight.as("itemUnitWeight")
                        , item.unitName.as("itemUnitName")
                        , storageFee.id.as("storageFeeId")
                        , storageFee.name.as("storageFeeName")
                ))
                .from(customerItem)
                .join(customerItem.customer, customer)
                .join(customerItem.item, item)
                .join(customerItem.storageFee, storageFee);
    }


}
