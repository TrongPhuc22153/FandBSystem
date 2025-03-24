package com.phucx.phucxfoodshop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedStoredProcedureQueries;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "creditcard")
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "CreditCard.UpdateCreditCard", procedureName = "UpdateCreditCard",
    parameters = {
        @StoredProcedureParameter(name = "userId", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name = "creditName", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name = "creditNumber", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name = "expirationDate", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name = "securityCode", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name = "privatekey", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name = "result", type = Boolean.class, mode = ParameterMode.OUT),
    })
})
public class CreditCard {
    @Id
    private String id;
    private String name;
    private String number;
    private String expirationDate;
    private String securityCode;
    private String userID;
    public CreditCard(String name, String number, String expirationDate, String securityCode, String userID) {
        this.name = name;
        this.number = number;
        this.expirationDate = expirationDate;
        this.securityCode = securityCode;
        this.userID = userID;
    }
}
