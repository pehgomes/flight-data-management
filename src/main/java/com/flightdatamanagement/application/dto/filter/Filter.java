package com.flightdatamanagement.application.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Sort;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class Filter {

    @Builder.Default private int page = 0;
    @Builder.Default private int size = 10;
    @Builder.Default private Sort.Direction direction = Sort.Direction.ASC;

    abstract Enum<?> getField();
}
