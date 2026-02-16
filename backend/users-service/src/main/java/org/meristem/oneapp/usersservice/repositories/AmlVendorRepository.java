package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.models.Vendor;

public interface AmlVendorRepository extends BaseRepository<Vendor, Long> {
    Vendor findAmlVendorByVendorName(String vendorName);

    Vendor findAmlVendorByVendorCode(String vendorCode);
}
