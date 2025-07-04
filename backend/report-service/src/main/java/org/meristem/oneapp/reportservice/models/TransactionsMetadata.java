package org.meristem.oneapp.reportservice.models;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Table("transactions_metadata")
public class TransactionsMetadata extends BaseModel<String> {

    @Column("transaction_id")
    private Long transactionId;

    @Column("map_key")
    private String mapKey;

    @Column("map_value")
    private String mapValue;

    @Builder
    public TransactionsMetadata(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long transactionId, String key, String value) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.transactionId = transactionId;
        this.mapKey = key;
        this.mapValue = value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TransactionsMetadata that = (TransactionsMetadata) o;
        return Objects.equals(getTransactionId(), that.getTransactionId()) && Objects.equals(getMapKey(), that.getMapKey()) && Objects.equals(getMapValue(), that.getMapValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTransactionId(), getMapKey(), getMapValue());
    }
}
