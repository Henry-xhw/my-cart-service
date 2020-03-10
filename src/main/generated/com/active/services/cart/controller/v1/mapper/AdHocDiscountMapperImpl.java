package com.active.services.cart.controller.v1.mapper;

import com.active.services.cart.domain.AdHocDiscount;
import com.active.services.cart.model.AdHocDiscountDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-03-10T17:50:50+0800",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_201 (Oracle Corporation)"
)
public class AdHocDiscountMapperImpl implements AdHocDiscountMapper {

    @Override
    public List<AdHocDiscount> toDomain(List<AdHocDiscountDto> adHocDiscounts) {
        if ( adHocDiscounts == null ) {
            return null;
        }

        List<AdHocDiscount> list = new ArrayList<AdHocDiscount>( adHocDiscounts.size() );
        for ( AdHocDiscountDto adHocDiscountDto : adHocDiscounts ) {
            list.add( adHocDiscountDtoToAdHocDiscount( adHocDiscountDto ) );
        }

        return list;
    }

    protected AdHocDiscount adHocDiscountDtoToAdHocDiscount(AdHocDiscountDto adHocDiscountDto) {
        if ( adHocDiscountDto == null ) {
            return null;
        }

        AdHocDiscount adHocDiscount = new AdHocDiscount();

        adHocDiscount.setIdentifier( adHocDiscountDto.getIdentifier() );
        adHocDiscount.setAdHocDiscountName( adHocDiscountDto.getAdHocDiscountName() );
        adHocDiscount.setAdHocDiscountKeyerId( adHocDiscountDto.getAdHocDiscountKeyerId() );
        adHocDiscount.setAdHocDiscountAmount( adHocDiscountDto.getAdHocDiscountAmount() );
        adHocDiscount.setAdHocDiscountCouponCode( adHocDiscountDto.getAdHocDiscountCouponCode() );
        adHocDiscount.setAdHocDiscountGroupId( adHocDiscountDto.getAdHocDiscountGroupId() );

        return adHocDiscount;
    }
}
