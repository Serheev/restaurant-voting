package com.serheev.restaurant.to;

import com.serheev.restaurant.model.Dish;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MenuTo {
    @NotEmpty
    @Valid
    private List<Dish> dishes;
}
