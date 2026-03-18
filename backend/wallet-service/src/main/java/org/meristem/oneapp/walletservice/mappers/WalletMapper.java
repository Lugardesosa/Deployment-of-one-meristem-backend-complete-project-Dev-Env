package org.meristem.oneapp.walletservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.meristem.oneapp.walletservice.domains.responses.WalletAccountResponse;
import org.meristem.oneapp.walletservice.integrations.responses.MiddlewareWalletAccountResponse;

import java.util.List;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WalletMapper {
    WalletMapper INSTANCE = Mappers.getMapper(WalletMapper.class);



    List<WalletAccountResponse> walletVirtualAccountResponseToVirtualAccountResponse(List<MiddlewareWalletAccountResponse> responses);
    WalletAccountResponse walletVirtualAccountResponseToVirtualAccountResponse(MiddlewareWalletAccountResponse responses);
}
