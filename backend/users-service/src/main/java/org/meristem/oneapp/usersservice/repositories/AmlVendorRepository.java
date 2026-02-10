package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.models.AmlVendor;

public interface AmlVendorRepository extends BaseRepository<AmlVendor, Long> {
    AmlVendor findAmlVendorByVendorName(String vendorName);

    AmlVendor findAmlVendorByVendorCode(String vendorCode);
}
