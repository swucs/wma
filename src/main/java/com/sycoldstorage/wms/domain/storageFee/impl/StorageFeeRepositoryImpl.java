package com.sycoldstorage.wms.domain.storageFee.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sycoldstorage.wms.adapter.presentation.web.storageFee.SearchStorageFeeCondition;
import com.sycoldstorage.wms.adapter.presentation.web.storageFee.StorageFeeDto;
import com.sycoldstorage.wms.domain.storageFee.StorageFeeRepositoryCustom;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.sycoldstorage.wms.domain.storageFee.QStorageFee.storageFee;

public class StorageFeeRepositoryImpl implements StorageFeeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public StorageFeeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 보관료 목록
     * @param condition
     * @return
     */
    @Override
    public List<StorageFeeDto> searchStorageFees(SearchStorageFeeCondition condition) {
        return queryFactory
                .select(Projections.constructor(
                        StorageFeeDto.class
                        , storageFee.id
                        , storageFee.baseDate
                        , storageFee.name
                        , storageFee.storage
                        , storageFee.loading
                ))
                .from(storageFee)
                .where(likeName(condition.getName()))
                .orderBy(storageFee.id.asc())
                .fetch();
    }

    /**
     * 이름 Like 검색
     * @param name
     * @return
     */
    private BooleanExpression likeName(String name) {
        return StringUtils.isNotEmpty(name) ? storageFee.name.like("%" + name + "%") : null;
    }
}
