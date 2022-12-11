package model;

import lombok.*;

import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRow {

    private Long orderRowId;

    private String itemName;

    @NonNull
    @Min(1)
    private Integer quantity;
    @NonNull
    @Min(1)
    private Double price;
}

