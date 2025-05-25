package com.phucx.phucxfandb.service.waitlist;

import com.phucx.phucxfandb.dto.request.RequestWaitListDTO;
import com.phucx.phucxfandb.dto.response.WaitListDTO;
import org.springframework.security.core.Authentication;

public interface WaitListUpdateService {
    WaitListDTO createWaitList(Authentication authentication, RequestWaitListDTO createRequest);

    WaitListDTO updateWaitListStatus(String id, RequestWaitListDTO requestWaitListDTO);
    WaitListDTO updateWaitList(String id, RequestWaitListDTO updateRequest);
}
