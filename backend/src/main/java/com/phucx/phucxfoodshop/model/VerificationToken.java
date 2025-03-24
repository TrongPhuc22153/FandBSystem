package com.phucx.phucxfoodshop.model;

import java.time.LocalDateTime;

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

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "verificationtoken")
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "VerificationToken.SaveVerificationtToken", procedureName = "SaveVerificationToken",
    parameters = {
        @StoredProcedureParameter(name="id", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="token", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="username", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="type", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="expiryDate", type = LocalDateTime.class, mode = ParameterMode.IN),
    }),
})
public class VerificationToken {
    @Id
    private String id;
    private String token;
    private String userID;
    private String type; 
    private LocalDateTime expiryDate;
}
