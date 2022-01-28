package com.sycoldstorage.wms.domain.item;

import com.sycoldstorage.wms.adapter.presentation.web.item.ItemDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * 품목 Entity
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@ToString
@EqualsAndHashCode(of="id")
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double unitWeight;

    @Column(nullable = false)
    private String unitName;

    @Column(nullable = false)
    private LocalDateTime registeredDate;

    private String remarks;


    public Item(ItemDto itemDto) {
        changeItem(itemDto);
        this.registeredDate = LocalDateTime.now();
    }

    public void changeItem(ItemDto itemDto) {
        this.name = itemDto.getName();
        this.unitWeight = itemDto.getUnitWeight();
        this.unitName = itemDto.getUnitName();
        this.registeredDate = itemDto.getRegisteredDate();
        this.remarks = itemDto.getRemarks();
    }

    /**
     * ItemDto로 변환
     * @return
     */
    public ItemDto toItemDto() {
        return ItemDto.builder()
                .id(this.id)
                .name(this.name)
                .unitWeight(this.unitWeight)
                .unitName(this.unitName)
                .registeredDate(this.registeredDate)
                .remarks(this.remarks)
                .build();
    }


}
