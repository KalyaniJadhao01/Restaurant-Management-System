package com.restaurant.management.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopularMenuItemResponse {

    private String menuItemName;

    private Long quantitySold;

}