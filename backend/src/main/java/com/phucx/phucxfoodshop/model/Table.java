package com.phucx.phucxfoodshop.model;

import com.phucx.phucxfoodshop.constant.TableStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@jakarta.persistence.Table(name = "tables")
public class Table {
    @Id
    private String tableID;
    private String tableNumber;
    private String location;
    private TableStatus status;
    private Integer capacity;
}
