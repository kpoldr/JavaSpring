package model;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private Long id;

    @NonNull
    @Size(min = 2)
    private String orderNumber;

    @Valid
    private OrderRow[] orderRows;
}
