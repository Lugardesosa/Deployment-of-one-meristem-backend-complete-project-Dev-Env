package org.meristem.oneapp.trustiesservice.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@NoArgsConstructor
@Getter
@Setter
@Table("simple_will")
public class SimpleWill extends Wills {
}
