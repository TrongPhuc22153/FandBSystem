package com.phucx.phucxfoodshop.model.entity;

import com.phucx.phucxfoodshop.constant.TableStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tables")
public class ReservationTable {
    @Id
    private String tableID;
    private String tableNumber;
    private String location;
    private TableStatus status;
    private Integer capacity;
}
