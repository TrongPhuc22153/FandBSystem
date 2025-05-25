package com.phucx.phucxfandb.service.waitlist;

import com.phucx.phucxfandb.constant.WaitListStatus;
import com.phucx.phucxfandb.dto.request.WaitListRequestParamsDTO;
import com.phucx.phucxfandb.dto.response.WaitListDTO;
import com.phucx.phucxfandb.entity.WaitList;
import org.springframework.data.domain.Page;

public interface WaitListReaderService {
    WaitListDTO getWaitList(String id);

    Page<WaitListDTO> getWaitLists(WaitListRequestParamsDTO params);

    WaitList getWaitListEntity(String id);

    boolean existsByTableIdAndWaitListStatus(String tableId, WaitListStatus status);
}
