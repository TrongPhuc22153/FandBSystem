package com.phucx.phucxfoodshop.compositeKey;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data @Embeddable
public class EmployeeTerritoriesKey {
    private Integer employeeID;
    private Integer territoryID;
}
