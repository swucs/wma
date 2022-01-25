package com.sycoldstorage.wms.domain.customer.impl;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sycoldstorage.wms.adapter.presentation.web.customer.CustomerDto;
import com.sycoldstorage.wms.adapter.presentation.web.customer.SearchCustomerCondition;
import com.sycoldstorage.wms.domain.customer.CustomerRepositoryCustom;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.sycoldstorage.wms.domain.customer.QCustomer.customer;

public class CustomerRepositoryImpl implements CustomerRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CustomerRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<CustomerDto> searchCustomers(SearchCustomerCondition condition) {

        return queryFactory
                .select(Projections.constructor(CustomerDto.class
                        , customer.id
                        , customer.name
                        , customer.businessNumber
                        , customer.representativeName
                        , customer.businessConditions
                        , customer.typeOfBusiness
                        , customer.address
                        , customer.phoneNumber
                        , customer.faxNumber
                        , new CaseBuilder().when(customer.use.eq(true)).then("Y").otherwise("N")
                ))
                .from(customer)
                .where(
                        likeName(condition.getName())
                        , equalId(condition.getId())
                        , equalUse(condition.getUseYn())
                )
                .fetch();
    }

    private Predicate equalUse(String useYn) {
        if (StringUtils.hasText(useYn)) {
            if ("Y".equals(useYn)) {
                return customer.use.eq(true);
            }

            if ("N".equals(useYn)) {
                return customer.use.eq(false);
            }
        }
        return null;
    }

    private Predicate equalId(Long id) {
        return id != null ? customer.id.eq(id) : null;
    }

    private BooleanExpression likeName(String name) {
        return StringUtils.hasText(name) ? customer.name.like("%" + name + "%") : null;
    }



}
